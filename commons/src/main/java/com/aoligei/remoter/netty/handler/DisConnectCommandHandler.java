package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import io.netty.channel.ChannelHandlerContext;

public class DisConnectCommandHandler extends AbstractServerCensorC2CHandler{
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {

    }
}
