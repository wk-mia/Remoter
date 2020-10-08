package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.convert.RemoterDecoder;
import com.aoligei.remoter.convert.RemoterEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-15
 * 客户端通道初始化器
 */
@Component
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyClientHandler channelHandler;

    /**
     * 通道初始化
     * 添加编码器、解码器及handler
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                //.addLast(new IdleStateHandler(20,10,0))
                .addLast(setDecoder())
                .addLast(setEncoder())
                .addLast(setHandler());
    }

    private ChannelHandler setHandler() {
        return channelHandler;
    }

    private RemoterDecoder setDecoder() {
        return new RemoterDecoder(BaseResponse.class);
    }

    private RemoterEncoder setEncoder() {
        return new RemoterEncoder(BaseRequest.class);
    }

    public NettyClientHandler getChannelHandler(){
        return channelHandler;
    }
}
