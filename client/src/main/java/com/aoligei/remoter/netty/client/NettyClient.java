package com.aoligei.remoter.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class NettyClient {

    private final String host;

    private final int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception{
        // IO线程池
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new TestClientHandler());
                        }
                    });
            // 连接到远程节点，等待连接完成
            ChannelFuture channelFuture = bootstrap.connect().sync();
            // 发送消息到服务器端
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer("hello server", CharsetUtil.UTF_8));

            // 阻塞操作，closeFuture()开启了一个channel的监听器，直到链路断开
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args)throws Exception{
        new NettyClient("127.0.0.1",60001).run();
    }
}
