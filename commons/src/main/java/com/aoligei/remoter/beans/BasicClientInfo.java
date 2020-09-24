package com.aoligei.remoter.beans;


/**
 * @author wk-mia
 * 2020-9-9
 * 客户端基本信息元数据
 */
public class BasicClientInfo {

    /**
     * 客户端身份识别码
     */
    private String clientId;
    /**
     * 客户端名称
     */
    private String clientName;
    /**
     * 客户端类型，表示客户端的系统类型
     */
    private int clientSystemType;
    /**
     * 客户端IP
     */
    private String clientIp;
    /**
     * 此客户端是否已拒绝所有连接请求
     */
    private Boolean isRejectConnection;

    public BasicClientInfo(String clientId, String clientName, int clientSystemType, String clientIp, Boolean isRejectConnection) {
        this.clientId = clientId;
        this.clientName = clientName;
        this.clientSystemType = clientSystemType;
        this.clientIp = clientIp;
        this.isRejectConnection = isRejectConnection;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getClientSystemType() {
        return clientSystemType;
    }

    public void setClientSystemType(int clientSystemType) {
        this.clientSystemType = clientSystemType;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Boolean getRejectConnection() {
        return isRejectConnection;
    }

    public void setRejectConnection(Boolean rejectConnection) {
        isRejectConnection = rejectConnection;
    }

    @Override
    public String toString() {
        return "BasicClientInfo{" +
                "clientId='" + clientId + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientSystemType=" + clientSystemType +
                ", clientIp='" + clientIp + '\'' +
                ", isRejectConnection=" + isRejectConnection +
                '}';
    }
}
