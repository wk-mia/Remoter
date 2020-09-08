package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.constant.IncompleteParamConstants;
import com.aoligei.remoter.constant.ResponseConstants;
import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.dto.ClientInformation;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IncompleteParamException;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.generate.impl.UUIDIdentifier;
import com.aoligei.remoter.netty.aop.RequestInspect;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.beans.ChannelCache;
import com.aoligei.remoter.netty.beans.MetaCache;
import com.aoligei.remoter.netty.manage.GroupCacheManage;
import com.aoligei.remoter.netty.manage.OnlineConnectionManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

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
public class ControlCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 当前已连接到服务器的所有客户端
     */
    @Autowired
    private OnlineConnectionManage onlineConnectionManage;
    /**
     * 处于连接中的通道组缓存
     */
    @Autowired
    private GroupCacheManage groupCacheManage;

    @Override
    @RequestInspect(inspectItem = {InspectEnum.CONTROL_PARAMS})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws ServerException {
        if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.MASTER){
            /**
             * 检查所要控制的受控端是否能在当前连接到服务器的元数据列表中找到，
             * 以及该受控端目前是否已被控制。
             */
            String slaveClientId = (String) baseRequest.getData();
            if(onlineConnectionManage.getOnlineConn().stream().filter(item ->
                    slaveClientId.equals(item.getClientId())).findAny().isPresent()){

                if(groupCacheManage.caches.stream().filter(item ->
                        slaveClientId.equals(item.getSlaveMeta().getClientId())).findAny().isPresent()){
                    throw new ServerException(ServerExceptionConstants.SLAVE_BEING_CONTROLLED);
                }else {
                    /**
                     * 生成连接编码并通知Slave建立与Master的通信。并缓存该通道组，通道组中并没有Slaver。
                     */
                    String connectionId = new UUIDIdentifier().provide();
                    ClientInformation slaveInfo = onlineConnectionManage.getSlaveInfoBySlaveClientId(slaveClientId);
                    BaseResponse baseResponse = BuildUtil.buildResponse(connectionId,TerminalTypeEnum.SERVER,
                            baseRequest.getCommandEnum(),null);
                    groupCacheManage.registerMaster(baseRequest.getConnectionId(),baseRequest.getClientId(),
                            channelHandlerContext.channel(),groupCacheManage.getScheduled(channelHandlerContext,3));
                    slaveInfo.getChannel().writeAndFlush(baseResponse);
                }
            }else {
                throw new ServerException(ServerExceptionConstants.SLAVE_NOT_FIND);
            }


        }else if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.SLAVE){
            /**
             * Slave同意远程请求并返回信息给服务器，在返回信息中带上连接编码。服务器缓存该连接组
             * 并通知Master远程控制达成的消息。
             */
            Boolean canConnect = (Boolean) baseRequest.getData();
            if(baseRequest.getConnectionId() == null || "".equals(baseRequest.getConnectionId())){
                throw new IncompleteParamException(IncompleteParamConstants.CONNECTION_ID_NULL);
            }
            /**
             * 获取到Master
             */
            ChannelCache channelCache = groupCacheManage.getChannelCacheByConnectionId(baseRequest.getConnectionId());
            if(channelCache == null){
                throw new ServerException(ServerExceptionConstants.CONNECTION_NOT_FIND);
            }
            if(canConnect == null || !canConnect){
                /**
                 * Slave拒绝Master发起的连接，将该连接组缓存移除，并通知Master该连接已被Slave拒绝。
                 */
                BaseResponse baseResponse = BuildUtil.buildResponse(null,TerminalTypeEnum.SERVER,
                        baseRequest.getCommandEnum(), ResponseConstants.SLAVE_REFUSED_CONNECTION);
                channelCache.getMasterMeta().getChannel().writeAndFlush(baseResponse);
                groupCacheManage.caches.remove(channelCache);
                logInfo(baseRequest,"Slave[" + baseRequest.getClientId() + "]已拒绝控制");
            }else {
                /**
                 * Slave同意Master发起的连接，将Slave的连接注册到连接组中，返回同意连接的消息给Master
                 * 返回消息中带connectionId，用于标识这个连接组。
                 */
                BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getConnectionId(),TerminalTypeEnum.SERVER,
                        baseRequest.getCommandEnum(),ResponseConstants.SLAVE_AGREE_CONNECTION);
                channelCache.getMasterMeta().getChannel().writeAndFlush(baseResponse);
                groupCacheManage.registerSlave(baseRequest.getConnectionId(),baseRequest.getClientId(),
                        channelHandlerContext.channel(),groupCacheManage.getScheduled(channelHandlerContext,3));
                logInfo(baseRequest,"Slave[" + baseRequest.getClientId() + "]已接受控制");
            }
        }else {
            throw new IncompleteParamException(IncompleteParamConstants.TERMINAL_TYPE_ERROR);
        }
    }
}
