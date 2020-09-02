package com.aoligei.remoter.netty.beans;

import java.util.List;

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
     * 受控端元数据
     */
    private MetaCache slaveChannel;

    /**
     * 主控端元数据
     */
    private List<MetaCache> masterChannels;

    public ChannelCache(MetaCache slaveChannel, List<MetaCache> masterChannels) {
        this.slaveChannel = slaveChannel;
        this.masterChannels = masterChannels;
    }

    public MetaCache getSlaveChannel() {
        return slaveChannel;
    }

    public void setSlaveChannel(MetaCache slaveChannel) {
        this.slaveChannel = slaveChannel;
    }

    public List<MetaCache> getMasterChannels() {
        return masterChannels;
    }

    public void setMasterChannels(List<MetaCache> masterChannels) {
        this.masterChannels = masterChannels;
    }
}
