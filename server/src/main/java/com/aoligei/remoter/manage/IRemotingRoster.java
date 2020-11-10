package com.aoligei.remoter.manage;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.exception.RemoterException;
import io.netty.channel.Channel;


/**
 * @author wk-mia
 * 2020-9-2
 * 远程控制工作中的客户端花名册
 */
public interface IRemotingRoster {

    /**
     * 向在线通道分组管理器注册受控端的实例
     * @param connectionId 连接编码
     * @param slaveClientId 受控端的身份识别码
     * @param channel 通道
     * @throws RemoterException 异常信息
     */
    void registerSlave(String connectionId, String slaveClientId, Channel channel)throws RemoterException;

    /**
     * 向在线通道分组管理器注册主控端的实例
     * @param connectionId 受连接编码
     * @param masterClientId 主控端的身份识别码
     * @param channel 通道
     * @throws RemoterException 异常信息
     */
    void registerMaster(String connectionId, String masterClientId, Channel channel)throws RemoterException;

    /**
     * 从在线通道分组管理器中注销实例
     * @param channel 通道
     * @throws RemoterException
     */
    void unRegister(Channel channel)throws RemoterException;

    /**
     * 从在线通道分组管理器中注销实例
     * @param connectionId 连接编码
     * @throws RemoterException
     */
    void unRegister(String connectionId)throws RemoterException;

    /**
     * 通知消息给所有主控端
     * @param slaveClientId 受控端身份识别码，可作为通道组标识
     * @param baseResponse 消息主体
     * @throws RemoterException 异常信息
     */
    void notifyAllMaster(String slaveClientId, BaseResponse baseResponse)throws RemoterException;

    /**
     * 通知消息给受控端
     * @param connectionId 连接编码
     * @param baseResponse 消息主体
     * @throws RemoterException 异常信息
     */
    void notifySlave(String connectionId, BaseResponse baseResponse)throws RemoterException;
}
