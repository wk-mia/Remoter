package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.aop.InspectEnum;
import com.aoligei.remoter.netty.aop.RequestInspect;
import com.aoligei.remoter.netty.beans.BaseRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-3
 * 点对点模式服务端-心跳包处理器
 * 目前仅仅只是日志输出心跳包
 */
public class HeartbeatCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 特定的处理器：心跳包处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws NettyServerException
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.NO_CLEAR_CLIENT_ID})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
        String clientId = baseRequest.getClientId();
        StringBuilder info = new StringBuilder().append("receive a heartbeat from:").append(clientId);
        logInfo(baseRequest,info.toString());
    }
}
