package com.aoligei.remoter.dto;

import com.aoligei.remoter.enums.TerminalTypeEnum;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author wk-mia
 * 2020-8-31
 * 客户端信息类
 */

public class ClientInformation {
    /**
     * 客户端类型
     */
    private int clientSystemType;
    /**
     * 客户端名称
     */
    private String clientName;
    /**
     * 客户端身份识别码
     */
    private String clientId;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 客户端远程端口
     */
    private Integer clientPort;
    /**
     * 此客户端是否已拒绝所有连接请求
     */
    private Boolean isRejectAllConnections;
    /**
     * 连接通道
     */
    private Channel channel;
    /**
     * 监听任务
     */
    private ScheduledFuture scheduledFuture;
    /**
     * 终端类型
     */
    private TerminalTypeEnum terminalTypeEnum;

    public int getClientSystemType() {
        return clientSystemType;
    }

    public void setClientSystemType(int clientSystemType) {
        this.clientSystemType = clientSystemType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public Boolean getRejectAllConnections() {
        return isRejectAllConnections;
    }

    public void setRejectAllConnections(Boolean rejectAllConnections) {
        isRejectAllConnections = rejectAllConnections;
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

    public TerminalTypeEnum getTerminalTypeEnum() {
        return terminalTypeEnum;
    }

    public void setTerminalTypeEnum(TerminalTypeEnum terminalTypeEnum) {
        this.terminalTypeEnum = terminalTypeEnum;
    }

    public ClientInformation(int clientSystemType, String clientName, String clientId, String clientIp, Integer clientPort, Boolean isRejectAllConnections, Channel channel, ScheduledFuture scheduledFuture, TerminalTypeEnum terminalTypeEnum) {
        this.clientSystemType = clientSystemType;
        this.clientName = clientName;
        this.clientId = clientId;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.isRejectAllConnections = isRejectAllConnections;
        this.channel = channel;
        this.scheduledFuture = scheduledFuture;
        this.terminalTypeEnum = terminalTypeEnum;
    }

    @Override
    public String toString() {
        return "ClientIdentifier{" +
                "clientName='" + clientName + '\'' +
                ", clientId='" + clientId + '\'' +
                ", ip='" + clientIp + '\'' +
                ", port=" + clientPort +
                ", isRejectAllConnections=" + isRejectAllConnections +
                '}';
    }
}
