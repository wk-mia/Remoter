package com.aoligei.remoter.manage;

import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ServerException;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author wk-mia
 * 2020-9-9
 * 在线连接的客户端花名册
 */
public interface IOnlineRoster {

    /**
     * 添加客户端的连接元数据
     * @param clientId 客户端身份识别码
     * @param channel 通道
     * @param terminalTypeEnum 终端类型
     * @throws ServerException
     */
    void add(String clientId, Channel channel, TerminalTypeEnum terminalTypeEnum)throws ServerException;

    /**
     * 从在线列表中销毁连接
     * @param clientId 客户端身份识别码
     * @throws ServerException
     */
    void remove(String clientId)throws ServerException;
}
