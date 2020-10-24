package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.manage.SingleTaskManage;
import com.aoligei.remoter.util.AccessConfigUtil;
import com.aoligei.remoter.util.AccessConfigUtil.Config;
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
@Component(value = "NettyClient")
public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    /**处理器初始化器*/
    private ClientChannelInitializer channelInitializer;
    @Autowired
    private NettyClient(ClientChannelInitializer channelInitializer){
        this.channelInitializer = channelInitializer;
    }

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
        host = AccessConfigUtil.getValue(Config.PARAM,"remoter.server.ip");
        port = AccessConfigUtil.getNumber(Config.PARAM,"remoter.server.port");
    }


    /**
     * 连接服务器
     * @throws Exception
     */
    public void connect(BaseRequest connectRequest) throws Exception{
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                .handler(channelInitializer);
        try {
            final ChannelFuture sync = bootstrap.connect(host, port).sync();
            sync.channel().writeAndFlush(connectRequest);
            sync.channel().closeFuture().sync();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }finally {

        }
    }

    /**
     * 销毁连接
     */
    public void destroy(){
        group.shutdownGracefully();
    }

}
