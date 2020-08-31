package com.aoligei.remoter.account;

import com.aoligei.remoter.dto.ClientInformation;
import com.aoligei.remoter.exception.RemoterException;

/**
 * 变更身份信息账册
 */
public interface IAccountAlter {

    /**
     * 刷新客户端身份识别码
     * @param ip 客户端IP地址
     * @return 客户端信息
     * @throws RemoterException
     */
    ClientInformation refreshClientId(String ip)throws RemoterException;

    /**
     * 向账册中注册账户
     * @param clientInformation 客户端信息
     * @return true/false
     * @throws RemoterException
     */
    boolean register(ClientInformation clientInformation) throws RemoterException;

    /**
     * 从账册中移除账户
     * @param clientId 客户端身份识别码
     * @return true/false
     * @throws RemoterException
     */
    boolean destroy(String clientId) throws RemoterException;
}
