package com.aoligei.remoter.constant;

/**TERMINAL_TYPE_EMPTY
 * @author wk-mia
 * 2020-9-8
 * 请求参数不全常量配置类
 * 该类中定义了客户端发来的请求中参数不齐全的常量
 */
public class IncompleteParamConstants {
    /**
     * 数据区中没有指定客户端名称
     */
    public static final String CLIENT_NAME_NOT_IN_DATA = "the client name is not included in the data area";
    /**
     * 数据区中没有指定客户端IP地址
     */
    public static final String CLIENT_IP_NOT_IN_DATA = "the client ip address is not included in the data area";
    /**
     * 数据区中没有指定客户端是否已拒绝控制请求
     */
    public static final String REJECT_CONNECTION_NOT_IN_DATA = "the reject connection status is not included in the data area";
    /**
     * 客户端发出的请求体为空
     */
    public static final String REQUEST_NULL = "the request cannot be empty";
    /**
     * 客户端身份识别码为空
     */
    public static final String CLIENT_ID_NULL = "the client id cannot be empty";
    /**
     * 命令为空
     */
    public static final String COMMAND_NULL = "the command cannot be empty";
    /**
     * 连接编码为空
     */
    public static final String CONNECTION_ID_NULL = "the connection id cannot be empty";
    /**
     * 数据区不能为空
     */
    public static final String DATA_NULL = "the data cannot be empty";
    /**
     * 终端类型不能为空
     */
    public static final String TERMINAL_TYPE_NULL = "the terminal type cannot be empty";
    /**
     * 数据区中不包含受控端身份识别码
     */
    public static final String NO_SLAVER_SPECIFIED = "the data not contain the slaver id";
    /**
     * 终端类型错误
     */
    public static final String TERMINAL_TYPE_ERROR = "the terminal type can't be an unexpected value";
}
