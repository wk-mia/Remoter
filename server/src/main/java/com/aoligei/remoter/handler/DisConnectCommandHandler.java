package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.exception.ServerException;
import io.netty.channel.ChannelHandlerContext;

public class DisConnectCommandHandler extends AbstractServerCensorC2CHandler{
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws ServerException {

    }
}
