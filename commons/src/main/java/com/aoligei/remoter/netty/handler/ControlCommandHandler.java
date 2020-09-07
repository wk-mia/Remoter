package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.account.ClientAccountBooks;
import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.aop.RequestInspect;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.beans.MetaCache;
import com.aoligei.remoter.netty.manage.GroupCacheManage;
import com.aoligei.remoter.netty.manage.OnlineConnectionManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.BeanUtils;
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
    @RequestInspect(inspectItem = {InspectEnum.CONTROL_PARAMS_IS_COMPLETE})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
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
                    throw new NettyServerException(ExceptionMessageConstants.SLAVE_BEING_CONTROLLED);
                }else {
                    /**
                     * 生成连接编码并通知双方
                     * 如何通知slaver???此时并不知道slaver的通道...
                     */
                    // TODO: 2020/9/5 成连接编码并通知双方
                    String connectionId = null;
                    MetaCache slaveCache = onlineConnectionManage.getOnlineConn().stream().filter(item -> slaveClientId.equals(item.getClientId())).findAny().get();


                    BaseResponse baseResponse = BuildUtil.buildResponse(connectionId,TerminalTypeEnum.SERVER,
                            baseRequest.getCommandEnum(),null);

                }
            }else {
                throw new NettyServerException(ExceptionMessageConstants.SLAVE_NOT_FIND);
            }
        }else if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.SLAVE){
            /**
             * 记录日志
             */
            logInfo(baseRequest,"Slave[" + baseRequest.getClientId() + "]已接受控制");
        }else {
            throw new NettyServerException(ExceptionMessageConstants.TERMINAL_TYPE_ERROR);
        }
    }
}
