package com.aoligei.remoter;

import com.aoligei.remoter.ui.IHomePage;
import com.aoligei.remoter.ui.listener.MainActionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication

public class RemoterClientApplication {

    public static void main(String[] args) {
        // ConfigurableApplicationContext context = SpringApplication.run(RemoterClientApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(RemoterClientApplication.class);
        ConfigurableApplicationContext context = builder.headless(false).run(args);
        /**
         * 启动一个客户端的实例
         */
        IHomePage homePage = context.getBean(MainActionListener.class);
        homePage.start();
    }

}
