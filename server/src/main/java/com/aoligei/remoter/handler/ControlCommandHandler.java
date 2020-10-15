package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.beans.RemotingElement;
import com.aoligei.remoter.beans.OnlineElement;
import com.aoligei.remoter.business.aop.RequestInspect;
import com.aoligei.remoter.constant.IncompleteParamConstants;
import com.aoligei.remoter.constant.ResponseConstants;
import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.ResponseStatusEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IncompleteParamException;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.generate.IdentifyFactory;
import com.aoligei.remoter.manage.impl.RemotingRosterManage;
import com.aoligei.remoter.manage.impl.OnlineRosterManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wk-mia
 * 2020-9-5
 * 点对点模式服务端-远程控制处理器
 * 业务上：远程控制的请求是由Master端发出的，服务端并不负责远程控制的解析工作，仅仅
 * 负责将该远程控制信息转发给与之对应的Slave端。转发消息过程中若发生错误，服务器会向
 * Master返回错误信息；转发消息成功后，向Master发送请求发送成功信息并分配连接编码。
 *
 * Slave和Master收到连接编码后，通过连接编码向服务器申请连接。
 */
@Component(value = "ControlCommandHandler")
public class ControlCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 当前已连接到服务器的所有客户端
     */
    @Autowired
    private OnlineRosterManage onlineConnectionManage;
    /**
     * 处于连接中的通道组缓存
     */
    @Autowired
    private RemotingRosterManage remotingRosterManage;

    /**
     * 1. Master向Server发起控制请求，Server检查请求的Slaver是否存在、是否在工作中等；
     *    若不能建立远程连接，则返回错误信息；如可以建立连接，则新建连接组，并将Master
     *    加入到连接组中；
     * 2. Server发送控制请求给Slaver，请求中附带连接编码；
     * 3. Slaver选择是否建立连接，结果返回给Server，如同意连接，则准备开始屏幕截图任务；
     * 4. 如Slaver同意连接，则将Slaver加入到连接组中；如不同意，则清除该连接组；
     * 5  Server通知Master连接的结果；
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws ServerException
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.CONTROL_PARAMS})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws ServerException {
        if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.MASTER){
            /**主控端的业务逻辑*/
            this.masterHandle(channelHandlerContext,baseRequest);
        }else if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.SLAVE){
            /**受控端的业务逻辑*/
            this.slaverHandle(channelHandlerContext,baseRequest);
        }else {
            /**不允许的终端类型*/
            throw new IncompleteParamException(IncompleteParamConstants.TERMINAL_TYPE_ERROR);
        }
    }

    /**
     * 主控端的控制请求处理
     * @param channelHandlerContext
     * @param baseRequest
     * @throws ServerException
     */
    private void masterHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest)throws ServerException{
        /**检查所要控制的受控端是否能在当前连接到服务器的元数据列表中找到，以及该受控端目前是否已被控制。*/
        String slaveClientId = (String) baseRequest.getData();
        if(! onlineConnectionManage.isOnline(slaveClientId)){
            throw new ServerException(ServerExceptionConstants.SLAVE_NOT_FIND);
        }
        if(remotingRosterManage.isSlaverWorking(slaveClientId)){
            throw new ServerException(ServerExceptionConstants.SLAVE_BEING_CONTROLLED);
        }
        /**生成连接编码；并初始化该连接组，连接组中并没有Slaver。*/
        String connectionId = IdentifyFactory.createConnectionId();
        remotingRosterManage.registerMaster(connectionId,baseRequest.getClientId(),
                channelHandlerContext.channel());
        /**通知Slaver控制请求*/
        OnlineElement slaver = onlineConnectionManage.getSlaveInfoBySlaveClientId(slaveClientId);
        BaseResponse baseResponse = BuildUtil.buildResponseOK(connectionId,TerminalTypeEnum.SERVER_2_SLAVE,
                baseRequest.getCommandEnum(),null,null);
        slaver.getChannel().writeAndFlush(baseResponse);
    }

    /**
     * 受控端的控制请求处理
     * @param channelHandlerContext
     * @param baseRequest
     * @throws ServerException
     */
    private void slaverHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest)throws ServerException{
        /**
         * Slave同意远程请求并返回信息给服务器，在返回信息中带上连接编码。服务器缓存该连接组
         * 并通知Master远程控制达成的消息。
         */
        Boolean canConnect = (Boolean) baseRequest.getData();
        /**
         * 根据连接编码获取到发起控制请求的Master
         */
        if(StringUtils.isEmpty(baseRequest.getConnectionId())){
            throw new IncompleteParamException(IncompleteParamConstants.CONNECTION_ID_NULL);
        }
        RemotingElement remotingElement = remotingRosterManage.getChannelCacheByConnectionId(baseRequest.getConnectionId());
        if(remotingElement == null){
            throw new ServerException(ServerExceptionConstants.CONNECTION_NOT_FIND);
        }

        if(canConnect == null || !canConnect){
            /**
             * Slave拒绝Master发起的连接，将该连接组缓存移除，并通知Master该连接已被Slave拒绝。
             */
            BaseResponse baseResponse = BuildUtil.buildResponseERROR(null,TerminalTypeEnum.SERVER_2_MASTER,
                    baseRequest.getCommandEnum(), null, ResponseConstants.SLAVE_REFUSED_CONNECTION);
            remotingElement.getMasterElement().getChannel().writeAndFlush(baseResponse);

            remotingRosterManage.remotingRoster.remove(remotingElement);
            logInfo("slaver [" + baseRequest.getClientId() + "] has refused the connection");
        }else {
            /**
             * Slave同意Master发起的连接，将Slave的连接注册到连接组中，返回同意连接的消息给Master
             * 返回消息中带connectionId，用于标识这个连接组。
             */
            BaseResponse baseResponse = BuildUtil.buildResponseOK(baseRequest.getConnectionId(),TerminalTypeEnum.SERVER_2_MASTER,
                    baseRequest.getCommandEnum(),null,ResponseConstants.SLAVE_AGREE_CONNECTION);
            remotingElement.getMasterElement().getChannel().writeAndFlush(baseResponse);

            remotingRosterManage.registerSlave(baseRequest.getConnectionId(),baseRequest.getClientId(),
                    channelHandlerContext.channel());
            logInfo("slaver [" + baseRequest.getClientId() + "]  has accepted the connection");
        }
    }
}
