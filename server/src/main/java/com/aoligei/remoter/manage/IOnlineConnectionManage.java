package com.aoligei.remoter.manage;

import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ServerException;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author wk-mia
 * 2020-9-9
 * 保持客户端在线连接的管理接口
 */
public interface IOnlineConnectionManage {

    /**
     * 添加客户端的连接元数据
     * @param clientId 客户端身份识别码
     * @param channel 通道
     * @param scheduledFuture 监听任务
     * @param terminalTypeEnum 终端类型
     * @throws ServerException
     */
    void add(String clientId, Channel channel, ScheduledFuture scheduledFuture, TerminalTypeEnum terminalTypeEnum)throws ServerException;

    /**
     * 从在线列表中销毁连接
     * @param clientId 客户端身份识别码
     * @throws ServerException
     */
    void remove(String clientId)throws ServerException;
}
