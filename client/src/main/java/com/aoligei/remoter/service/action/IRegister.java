package com.aoligei.remoter.service.action;

/**
 * @author wk-mia
 * 2020-9-26
 * 客户端注册
 */
public interface IRegister {
    /**
     * 发起注册请求
     */
    void register();

    /**
     * 刷新客户端身份识别码
     */
    void refreshClientId();
}
