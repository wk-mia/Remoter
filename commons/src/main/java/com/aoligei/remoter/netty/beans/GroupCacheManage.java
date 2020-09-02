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
         * 如果客户端身份识别码在缓存连接组的Key中能找到，表示客户端当前正
         * 在远程工作中，抛出异常消息；如果找到了缓存，但是没有受控端通道，
         * 抛出异常信息；当且仅当查找不到缓存时，才将该通道加入管理组中。
         */
        if(cache.get(slaveClientId) == null){
            MetaCache metaCache = BuildUtil.buildMetaCache(slaveClientId, channel, scheduledFuture, MetaCache.ClientType.SLAVE);
            cache.put(slaveClientId,new ChannelCache(metaCache,null));
        }else {
            if(cache.get(slaveClientId).getSlaveChannel() == null){
                /**
                 * 工作异常，缓存存在但没有通道
                 */
                throw new NettyServerException(ExceptionMessageConstants.CLIENT_WORK_ERROR);
            }else {
                /**
                 * 正常工作中，此时不允许被控制
                 */
                throw new NettyServerException(ExceptionMessageConstants.CLIENT_BEING_CONTROLLED);
            }
        }
    }

    @Override
    public void registerMasters(String slaveClientId, String masterClientId, Channel channel, ScheduledFuture scheduledFuture) throws NettyServerException {
        /**
         * 如果受控客户端在缓存中能找到，表示受控客户端当前正处于远程工作中；
         * 如果受控客户端能找到，主控客户端找不到，表示主控客户端还未加入到
         * 通道组中；如果受控客户端和主控客户端都能找到，表示当前请求已有通道。
         * 如果受控客户端没有找到缓存，则表示受控客户端还没有加入到通道组中，
         * 抛出异常。
         */
        if(cache.get(slaveClientId) == null){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_NOT_FIND);
        }else {
            if(cache.get(slaveClientId).getMasterChannels() != null
                    && cache.get(slaveClientId).getMasterChannels().stream().filter(item -> masterClientId.equals(item.getClientId())).findAny().isPresent()){
                throw new NettyServerException(ExceptionMessageConstants.CLIENT_BEING_CONTROLLED);
            }
            MetaCache metaCache = BuildUtil.buildMetaCache(slaveClientId, channel, scheduledFuture,MetaCache.ClientType.MASTER);
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
