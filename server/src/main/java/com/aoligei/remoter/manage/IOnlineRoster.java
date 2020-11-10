package com.aoligei.remoter.manage;

import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.RemoterException;
import io.netty.channel.Channel;

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
     * @throws RemoterException
     */
    void add(String clientId, Channel channel, TerminalTypeEnum terminalTypeEnum)throws RemoterException;

    /**
     * 从在线列表中销毁连接
     * @param clientId 客户端身份识别码
     * @throws RemoterException
     */
    void remove(String clientId)throws RemoterException;
}
