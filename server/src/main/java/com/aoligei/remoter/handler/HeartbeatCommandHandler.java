package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.business.aop.RequestInspect;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.exception.ServerException;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-3
 * 点对点模式服务端-心跳包处理器
 * 目前仅仅只是日志输出心跳包
 */
@Component(value = "HeartbeatCommandHandler")
public class HeartbeatCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 特定的处理器：心跳包处理器
     * 检查项 = {ORDINARY_PARAMS}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws ServerException
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.ORDINARY_PARAMS})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws ServerException {
        String clientId = baseRequest.getClientId();
        StringBuilder info = new StringBuilder().append("receive a heartbeat from:").append(clientId);
        logInfo(baseRequest,info.toString());
    }
}
