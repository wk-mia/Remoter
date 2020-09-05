package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-5
 * 点对点模式服务端-远程控制处理器
 * 业务上：远程控制的请求是由Master端发出的，服务端并不负责远程控制的解析工作，仅仅
 * 负责将该远程控制信息转发给与之对应的Slave端。转发消息过程中若发生错误，服务器会向
 * Master返回错误信息；转发消息成功后，向Master发送请求发送成功信息。
 *
 * Slave收到控制请求后，给服务器回复一个可被控制的消息。服务器收到信息后，通知双方控
 * 制成功的消息。此时，Master才可发控制命令，Slave才可发屏幕截图信息等。
 */
public class ControlCommandHandler extends AbstractServerCensorC2CHandler {

    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {

    }
}
