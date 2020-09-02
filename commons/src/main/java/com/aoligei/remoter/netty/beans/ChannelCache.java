package com.aoligei.remoter.netty.beans;

import java.util.List;

/**
 * @author wk-mia
 * 2020-9-2
 * 连接元数据，用于维护单个连接。
 * 里面包含了slaveChannel和masterChannels两个对象。
 */
public class ClientChannelCache {

    private MetaCache slaveChannel;

    private List<MetaCache> masterChannels
}
