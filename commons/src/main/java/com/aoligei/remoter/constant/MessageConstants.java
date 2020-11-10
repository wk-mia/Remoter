package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-11-8
 * 消息常量
 */
public final class MessageConstants {
    /**
     * 客户端端连接服务器成功
     */
    public static final String CLIENT_CONNECT_SUCCEEDED = "the client connect succeeded";
    /**
     * 受控端拒绝了这次连接请求
     */
    public static final String SLAVE_REFUSED_CONNECTION = "the slave refused this connection";
    /**
     * 受控端接受了这次连接请求
     */
    public static final String SLAVE_AGREE_CONNECTION = "the slave agree this connection";
    /**
     * 服务器即将关闭连接
     */
    public static final String WILL_BE_DISCONNECTED = "server will be close the disconnection";
    /**
     * 即将向服务器发送消息
     */
    public static final String PREPARE_SEND = "the request will be send soon";
    /**
     * 丢失与服务器的连接
     */
    public static final String LOST_CONNECTION = "the connection to the server was lost";
}
