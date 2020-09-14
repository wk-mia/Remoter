package com.aoligei.remoter.handler.master;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.handler.AbstractClientHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-14
 * 点对点模式主控端-屏幕截图处理器
 * 负责接受屏幕截图并处理
 */
public class ScreenShotsCommandHandler extends AbstractClientHandler {
    /**
     * 特定的处理器-屏幕截图处理器，供MASTER使用。
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        /**
         * 记录日志，加载收到的屏幕截图
         */

    }
}
