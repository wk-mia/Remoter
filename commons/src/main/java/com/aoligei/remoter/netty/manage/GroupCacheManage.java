package com.aoligei.remoter.netty.manage;

import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.constant.IncompleteParamConstants;
import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IncompleteParamException;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.beans.ChannelCache;
import com.aoligei.remoter.netty.beans.MetaCache;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author wk-mia
 * 2020-9-2
 * 在线通道分组管理器
 */
@Component
public class GroupCacheManage implements ICacheManage {

    /**
     * 所有的在线连接组
     */
    public List<ChannelCache> caches = new CopyOnWriteArrayList<>();

    @Override
    public void registerSlave(String connectionId,String slaveClientId, Channel channel, ScheduledFuture scheduledFuture) throws ServerException {
        /**
         * 如果连接编码在缓存中找到，则根据受控端元数据是否为空决定是否需要更新缓存；
         * 如果连接编码在缓存中未找到，新增缓存；如果客户端身份识别码为空，抛出提示。
         */
        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        ChannelCache cache = this.getChannelCacheByConnectionId(connectionId);
        if(cache == null){
            /**
             * 新增缓存
             */
            MetaCache slaveMeta = BuildUtil.buildMetaCache(slaveClientId, channel, scheduledFuture, TerminalTypeEnum.SLAVE);
            caches.add(new ChannelCache(connectionId,slaveMeta,null));
        }else {
            /**
             * 更新缓存
             */
            if(cache.getSlaveMeta() == null){
                MetaCache slaveMeta = BuildUtil.buildMetaCache(slaveClientId, channel, scheduledFuture, TerminalTypeEnum.SLAVE);
                cache.setSlaveMeta(slaveMeta);
            }else {
                throw new ServerException(ServerExceptionConstants.SLAVE_BEING_CONTROLLED);
            }
        }
    }

    @Override
    public void registerMaster(String connectionId, String masterClientId, Channel channel, ScheduledFuture scheduledFuture) throws ServerException {
        /**
         * 如果连接编码在缓存中找到，则根据主控端元数据是否为空决定是否需要更新缓存；
         * 如果连接编码在缓存中未找到，新增缓存；如果客户端身份识别码为空，抛出提示。
         */
        if(masterClientId == null || "".equals(masterClientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        ChannelCache cache = this.getChannelCacheByConnectionId(connectionId);
        if(cache == null){
            /**
             * 新增缓存
             */
            MetaCache masterMeta = BuildUtil.buildMetaCache(masterClientId, channel, scheduledFuture, TerminalTypeEnum.MASTER);
            caches.add(new ChannelCache(connectionId,null,masterMeta));
        }else {
            /**
             * 更新缓存
             */
            if(cache.getMasterMeta() == null){
                MetaCache masterMeta = BuildUtil.buildMetaCache(masterClientId, channel, scheduledFuture, TerminalTypeEnum.MASTER);
                cache.setMasterMeta(masterMeta);
            }else {
                throw new ServerException(ServerExceptionConstants.MASTER_ALREADY_CONNECTED);
            }
        }
    }

    @Override
    public void unRegisterSlave(String slaveClientId) throws ServerException {
        // TODO: 2020/9/3  清除所有Slave的连接
    }

    @Override
    public void unRegisterMaster(String slaveClientId,String masterClientId) throws ServerException {
        // TODO: 2020/9/3  清除当前Master连接
    }

    @Override
    public void notifyAllMaster(String slaveClientId, BaseResponse baseResponse) throws IncompleteParamException {
        /**
         * 找到所有的主控端，将消息转送出去
         */
        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        List<ChannelCache> masters = this.getChannelCachesBySlaveClientId(slaveClientId);
        if(masters != null && masters.size() >= 1){
            masters.forEach(item ->
                    item.getMasterMeta().getChannel().writeAndFlush(baseResponse)
            );
        }
    }

    @Override
    public void notifySlave(String connectionId,BaseResponse baseResponse)throws ServerException {
        /**
         * 找到所有受控端，将消息转送出去
         */
        ChannelCache slaveCache = caches.stream().filter(item ->
                connectionId.equals(item.getConnectionId())).findFirst().get();
        if(slaveCache.getSlaveMeta() != null){
            if(slaveCache.getSlaveMeta().getChannel() != null && slaveCache.getSlaveMeta().getChannel().isOpen()){
                slaveCache.getSlaveMeta().getChannel().writeAndFlush(baseResponse);
            }
        }else {
            throw new ServerException(ServerExceptionConstants.SLAVE_NOT_FIND);
        }
    }

    /**
     * 通过连接编码在缓存中获取通道组
     * @param connectionId 连接编码
     * @return 通道组
     * @throws ServerException
     */
    public ChannelCache getChannelCacheByConnectionId(String connectionId){
        return caches.stream().filter(item ->
                connectionId.equals(item.getConnectionId())).findFirst().get();
    }


    /**
     * 获取当前连接组中所有的主控端
     * 只能通过受控端的身份标识，原因是单个Slave<——>多个Master时，每一个连接都有一个
     * connectionId，而能表示这一组连接的只有Slave的身份编码。
     * @param slaveClientId 受控端身份识别码
     * @return 数量
     * @throws ServerException 异常信息
     */
    public List<ChannelCache> getChannelCachesBySlaveClientId(String slaveClientId){
        return caches.stream().filter(item ->
                slaveClientId.equals(item.getSlaveMeta().getClientId())).collect(Collectors.toList());
    }

    /**
     * 获取当前连接组中所有的受控端
     * @param masterClientId 主控端身份识别码
     * @return 数量
     */
    public List<ChannelCache> getChannelCachesByMasterClientId(String masterClientId) {
        return caches.stream().filter(item ->
                masterClientId.equals(item.getMasterMeta().getClientId())).collect(Collectors.toList());
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
