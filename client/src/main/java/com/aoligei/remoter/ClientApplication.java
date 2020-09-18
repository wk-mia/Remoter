package com.aoligei.remoter;

import com.aoligei.remoter.ui.IHomePage;
import com.aoligei.remoter.ui.listener.MainActionListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ClientApplication.class, args);
        /**
         * 启动一个客户端的实例
         */
        IHomePage homePage = context.getBean(MainActionListener.class);
        homePage.start();
    }

}
