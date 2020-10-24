package com.aoligei.remoter.service.action;

/**
 * @author wk-mia
 * 2020-9-26
 * 客户端远程控制
 */
public interface IControl {
    /**
     * 发起远程控制请求
     */
    void control();
    /**
     * 停止控制
     */
    void stopControl();
}
