package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-11-8
 * 请求参数缺失异常
 */
public final class MissingParamConstants {
    /**
     * 没有指定客户端名称
     */
    public static final String CLIENT_NAME_NOT_INCLUDED = "the client name is not included";
    /**
     * 没有指定客户端IP地址
     */
    public static final String CLIENT_IP_NOT_INCLUDED = "the client ip address is not included";
    /**
     * 数据区中没有指定客户端是否已拒绝控制请求
     */
    public static final String REJECT_CONNECTION_NOT_INCLUDED = "the reject connection status is not included in the data area";
    /**
     * 客户端发出的请求体为空
     */
    public static final String REQUEST_CANNOT_BE_EMPTY = "the request cannot be empty";
    /**
     * 客户端身份识别码为空
     */
    public static final String CLIENT_ID_CANNOT_BE_EMPTY = "the client id cannot be empty";
    /**
     * 命令为空
     */
    public static final String COMMAND_CANNOT_BE_EMPTY = "the command cannot be empty";
    /**
     * 连接编码为空
     */
    public static final String CONNECTION_ID_CANNOT_BE_EMPTY = "the connection id cannot be empty";
    /**
     * 数据区不能为空
     */
    public static final String DATA_CANNOT_BE_EMPTY = "the data cannot be empty";
    /**
     * 终端类型不能为空
     */
    public static final String TERMINAL_TYPE_CANNOT_BE_EMPTY = "the terminal type cannot be empty";
    /**
     * 数据区中不包含受控端身份识别码
     */
    public static final String NO_SLAVER_SPECIFIED = "the data not contain the slaver id";
}
