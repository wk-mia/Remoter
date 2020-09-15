package com.aoligei.remoter.initial;

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
public class RemoterChannelInitializer<T> extends ChannelInitializer<SocketChannel> {

    /**
     * 处理器
     */
    private ChannelHandler channelHandler;

    /**
     * 初始化
     */
    {
        /**
         * 跟据对应的类型初始化
         */
    }


    /**
     * 通道初始化
     * 添加编码器、解码器及handler
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(null)//编码器
                .addLast(null)//解码器
                .addLast(channelHandler);
    }
}
