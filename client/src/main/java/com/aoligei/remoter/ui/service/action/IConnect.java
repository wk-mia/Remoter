package com.aoligei.remoter.ui.service.action;

/**
 * @author wk-mia
 * 2020-9-26
 * 客户端与服务器的连接
 */
public interface IConnect {
    /**
     * 发起连接服务器请求
     */
    void connect();
    /**
     * 断开连接
     */
    void disconnect();
}
