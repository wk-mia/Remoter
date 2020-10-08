package com.aoligei.remoter.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.net.InetSocketAddress;

/**
 * @author wk-mia
 * 2020-9-15
 * Netty-服务端，负责服务器与客户端的通信
 */
@Component
public class NettyServer {

    private static final Logger log = LoggerFactory.getLogger(NettyServer.class);

    /**
     * 处理器初始化器
     */
    @Autowired
    private ServerChannelInitializer channelInitialize;


    private void bind(int port) throws InterruptedException{
        final NioEventLoopGroup boss=new NioEventLoopGroup();
        final NioEventLoopGroup worker=new NioEventLoopGroup();
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss,worker)
                .channel(NioServerSocketChannel.class)
                /**保持连接数*/
                .option(ChannelOption.SO_BACKLOG,1024)
                /**保持连接*/
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(channelInitialize);
        try{

            final ChannelFuture future = bootstrap.bind(port).sync();
            if(future.isSuccess()){
                log.info("server start on port:{}",port);
            }

            future.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public void start(){
        try {
            int port = 60001;
            bind(port);
        }catch (InterruptedException e){
            log.error(e.getMessage(),e);
            e.printStackTrace();
        }
    }
}
