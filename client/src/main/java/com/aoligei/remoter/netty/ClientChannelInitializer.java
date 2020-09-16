package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.convert.RemoterDecoder;
import com.aoligei.remoter.convert.RemoterEncoder;
import com.aoligei.remoter.initial.AbstractRemoterChannelInitializer;
import io.netty.channel.ChannelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-15
 * 客户端通道初始化器
 */
@Component
public class ClientChannelInitializer extends AbstractRemoterChannelInitializer {

    @Autowired
    private NettyClientHandler channelHandler;

    @Override
    protected ChannelHandler setHandler() {
        return channelHandler;
    }

    @Override
    protected RemoterDecoder setDecoder() {
        return new RemoterDecoder(BaseResponse.class);
    }

    @Override
    protected RemoterEncoder setEncoder() {
        return new RemoterEncoder(BaseRequest.class);
    }

    public NettyClientHandler getChannelHandler(){
        return channelHandler;
    }
}
