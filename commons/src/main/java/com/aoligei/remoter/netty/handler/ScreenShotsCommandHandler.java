package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-3
 * 点对点模式服务端-屏幕截图处理器
 * 业务上：屏幕截图的信息是由Slava端发出的，服务端并不负责屏幕截图的解析和展示，仅仅
 * 负责将该屏幕截图信息转发给所有关注着这个Slave端的Master集。需注意的是，尽管在代码
 * 中，是遍历Masters集合分别转发该消息；但因目前暂不支持单个Slaver同时被多个Masters
 * 关注，所以实质上仍是点对点的业务场景。
 */
public class ScreenShotsCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 特定的处理器：屏幕截图处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws NettyServerException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {

    }
}
