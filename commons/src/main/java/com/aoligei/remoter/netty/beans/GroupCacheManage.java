package com.aoligei.remoter.netty.beans;

import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author wk-mia
 * 2020-9-2
 * 在线通道分组管理器
 */
@Component
public class GroupCacheManage implements ICacheManage {

    /**
     * 所有的在线连接组
     * Key：受控端的clientId
     * Value：主控端的通道组
     */
    public Map<String,ChannelCache> cache = new ConcurrentHashMap<>();

    @Override
    public void registerSlave(String slaveClientId, Channel channel, ScheduledFuture scheduledFuture) throws NettyServerException {
        /**
         * 如果受控端身份识别码在缓存连接组的Key中能找到，表示受控端当前正
         * 在被控制中，抛出异常消息；当且仅当查找不到缓存时，才将该通道加入管理组中。
         */
        if(this.isSlaveExistInCache(slaveClientId)){
            throw new NettyServerException(ExceptionMessageConstants.SLAVE_BEING_CONTROLLED);
        }else {
            MetaCache metaCache = BuildUtil.buildMetaCache(slaveClientId, channel, scheduledFuture, MetaCache.ClientType.SLAVE);
            cache.put(slaveClientId,new ChannelCache(metaCache,null));
        }
    }

    @Override
    public void registerMasters(String slaveClientId, String masterClientId, Channel channel, ScheduledFuture scheduledFuture) throws NettyServerException {
        /**
         * 如果受控客户端没有找到缓存，则表示受控客户端还没有加入到通道组中；
         * 抛出异常；
         * 如果受控客户端能找到，主控客户端找不到，表示主控客户端还未加入到
         * 通道组中；
         * 如果受控客户端和主控客户端都能找到，表示发生了重复的连接请求。
         */
        if(this.isMasterExistInCurrentMasterChannels(slaveClientId,masterClientId)){
            throw new NettyServerException(ExceptionMessageConstants.MASTER_ALREADY_CONNECTED);
        }else {
            MetaCache metaCache = BuildUtil.buildMetaCache(masterClientId, channel, scheduledFuture,MetaCache.ClientType.MASTER);
            cache.get(slaveClientId).getMasterChannels().add(metaCache);
        }
    }

    @Override
    public void unRegisterSlave(String slaveClientId) throws NettyServerException {
        /**
         * 如果受控端身份识别码在缓存连接组的Key中能找到，表示受控端当前正
         * 在被控制中，停止工作并移除整个当前工作组；
         * 如果查找不到缓存，抛出异常信息。
         */
        if(this.isSlaveExistInCache(slaveClientId)){
            /**
             * 清除整个工作组
             */
            // TODO: 2020/9/3  清除整个工作组
        }else {
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_NOT_WORK);
        }
    }

    @Override
    public void unRegisterMaster(String slaveClientId,String masterClientId) throws NettyServerException {
        // TODO: 2020/9/3  清除Master连接
    }

    @Override
    public void notifyAllMaster(String slaveClientId,BaseResponse baseResponse) throws NettyServerException {
        /**
         * 找到所有的主控端，将消息转送出去
         */
        if(currentMastersCount(slaveClientId) >= 1){
            cache.get(slaveClientId).getMasterChannels().forEach(item ->
                    item.getChannel().writeAndFlush(baseResponse));
        }
    }

    @Override
    public void notifySlave(List<String> targetClientIds,BaseResponse baseResponse)throws NettyServerException{
        /**
         * 找到所有受控端，将消息转送出去
         */
        if(targetClientIds == null || targetClientIds.size() == 0){
            throw new NettyServerException(ExceptionMessageConstants.TARGET_CLIENTS_EMPTY);
        }else if(targetClientIds.size() > 1){
            throw new NettyServerException(ExceptionMessageConstants.NOT_SUPPORT_MASTER_CONTROL_MULTIPLE_SLAVE);
        }else {
            targetClientIds.forEach(item -> {
                if(isSlaveExistInCache(item)){
                    cache.get(item).getSlaveChannel().getChannel().writeAndFlush(baseResponse);
                }
            });
        }
    }

    /**
     * 判断一个受控端的身份识别码是否在通道组的受控端列表中
     * @param slaveClientId 受控端的身份识别码
     * @return true/false
     * @throws NettyServerException
     */
    private boolean isSlaveExistInCache(String slaveClientId){
        if(cache.get(slaveClientId) == null){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 判断一个主控端的身份识别码是否在当前通道组的主控端列表中
     * @param slaveClientId 主控端的身份识别码
     * @param masterClientId 受控端的身份识别码，用于定位通道组
     * @return true/false
     * @throws NettyServerException
     */
    private boolean isMasterExistInCurrentMasterChannels(String slaveClientId,String masterClientId) throws NettyServerException{
        if(cache.get(slaveClientId) == null){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_NOT_FIND);
        }else {
            if(cache.get(slaveClientId).getMasterChannels() == null){
                return true;
            }else {
                return !(cache.get(slaveClientId).getMasterChannels().stream().filter(item -> masterClientId.equals(item.getClientId())).findAny().isPresent());
            }
        }
    }

    /**
     * 获取当前连接组中主控端的个数
     * @param slaveClientId 受控端身份识别码
     * @return 数量
     * @throws NettyServerException 异常信息
     */
    public int currentMastersCount(String slaveClientId)throws NettyServerException{
        if(cache.get(slaveClientId) == null){
            throw new NettyServerException(ExceptionMessageConstants.SLAVE_NOT_FIND);
        }else {
            if(cache.get(slaveClientId).getMasterChannels() == null){
                return 0;
            }else {
                return cache.get(slaveClientId).getMasterChannels().size();
            }
        }
    }

    /**
     * 定义一个处理器的监听任务
     * @param channelHandlerContext 通道上下文
     * @param timeout 超时时间
     * @return
     */
    public ScheduledFuture getScheduled(ChannelHandlerContext channelHandlerContext,int timeout){
        return channelHandlerContext.executor().schedule(
                () -> channelHandlerContext.channel().close(),timeout, TimeUnit.SECONDS
        );
    }
}
