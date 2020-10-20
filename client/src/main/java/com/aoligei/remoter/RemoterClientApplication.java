package com.aoligei.remoter;

import com.aoligei.remoter.ui.service.action.IInteract;
import com.aoligei.remoter.ui.service.action.IStart;
import com.aoligei.remoter.ui.service.listener.HomePageActionListener;
import com.aoligei.remoter.ui.service.listener.SlaverPageActionListener;
import com.aoligei.remoter.util.AccessConfigUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication

public class RemoterClientApplication {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(RemoterClientApplication.class);
        ConfigurableApplicationContext context = builder.headless(false).run(args);
        /**启动一个客户端的实例*/
        IStart homePage = context.getBean(HomePageActionListener.class);
        homePage.start();
    }

}
