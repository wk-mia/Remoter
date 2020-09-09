package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-9-8
 * 服务器处理异常常量配置类
 * 该类中定义了服务器处理客户端的请求异常的常量
 */
public class ServerExceptionConstants {
    /**
     * 客户端已经注册过
     */
    public static final String CLIENT_ALREADY_REGISTER = "the client has already registered";
    /**
     * 客户端未注册
     */
    public static final String CLIENT_NOT_REGISTER = "the client has not registered";
    /**
     * 连接未找到
     */
    public static final String CONNECTION_NOT_FIND = "the specified connection was not found";
    /**
     * 暂不支持主控端同时远程多个受控终端
     */
    public static final String NOT_SUPPORT_MASTER_CONTROL_MULTIPLE_SLAVE = "not support master control multiple slave at the same time";
    /**
     * 暂不支持多个主控端同时远程一个受控端
     */
    public static final String NOT_SUPPORT_SLAVE_CONTROL_BY_MULTIPLE_MASTER = "not support the slave controlled by multiple master at the same time";
    /**
     * 主控端不在受控端的通道组内，需要先连接受控端
     */
    public static final String MASTER_NOT_IN_SLAVE_GROUP = "the master not in slave's group, need to connect with slave first";
    /**
     * 客户端当前正处于被远程控制中
     */
    public static final String SLAVE_BEING_CONTROLLED = "the client being controlled current";
    /**
     * 未在缓存中找到相应的受控端
     */
    public static final String SLAVE_NOT_FIND = "not find the slaver";
    /**
     * 主控端当前已在控制一个受控端
     */
    public static final String MASTER_ALREADY_CONNECTED = "the master has already connected slave";
    /**
     * 客户端工作异常
     */
    public static final String CLIENT_WORK_ERROR = "the client worked error";
    /**
     * 为找到此客户端
     */
    public static final String CLIENT_NOT_FIND = "not find the client";
}
