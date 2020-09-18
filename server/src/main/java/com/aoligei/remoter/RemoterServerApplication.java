package com.aoligei.remoter;


import com.aoligei.remoter.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RemoterServerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RemoterServerApplication.class, args);
        /**
         * 启动Netty服务端
         */
        NettyServer server = context.getBean(NettyServer.class);
        server.start();
    }
}
