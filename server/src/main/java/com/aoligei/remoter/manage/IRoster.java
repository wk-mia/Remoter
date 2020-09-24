package com.aoligei.remoter.manage;

import com.aoligei.remoter.beans.BasicClientInfo;
import com.aoligei.remoter.exception.ServerException;

/**
 * @author wk-mia
 * 2020-9-9
 * 已注册过的客户端花名册
 */
public interface IRoster {

    /**
     * 向账册中注册账户
     * @param BasicClientInfo 客户端信息
     * @return true/false
     * @throws ServerException
     */
    void register(BasicClientInfo BasicClientInfo) throws ServerException;

    /**
     * 注销客户端
     * @param clientId 客户端身份识别码
     * @return true/false
     * @throws ServerException
     */
    void unRegister(String clientId) throws ServerException;

    /**
     * 刷新客户端身份识别码
     * @param oldClientId 客户端原身份识别码
     * @return 客户端新身份识别码
     * @throws ServerException
     */
    String refreshClientId(String oldClientId)throws ServerException;

}
