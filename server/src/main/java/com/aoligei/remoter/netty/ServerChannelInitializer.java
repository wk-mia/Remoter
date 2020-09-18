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
 * 服务器通道初始化器
 */
@Component
public class ServerChannelInitializer  extends AbstractRemoterChannelInitializer {

    @Autowired
    private ServerChannelC2CHandler channelHandler;

    @Override
    protected ChannelHandler setHandler() {
        return channelHandler;
    }

    @Override
    protected RemoterDecoder setDecoder() {
        return new RemoterDecoder(BaseRequest.class);
    }

    @Override
    protected RemoterEncoder setEncoder() {
        return new RemoterEncoder(BaseResponse.class);
    }

    public ServerChannelC2CHandler getChannelHandler(){
        return channelHandler;
    }
}
