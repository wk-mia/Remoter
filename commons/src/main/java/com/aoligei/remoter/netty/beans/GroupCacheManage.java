package com.aoligei.remoter.netty.beans;

import com.aoligei.remoter.exception.NettyServerException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wk-mia
 * 2020-9-2
 * 在线通道分组管理器
 */
@Component
public class GroupChannelManage implements IChannelManage {

    /**
     * 当前处于连接中的实例集合
     * Key：
     */
    private Map<Integer,ClientChannelCache> cache = new ConcurrentHashMap<>();

    /**
     * 受控端注册
     * 受控端只能有一个
     * @param slaveClientChannelCache 受控端实例
     * @throws NettyServerException
     */
    @Override
    public void registerSlave(ClientChannelCache slaveClientChannelCache) throws NettyServerException {
        if(this.slaveClientChannelCache == null || this.slaveClientChannelCache.getChannelHandlerContext() == null){
            this.slaveClientChannelCache = slaveClientChannelCache;
        }else {

        }
    }

    @Override
    public void registerMasters(ClientChannelCache masterClientChannelCache) throws NettyServerException {
        this.masterClientChannelCaches.add(masterClientChannelCache);
    }

    @Override
    public void unRegisterSlave(String slaveClientId) throws NettyServerException {

    }

    @Override
    public void unRegisterMaster(String masterClientId) throws NettyServerException {

    }

    @Override
    public void notifyAllMaster(BaseResponse baseResponse) throws NettyServerException {

    }
}
