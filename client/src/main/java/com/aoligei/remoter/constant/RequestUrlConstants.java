package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-8-31
 * 服务器接口地址常量配置类
 */
public class RequestUrlConstants {

    /**
     * 向服务器索要一个身份识别码
     */
    public static final String GET_CLIENT_ID = "/client/getClientId";
    /**
     * 检查身份识别码是否合法
     */
    public static final String IS_CLIENT_ID_LEGAL = "/client/isClientIdLegal";
}
