package com.aoligei.remoter.enums;

/**
 * @author wk-mia
 * 2020-9-4
 * 请求检查项枚举
 */
public enum InspectEnum {
    /**
     * 注册时参数是否合法
     */
    REGISTER_PARAM,
    /**
     * 连接时参数是否合法
     */
    CONNECT_PARAMS,
    /**
     * 控制时参数是否合法
     */
    CONTROL_PARAMS,
    /**
     * 常规的参数校验
     * 包括键盘、鼠标、屏幕截图、声音、心跳包等命令
     */
    ORDINARY_PARAMS,
    /**
     * 断开连接的请求校验
     */
    DISCONNECT_PARAMS,
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
