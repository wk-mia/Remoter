package com.aoligei.remoter.initial;

import com.aoligei.remoter.convert.RemoterDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author wk-mia
 * 2020-9-15
 * 通道初始化器
 */
@Component
public abstract class AbstractRemoterChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 处理器
     */
    private ChannelHandler channelHandler;

    /**
     * 通道初始化
     * 添加编码器、解码器及handler
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(setDecoder())//编码器
                .addLast(setEncoder())//解码器
                .addLast(setHandler());
    }

    protected abstract ChannelHandler setHandler();

    protected abstract RemoterDecoder setDecoder();

    protected abstract ChannelHandler setEncoder();
}
