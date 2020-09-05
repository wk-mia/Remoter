package com.aoligei.remoter.netty.beans;

import com.aoligei.remoter.exception.NettyServerException;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.List;

/**
 * @author wk-mia
 * 2020-9-2
 * 在线通道管理接口
 */
public interface ICacheManage {

    /**
     * 向在线通道分组管理器注册受控端的实例
     * @param slaveClientId 受控端的身份识别码
     * @param channel 通道
     * @param scheduledFuture 监听任务
     * @throws NettyServerException 异常信息
     */
    void registerSlave(String slaveClientId, Channel channel, ScheduledFuture scheduledFuture)throws NettyServerException;

    /**
     * 向在线通道分组管理器注册主控端的实例
     * @param slaveClientId 受控端的身份识别码
     * @param masterClientId 主控端的身份识别码
     * @param channel 通道
     * @param scheduledFuture 监听任务
     * @throws NettyServerException 异常信息
     */
    void registerMasters(String slaveClientId, String masterClientId, Channel channel,ScheduledFuture scheduledFuture)throws NettyServerException;

    /**
     * 从在线通道分组管理器中注销受控端的实例
     * @param slaveClientId 受控端的身份识别码
     * @throws NettyServerException 异常信息
     */
    void unRegisterSlave(String slaveClientId)throws NettyServerException;

    /**
     * 从在线通道分组管理器中注销主控端的实例
     * @param slaveClientId 受控端的身份识别码
     * @param masterClientId 主控端的身份识别码
     * @throws NettyServerException 异常信息
     */
    void unRegisterMaster(String slaveClientId,String masterClientId)throws NettyServerException;

    /**
     * 通知消息给所有主控端
     * @param slaveClientId 受控端身份识别码，可作为通道组标识
     * @param baseResponse 消息主体
     * @throws NettyServerException 异常信息
     */
    void notifyAllMaster(String slaveClientId,BaseResponse baseResponse)throws NettyServerException;

    /**
     * 通知消息给受控端
     * @param targetClientIds 受控端列表
     * @param baseResponse 消息主体
     * @throws NettyServerException 异常信息
     */
    void notifySlave(List<String> targetClientIds, BaseResponse baseResponse)throws NettyServerException;
}
