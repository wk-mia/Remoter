package com.aoligei.remoter.enums;

/**
 * @author wk-mia
 * 2020-9-9
 * 客户端请求处理结果的状态枚举
 */
public enum StatusEnum {
    /**
     * 服务器正常处理
     */
    OK,
    /**
     * 服务器处理失败，通常是参数问题
     */
    FAIL,
    /**
     * 服务器处理错误，发生未知错误，不得不使服务器断开与客户端的连接
     */
    ERROR;
    private StatusEnum(){}
}
