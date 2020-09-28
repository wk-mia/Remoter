package com.aoligei.remoter.command;

import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-1
 * 命令发起器接口
 */
public interface ICommandSponsor<T> {

    /**
     * 发起请求处理
     * @param request 请求
     * @throws Exception
     */
    void sponsor(T request)throws Exception;

    /**
     * 设置通道上下文
     * @param context 通道上下文
     */
    void setContext(ChannelHandlerContext context);
}
