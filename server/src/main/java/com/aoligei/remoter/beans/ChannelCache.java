package com.aoligei.remoter.beans;


/**
 * @author wk-mia
 * 2020-9-2
 * 连接通道，用于维护单组连接。
 * 里面包含了slaveChannel和masterChannels两个对象。slaveChannel表示受控端，任何一个连接场景中
 * 只允许存在一个受控端；masterChannels表示主控端，主控端可以有多个，即是说，一个受控端可同时被
 * 多个主控端访问。
 */
public class ChannelCache {

    /**
     * 连接的唯一编码，用于标识一个Master和Slave的连接
     */
    private String connectionId;
    /**
     * 受控端元数据
     */
    private MetaCache slaveMeta;
    /**
     * 主控端元数据
     */
    private MetaCache masterMeta;


    public ChannelCache(String connectionId, MetaCache slaveMeta, MetaCache masterMeta) {
        this.connectionId = connectionId;
        this.slaveMeta = slaveMeta;
        this.masterMeta = masterMeta;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    public MetaCache getSlaveMeta() {
        return slaveMeta;
    }

    public void setSlaveMeta(MetaCache slaveMeta) {
        this.slaveMeta = slaveMeta;
    }

    public MetaCache getMasterMeta() {
        return masterMeta;
    }

    public void setMasterMeta(MetaCache masterMeta) {
        this.masterMeta = masterMeta;
    }
}
