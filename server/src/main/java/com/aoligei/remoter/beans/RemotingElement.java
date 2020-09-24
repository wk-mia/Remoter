package com.aoligei.remoter.beans;


/**
 * @author wk-mia
 * 2020-9-2
 * 控制元数据，用于维护单组连接。
 * 里面包含了slaveChannel和masterChannels两个对象。slaveChannel表示受控端，任何一个连接场景中
 * 只允许存在一个受控端；masterChannels表示主控端，主控端可以有多个，即是说，一个受控端可同时被
 * 多个主控端访问。
 */
public class RemotingElement {

    /**
     * 连接的唯一编码，用于标识一个Master和Slave的连接
     */
    private String connectionId;
    /**
     * 受控端元数据
     */
    private OnlineElement slaveElement;
    /**
     * 主控端元数据
     */
    private OnlineElement masterElement;


    public RemotingElement(String connectionId, OnlineElement slaveElement, OnlineElement masterElement) {
        this.connectionId = connectionId;
        this.slaveElement = slaveElement;
        this.masterElement = masterElement;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public OnlineElement getSlaveElement() {
        return slaveElement;
    }

    public void setSlaveElement(OnlineElement slaveElement) {
        this.slaveElement = slaveElement;
    }

    public OnlineElement getMasterElement() {
        return masterElement;
    }

    public void setMasterElement(OnlineElement masterElement) {
        this.masterElement = masterElement;
    }
}
