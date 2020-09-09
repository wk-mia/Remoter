package com.aoligei.remoter.beans;

import com.aoligei.remoter.enums.CommandEnum;

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
     * 连接的唯一编码，用于标识一个Master和Slave的连接
     */
    private String connectionId;

    /**
     * 命令类型
     */
    private Enum<CommandEnum> commandEnum;

    /**
     * 数据主体
     */
    private T data;

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
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
        return "BaseDataBody{" +
                "connectionId='" + connectionId + '\'' +
                ", commandEnum=" + commandEnum +
                ", data=" + data +
                '}';
    }
}
