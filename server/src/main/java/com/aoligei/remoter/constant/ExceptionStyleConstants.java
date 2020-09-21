package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-9-21
 * 异常处理类型常量
 */
public class ExceptionStyleConstants {
    /**
     * 在可控范围内的异常：
     * 非法业务请求、参数不全等
     */
    public static final String CONTROLLABLE = "IncompleteParamException";
    /**
     * 需断开连接的异常：
     * 非法连接请求、服务器负载过大等
     */
    public static final String NEED_CLOSE_CONNECT = "ServerException";
}
