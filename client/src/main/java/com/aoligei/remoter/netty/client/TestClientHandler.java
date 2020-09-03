package com.aoligei.remoter.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
public class TestClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        System.out.println("客户端收到服务器发来的数据:" + byteBuf.toString(CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush(Unpooled.copiedBuffer("hello server!", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端发生错误,:" + cause.getMessage());
        ctx.close();
    }
}
