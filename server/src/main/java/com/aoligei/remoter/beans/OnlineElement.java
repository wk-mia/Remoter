package com.aoligei.remoter.beans;

import com.aoligei.remoter.enums.TerminalTypeEnum;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author wk-mia
 * 2020-9-1
 * 连接元数据，用于表示单个连接。
 *
 * 里面包含了Channel和ScheduledFuture两个对象，Channel的作用为标识一个连接，
 * 可以理解为一个Channel对象就代表了一个连接。
 * 当然，你可以用其他任何可以替代的方案来维护此连接，只要能保证超时后这个连接能
 * 感知到状态的变化。
 */
public class OnlineElement {

    /**
     * 客户端身份识别码
     */
    private String clientId;
    /**
     * 连接通道
     */
    private Channel channel;
    /**
     * 终端类型
     */
    private TerminalTypeEnum terminalTypeEnum;


    public OnlineElement(String clientId, Channel channel, TerminalTypeEnum terminalTypeEnum) {
        this.clientId = clientId;
        this.channel = channel;
        this.terminalTypeEnum = terminalTypeEnum;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }


    public TerminalTypeEnum getTerminalTypeEnum() {
        return terminalTypeEnum;
    }

    public void setTerminalTypeEnum(TerminalTypeEnum terminalTypeEnum) {
        this.terminalTypeEnum = terminalTypeEnum;
    }
}
