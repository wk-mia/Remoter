package com.aoligei.remoter.dto;

import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-8-31
 * 客户端信息类
 */
@Component
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
