package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-8-28
 * 异常信息常量配置类
 */
public class ExceptionMessageConstants {

    /**
     * 无法获取请求的Ip地址
     */
    public static final String UNABLE_GET_IP = "unable to get IP address";

    /**
     * 客户端信息不完备
     */
    public static final String CLIENT_INFO_NOT_COMPLETE = "the client info are not complete";
    /**
     * 客户端身份识别码不允许为空
     */
    public static final String CLIENT_ID_EMPTY = "client_ID not allowed to be empty";
    /**
     * 客户端当前已存在
     */
    public static final String CLIENT_ALREADY_EXISTS = "the client already exists";
    /**
     * 为找到此客户端
     */
    public static final String CLIENT_NOT_FIND = "not find the client";
    /**
     * 为找到此身份识别码
     */
    public static final String CLIENT_ID_NOT_FIND = "not find the clientId";
    /**
     * 客户端当前正处于被远程控制中
     */
    public static final String CLIENT_BEING_CONTROLLED = "The client being controlled current";
}
