package com.aoligei.remoter;

import com.aoligei.remoter.ui.IHomePage;
import com.aoligei.remoter.ui.form.MainForm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class,args);
        /**
         * 启动一个客户端的实例
         */
        IHomePage homePage = new MainForm("主控端");
        homePage.start();
    }

}
