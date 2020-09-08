package com.aoligei.remoter.netty.command;

import com.aoligei.remoter.exception.ServerException;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-1
 * 命令处理器接口
 */
public interface ICommandHandler<T> {

    /**
     * 处理当前命令
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param base Channel输入对象
     * @throws ServerException NettyServer异常信息
     */
    void handle(ChannelHandlerContext channelHandlerContext,T base)throws ServerException;

    /**
     * 丢弃当前命令
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseBound Channel输入对象
     */
    void abandon(ChannelHandlerContext channelHandlerContext,T baseBound);
}
