package com.aoligei.remoter.bu.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class NettyServer {

    /**
     * 服务器端口
     */
    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new NettyServer(60001).start();
    }
    
    public void start() throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 用于启动NIO服务
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)// 实例化一个channel
                    .localAddress(new InetSocketAddress(port))// 监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                                      @Override
                                      protected void initChannel(SocketChannel socketChannel) throws Exception {
                                            socketChannel.pipeline()
                                                    .addLast(new TestServerHandler());
                                      }
                                  });
            // 绑定服务器
            ChannelFuture channelFuture = bootstrap.bind().sync();

            // 阻塞操作，closeFuture()开启了一个channel的监听器，直到链路断开
            channelFuture.channel().closeFuture().sync();
        } finally {
            // 关闭EventLoopGroup并释放所有资源，包括所有创建的线程
            group.shutdownGracefully().sync();
        }
    }
}
