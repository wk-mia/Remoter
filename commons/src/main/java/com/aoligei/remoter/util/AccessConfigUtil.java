package com.aoligei.remoter.util;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Properties;

/**
 * @author wk-mia
 * 2020-9-26
 * 获取配置工具类
 */
public class AccessConfigUtil {

    /**
     * 获取参数
     * @param key 键
     * @return 值
     */
    public static String getValue(Config config,String key){
        String path = config.path;
        Resource resource = new ClassPathResource(path);
        Properties properties = null;
        try{
            PropertiesFactoryBean bean = new PropertiesFactoryBean();
            bean.setLocation(resource);
            /**防止bean的对象还没有被初始化完成*/
            bean.afterPropertiesSet();
            properties = bean.getObject();
            String value = (String) properties.get(key);
            /**转换编码*/
            return new String(value.getBytes("ISO8859-1"),"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取参数
     * @param config 配置类型
     * @param key 键
     * @return 值
     */
    public static int getNumber(Config config,String key){
        String number = getValue(config,key);
        /**转换数字*/
        try {
            return Integer.valueOf(number).intValue();
        }catch (NumberFormatException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取参数
     * @param config 配置类型
     * @param key 键
     * @return 值
     */
    public static float getFloat(Config config,String key){
        String number = getValue(config,key);
        /**转换数字*/
        try {
            return Float.parseFloat(number);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return -1.0f;
        }
    }

    /**
     * 配置类型枚举
     */
    public enum Config{
        /**
         * 参数类配置
         */
        PARAM("remote/param.properties"),
        /**
         * 消息类配置
         */
        MSG("remote/msg.properties");
        /**
         * 配置目录
         */
        private String path;
        private Config(String path){ this.path = path; }
    }
}
