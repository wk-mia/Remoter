package com.aoligei.remoter.netty.beans;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return "DataBody{" +
                "clientId='" + clientId + '\'' +
                ", commandEnum=" + commandEnum +
                ", data=" + data +
                '}';
    }
}
