package com.aoligei.remoter.initial;

import com.aoligei.remoter.convert.RemoterDecoder;
import com.aoligei.remoter.convert.RemoterEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

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
                .addLast(setDecoder())
                .addLast(setEncoder())
                .addLast(setHandler());
    }

    protected abstract ChannelHandler setHandler();

    protected abstract RemoterDecoder setDecoder();

    protected abstract RemoterEncoder setEncoder();
}
