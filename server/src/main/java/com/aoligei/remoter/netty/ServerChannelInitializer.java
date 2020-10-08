package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.convert.RemoterDecoder;
import com.aoligei.remoter.convert.RemoterEncoder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author wk-mia
 * 2020-9-15
 * 服务器通道初始化器
 */
@Component
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private ServerChannelC2CHandler channelHandler;

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

    private ChannelHandler setHandler() {
        return channelHandler;
    }

    private RemoterDecoder setDecoder() {
        return new RemoterDecoder(BaseRequest.class);
    }

    private RemoterEncoder setEncoder() {
        return new RemoterEncoder(BaseResponse.class);
    }

    public ServerChannelC2CHandler getChannelHandler(){
        return channelHandler;
    }
}
