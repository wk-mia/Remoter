package com.aoligei.remoter.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-21
 * Spring工具类
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 注入ApplicationContext
     * @param applicationContext 上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringBeanUtil.applicationContext == null) {
            SpringBeanUtil.applicationContext = applicationContext;
        }
    }

    /**
     * 通过名称及制定类型获取Bean
     * @param name 类名称
     * @param clazz 类对象名称
     * @param <T> 类型
     * @return Bean
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 通过名称获取Bean
     * @param name 类名称
     * @return Bean
     */
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    /**
     * 通过类对象名称获取Bean.
     * @param clazz 类对象名称
     * @param <T> 类型
     * @return Bean
     */
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }
}
