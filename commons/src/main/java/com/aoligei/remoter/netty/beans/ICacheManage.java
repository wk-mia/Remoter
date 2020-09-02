package com.aoligei.remoter.netty.beans;

import com.aoligei.remoter.exception.NettyServerException;

/**
 * @author wk-mia
 * 2020-9-2
 * 在线通道管理接口
 */
public interface IChannelManage {

    /**
     * 向在线通道分组管理器注册受控端的实例
     * @param slaveClientChannelCache 受控端实例
     * @throws NettyServerException 异常信息
     */
    void registerSlave(ClientChannelCache slaveClientChannelCache)throws NettyServerException;

    /**
     * 向在线通道分组管理器注册主控端的实例
     * @param masterClientChannelCache 主控端实例
     * @throws NettyServerException 异常信息
     */
    void registerMasters(ClientChannelCache masterClientChannelCache)throws NettyServerException;

    /**
     * 从在线通道分组管理器中注销受控端的实例
     * @param slaveClientId 受控端的身份识别码
     * @throws NettyServerException 异常信息
     */
    void unRegisterSlave(String slaveClientId)throws NettyServerException;

    /**
     * 从在线通道分组管理器中注销主控端的实例
     * @param masterClientId 主控端的身份识别码
     * @throws NettyServerException 异常信息
     */
    void unRegisterMaster(String masterClientId)throws NettyServerException;

    /**
     * 通知消息给所有主控端
     * @param baseResponse 消息主体
     * @throws NettyServerException 异常信息
     */
    void notifyAllMaster(BaseResponse baseResponse)throws NettyServerException;
}
