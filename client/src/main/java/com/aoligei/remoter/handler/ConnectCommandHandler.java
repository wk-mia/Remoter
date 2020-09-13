package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.exception.ServerException;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-13
 * 点对点模式客户端-连接处理器
 * 负责向服务器发起连接的请求，以及接受到服务器对于连接请求的处理结果。
 */
public class ConnectCommandHandler extends AbstractClientHandler {
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ServerException {

    }
}
