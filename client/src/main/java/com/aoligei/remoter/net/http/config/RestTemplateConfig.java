package com.aoligei.remoter.net.http.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author wk-mia
 * 2020-9-2
 * RestTemplate配置类
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 构建一个RestTemplate
     * @return RestTemplate对象
     */
    @Bean
    public RestTemplate build(){
        return new RestTemplate();
    }
}
