package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-8-28
 * 异常信息常量配置类
 */
public class ExceptionMessageConstants {

    /**
     * 程序内部错误
     */
    public static final String INTERNAL_ERROR = "an internal error occurred";
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
    public static final String SLAVE_BEING_CONTROLLED = "the client being controlled current";
    /**
     * 客户端工作异常
     */
    public static final String CLIENT_WORK_ERROR = "the client worked error";
    /**
     * 未在缓存中找到相应的受控端
     */
    public static final String SLAVE_NOT_FIND = "not find the slaver";
    /**
     * 主控端当前已经连接到服务器
     */
    public static final String MASTER_ALREADY_CONNECTED = "the master has already connected slave";
    /**
     * 暂不支持主控端同时远程多个受控终端
     */
    public static final String NOT_SUPPORT_MASTER_CONTROL_MULTIPLE_SLAVE = "not support master control multiple slave at the same time";
    /**
     * 暂不支持多个主控端同时远程一个受控端
     */
    public static final String NOT_SUPPORT_SLAVE_CONTROL_BY_MULTIPLE_MASTER = "not support the slave controlled by multiple master at the same time";
    /**
     * 主控端列表不能为空
     */
    public static final String TARGET_CLIENTS_EMPTY = "target Clients not allowed to be empty";
    /**
     * 主控端不在受控端的通道组内，需要先连接受控端
     */
    public static final String MASTER_NOT_IN_SLAVE_GROUP = "the master not in slave's group, need to connect with slave first";
    /**
     * 未知命令
     */
    public static final String UNKNOWN_COMMAND = "unknown command";
    /**
     * 连接编码为空
     */
    public static final String CONNECTION_ID_EMPTY = "the connection id not allow be empty";
    /**
     * 连接未找到
     */
    public static final String CONNECTION_NOT_FIND = "the specified connection was not found";
    /**
     * 终端类型为空
     */
    public static final String TERMINAL_TYPE_EMPTY = "the terminal type cannot be empty";
    /**
     * 命令类型为空
     */
    public static final String COMMAND_EMPTY = "the command cannot be empty";
    /**
     * 主控端请求连接时未指定受控端
     */
    public static final String NO_SLAVER_SPECIFIED = "no slaver's client id specified in the request";
    /**
     * 终端类型为空
     */
    public static final String TERMINAL_TYPE_IS_EMPTY = "terminal type not allow empty";
    /**
     * 数据区域为空
     */
    public static final String DATA_IS_NULL = "data not allowed to be empty";
    /**
     * 终端类型错误
     */
    public static final String TERMINAL_TYPE_ERROR = "the terminal type can't be an unexpected value";
}
