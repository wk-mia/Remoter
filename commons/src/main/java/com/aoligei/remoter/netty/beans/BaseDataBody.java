package com.aoligei.remoter.netty.beans;

import java.io.Serializable;
import java.util.List;

/**
 * @author wk-mia
 * 2020-9-1
 * 用于在Netty之间进行数据传输的实体对象
 */
public class BaseDataBody<T> implements Serializable {

    /**
     * 序列化常量
     */
    private static final long serialVersionUID = -3911255650485738676L;


    /**
     * 客户端身份识别码
     */
    private String clientId;

    /**
     * 消息接收者身份识别码列表
     */
    private List<String> targetClientIds;

    /**
     * 命令类型
     */
    private Enum<CommandEnum> commandEnum;

    /**
     * 数据主体
     */
    private T data;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Enum<CommandEnum> getCommandEnum() {
        return commandEnum;
    }

    public void setCommandEnum(Enum<CommandEnum> commandEnum) {
        this.commandEnum = commandEnum;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getTargetClientIds() {
        return targetClientIds;
    }

    public void setTargetClientIds(List<String> targetClientIds) {
        this.targetClientIds = targetClientIds;
    }

    @Override
    public String toString() {
        return "BaseDataBody{" +
                "clientId='" + clientId + '\'' +
                ", targetClientIds=" + targetClientIds +
                ", commandEnum=" + commandEnum +
                ", data=" + data +
                '}';
    }
}
