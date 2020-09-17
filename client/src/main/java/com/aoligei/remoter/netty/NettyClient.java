package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.business.RequestProcessor;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.handler.SponsorCommandHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-15
 * Netty-客户端，负责客户端与服务器的通信
 */
@Component
public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    @Autowired
    private RequestProcessor processor;

    /**
     * 处理器初始化器
     */
    @Autowired
    private ClientChannelInitializer channelInitializer;
    /**
     * IO线程池
     */
    private NioEventLoopGroup group;
    /**
     * 服务器地址
     */
    private String host;
    /**
     * 服务器端口
     */
    private int port;

    /**
     * 初始化
     */
    {
        group = new NioEventLoopGroup();
        host = "";
        port = 60001;
    }

    public void register() throws Exception{

    }

    /**
     * 连接服务器
     * @throws Exception
     */
    public void connect() throws Exception{
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(channelInitializer);
        final ChannelFuture sync = bootstrap.connect(host, port).sync();
        sync.channel().writeAndFlush(processor.buildConnectRequest());
        try {
            sync.channel().closeFuture();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    /**
     * 向服务器发送请求
     * @param baseRequest 请求体
     */
    public void sponsorCommand(BaseRequest baseRequest) throws SponsorException {
        NettyClientHandler channelHandler = channelInitializer.getChannelHandler();
        channelHandler.sponsorCommand(baseRequest);
    }

    /**
     * 销毁连接
     */
    public void destroy(){
        group.shutdownGracefully();
    }

}
