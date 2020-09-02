package com.aoligei.remoter.netty.beans;

import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;
import org.springframework.stereotype.Component;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wk-mia
 * 2020-9-2
 * 在线通道分组管理器
 */
@Component
public class GroupCacheManage implements ICacheManage {

    /**
     * 所有的在线连接
     * Key：clientId
     * Value：通道组
     */
    private Map<String,ChannelCache> cache = new ConcurrentHashMap<>();

    @Override
    public void registerSlave(String slaveClientId, Channel channel, ScheduledFuture scheduledFuture) throws NettyServerException {
        /**
         * 如果客户端在缓存中能找到，表示客户端当前正
         * 在远程工作中；如果没有，则将该通道加入管理组中。
         */
        if(cache.get(slaveClientId) == null && cache.get(slaveClientId).getSlaveChannel() == null){
            MetaCache metaCache = BuildUtil.buildMetaCache(slaveClientId, channel, scheduledFuture);
            cache.put(slaveClientId,new ChannelCache(metaCache,null));
        }else {
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_BEING_CONTROLLED);
        }
    }

    @Override
    public void registerMasters(String slaveClientId, String masterClientId, Channel channel, ScheduledFuture scheduledFuture) throws NettyServerException {
        /**
         * 如果客户端在缓存中能找到，表示客户端当前正
         * 处于远程工作中；如果没有，则将该通道加入管理组中。
         */
        if(cache.get(slaveClientId) == null && cache.get(slaveClientId).getMasterChannels() == null){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_BEING_CONTROLLED);
        }else {
            MetaCache metaCache = BuildUtil.buildMetaCache(slaveClientId, channel, scheduledFuture);
            cache.put(slaveClientId,new ChannelCache(metaCache,null));
        }
    }

    @Override
    public void unRegisterSlave(String slaveClientId) throws NettyServerException {

    }

    @Override
    public void unRegisterMaster(String slaveClientId,String masterClientId) throws NettyServerException {

    }

    @Override
    public void notifyAllMaster(BaseResponse baseResponse) throws NettyServerException {

    }
}
