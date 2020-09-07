package com.aoligei.remoter.enums;

/**
 * @author wk-mia
 * 2020-9-4
 * 请求检查项枚举
 */
public enum InspectEnum {
    /**
     * 连接时参数是否合法
     */
    CONNECT_PARAMS_IS_COMPLETE,
    /**
     * 控制时参数是否合法
     */
    CONTROL_PARAMS_IS_COMPLETE,
    /**
     * 请求不合法
     */
    REQUEST_IS_ILLEGAL,
    /**
     * 在线缓存中没有找到指定的连接
     */
    CONNECTION_NOT_FIND,
    /**
     * 一个主控端同时控制多个受控端的情况
     */
    MASTER_TO_SLAVES,
    /**
     * 一个受控端同时被多个主控端控制
     */
    SLAVE_TO_MASTERS,
    /**
     * 主控端没有在受控端的通道组中
     */
    MASTER_NOT_IN_GROUP;
    private InspectEnum(){}
}
