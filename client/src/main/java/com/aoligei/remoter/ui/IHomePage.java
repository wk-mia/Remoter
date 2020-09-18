package com.aoligei.remoter.ui;

/**
 * @author wk-mia
 * 2020-9-15
 * 主窗体接口定义
 */
public interface IHomePage {
    /**
     * 启动并展示主页
     */
    void start();

    /**
     * 发起注册请求
     */
    void register();

    /**
     * 发起连接服务器请求
     */
    void connect();

    /**
     * 发起远程控制请求
     */
    void control();

    /**
     * 断开连接
     */
    void disconnect();

    /**
     * 停止控制
     */
    void stopControl();
}
