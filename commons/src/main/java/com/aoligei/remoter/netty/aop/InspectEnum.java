package com.aoligei.remoter.netty.aop;

/**
 * @author wk-mia
 * 2020-9-4
 * 请求检查项枚举
 */
public enum InspectEnum {
    /**
     * 请求中没用明确身份识别码
     */
    NO_CLEAR_CLIENT_ID,
    /**
     * 在线缓存中没有找到指定的受控端
     */
    SLAVE_NOT_WORK,
    /**
     * 一个主控端同时控制多个受控端的情况
     */
    MASTER_TO_SLAVES,
    /**
     * 主控端没有在受控端的通道组中
     */
    MASTER_NOT_IN_GROUP;
    private InspectEnum(){}
}
