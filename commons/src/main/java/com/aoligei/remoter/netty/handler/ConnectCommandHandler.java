package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.constant.ResponseConstants;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.beans.GroupCacheManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wk-mia
 * 2020-9-2
 * 点对点模式服务端-连接处理器
 * 负责初始话连接信息并维护所有连接信息。
 */
public class ConnectCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 当前的所有连接
     */
    @Autowired
    private GroupCacheManage groupChannelManage;

    /**
     * 特定的处理器：连接处理器
     * 初始话连接并维护这个连接信息
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws NettyServerException 异常信息
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
        /**
         * 如果TargetClientIds为空，代表受控端，尝试将该连接加入到通道组中；
         * 如果TargetClientIds不为空，代表主控端，尝试将该连接加入到通道组中；
         * 目前暂不支持一个受控端被多个主控端控制的模式以及一个主控端同时远程
         * 多个受控终端。
         */
        if(baseRequest.getTargetClientIds() == null){
            /**
             * 注册受控端，返回信息，输出日志
             */
            Channel channel = channelHandlerContext.channel();
            groupChannelManage.registerSlave(baseRequest.getClientId(),channel,
                    groupChannelManage.getScheduled(channelHandlerContext,channel,3));

            BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getClientId(),
                    baseRequest.getTargetClientIds(),baseRequest.getCommandEnum(), ResponseConstants.SLAVE_CONNECT_SUCCEEDED, null);
            channelHandlerContext.writeAndFlush(baseResponse);

            logInfo(baseRequest,"Slave[" + baseRequest.getClientId() + "]连接服务器成功");
        }else{
            if(baseRequest.getTargetClientIds().size() > 1){
                /**
                 * 不支持主控端同时连接多个受控端
                 */
                throw new NettyServerException(ExceptionMessageConstants.NOT_SUPPORT_MASTER_CONTROL_MULTIPLE_SLAVE);
            }else {
                /**
                 * 注册主控端，返回信息，输出日志
                 */
                if(groupChannelManage.currentMastersCount((String)baseRequest.getTargetClientIds().get(0)) == 0){
                    Channel channel = channelHandlerContext.channel();
                    groupChannelManage.registerMasters((String) baseRequest.getTargetClientIds().get(0),baseRequest.getClientId()
                            ,channel, groupChannelManage.getScheduled(channelHandlerContext,channel,3));

                    BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getClientId(),
                            baseRequest.getTargetClientIds(),baseRequest.getCommandEnum(), ResponseConstants.MASTER_CONNECT_SUCCEEDED, null);
                    channelHandlerContext.writeAndFlush(baseResponse);

                    logInfo(baseRequest,"Master[" + baseRequest.getClientId() + "]连接服务器成功");
                }else {
                    /**
                     * 不支持受控端同时被多个主控端控制
                     */
                    throw new NettyServerException(ExceptionMessageConstants.NOT_SUPPORT_SLAVE_CONTROL_BY_MULTIPLE_MASTER);
                }
            }
        }
    }
}
