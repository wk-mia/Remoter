package com.aoligei.remoter.netty.beans;

import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author wk-mia
 * 2020-9-1
 * 连接元数据，用于表示单个连接。
 * 里面包含了Channel和ScheduledFuture两个对象，Channel的作用为标识一个连接，
 * 可以理解为一个Channel对象就代表了一个连接；ScheduledFuture是Netty提供的Futrue
 * ，它主要用于监听当前Channel，当客户端超时未发送心跳包时，可自动关闭这个连接。
 * 当然，你可以用其他任何可以替代的方案来维护此连接，只要能保证超时后这个连接能
 * 感知到状态的变化。
 */
public class MetaCache {

    /**
     * 连接通道
     */
    private Channel channel;

    /**
     * 监听任务
     */
    private ScheduledFuture scheduledFuture;

    /**
     * 客户端身份识别码
     */
    private String clientId;

    /**
     * 终端类型
     */
    private ClientType clientType;

    /**
     * 终端类型
     * M：主控端
     * S：被控端
     */
    public static enum ClientType{
        MASTER,
        SLAVE;
        private ClientType(){};
    }

    public MetaCache(Channel channel, ScheduledFuture scheduledFuture, String clientId, ClientType clientType) {
        this.channel = channel;
        this.scheduledFuture = scheduledFuture;
        this.clientId = clientId;
        this.clientType = clientType;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
