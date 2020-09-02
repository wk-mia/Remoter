package com.aoligei.remoter.netty.master;

import com.aoligei.remoter.netty.beans.BaseResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author wk-mia
 * 2020-9-1
 * 控制端-输入数据处理器
 */
@ChannelHandler.Sharable
public class MasterChannelHandler extends SimpleChannelInboundHandler<BaseResponse> {

    private static Logger log = LoggerFactory.getLogger(MasterChannelHandler.class);

    /**
     * 缓存的上一次信息
     */
    private String preMessage;

    /**
     * 每个信息入站时都会被调用。
     * 客户端的处理主要包括有：发起命令/接受命令、发起连接请求/终止连接请求。
     * @param channelHandlerContext ChannelHandler和ChannelPipeline之间的上下文联系
     * @param baseResponse 请求响应体
     * @throws Exception 异常信息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws Exception {

    }

    /**
     * 异常处理：记录异常信息，关闭连接
     * 当异常发生时，很难从未知的异常信息中让连接恢复，所以这里直接关闭连接；
     * 当然，如果有某些特定的异常情况在掌控范围内，可直接在这里尝试恢复这个错误。
     * @param channelHandlerContext ChannelHandler和ChannelPipeline之间的上下文联系
     * @param cause 当前异常信息
     * @throws Exception 往上层抛异常信息
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        log.error(cause.getMessage(),cause);
        channelHandlerContext.close();
    }
}
