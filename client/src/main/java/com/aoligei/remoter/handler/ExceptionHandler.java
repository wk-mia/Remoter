package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.exception.ClientException;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-24
 * 点对点模式客户端-异常处理器
 * 负责处理服务器给出的异常
 */
@Component(value = "ExceptionHandler")
public class ExceptionHandler extends AbstractClientHandler  {

    /**
     * 特定的处理器-异常处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {

    }
}
