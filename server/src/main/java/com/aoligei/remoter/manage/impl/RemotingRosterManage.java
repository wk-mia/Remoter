package com.aoligei.remoter.manage.impl;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.beans.RemotingElement;
import com.aoligei.remoter.beans.OnlineElement;
import com.aoligei.remoter.constant.IncompleteParamConstants;
import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IncompleteParamException;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.manage.IRemotingRoster;
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
public class RemotingRosterManage implements IRemotingRoster {

    /**
     * 所有的在线连接组
     */
    public List<RemotingElement> remotingRoster = new CopyOnWriteArrayList<>();

    @Override
    public void registerSlave(String connectionId,String slaveClientId, Channel channel) throws ServerException {
        /**
         * 如果连接编码在缓存中找到，则根据受控端元数据是否为空决定是否需要更新缓存；
         * 如果连接编码在缓存中未找到，新增缓存；如果客户端身份识别码为空，抛出提示。
         */
        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        RemotingElement cache = this.getChannelCacheByConnectionId(connectionId);
        if(cache == null){
            /**
             * 新增缓存
             */
            OnlineElement slaveMeta = BuildUtil.buildMetaCache(slaveClientId, channel, TerminalTypeEnum.SLAVE);
            remotingRoster.add(new RemotingElement(connectionId,slaveMeta,null));
        }else {
            /**
             * 更新缓存
             */
            if(cache.getSlaveElement() == null){
                OnlineElement slaveMeta = BuildUtil.buildMetaCache(slaveClientId, channel, TerminalTypeEnum.SLAVE);
                cache.setSlaveElement(slaveMeta);
            }else {
                throw new ServerException(ServerExceptionConstants.SLAVE_BEING_CONTROLLED);
            }
        }
    }

    @Override
    public void registerMaster(String connectionId, String masterClientId, Channel channel) throws ServerException {
        /**
         * 如果连接编码在缓存中找到，则根据主控端元数据是否为空决定是否需要更新缓存；
         * 如果连接编码在缓存中未找到，新增缓存；如果客户端身份识别码为空，抛出提示。
         */
        if(masterClientId == null || "".equals(masterClientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        RemotingElement cache = this.getChannelCacheByConnectionId(connectionId);
        if(cache == null){
            /**
             * 新增缓存
             */
            OnlineElement masterMeta = BuildUtil.buildMetaCache(masterClientId, channel, TerminalTypeEnum.MASTER);
            remotingRoster.add(new RemotingElement(connectionId,null,masterMeta));
        }else {
            /**
             * 更新缓存
             */
            if(cache.getMasterElement() == null){
                OnlineElement masterMeta = BuildUtil.buildMetaCache(masterClientId, channel, TerminalTypeEnum.MASTER);
                cache.setMasterElement(masterMeta);
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
        List<RemotingElement> masters = this.getChannelCachesBySlaveClientId(slaveClientId);
        if(masters != null && masters.size() >= 1){
            masters.forEach(item ->
                    item.getMasterElement().getChannel().writeAndFlush(baseResponse)
            );
        }
    }

    @Override
    public void notifySlave(String connectionId,BaseResponse baseResponse)throws ServerException {
        /**
         * 找到所有受控端，将消息转送出去
         */
        RemotingElement slaveCache = remotingRoster.stream().filter(item ->
                connectionId.equals(item.getConnectionId())).findFirst().get();
        if(slaveCache.getSlaveElement() != null){
            if(slaveCache.getSlaveElement().getChannel() != null && slaveCache.getSlaveElement().getChannel().isOpen()){
                slaveCache.getSlaveElement().getChannel().writeAndFlush(baseResponse);
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
    public RemotingElement getChannelCacheByConnectionId(String connectionId){
        return remotingRoster.stream().filter(item ->
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
    public List<RemotingElement> getChannelCachesBySlaveClientId(String slaveClientId){
        return remotingRoster.stream().filter(item ->
                slaveClientId.equals(item.getSlaveElement().getClientId())).collect(Collectors.toList());
    }

    /**
     * 获取当前连接组中所有的受控端
     * @param masterClientId 主控端身份识别码
     * @return 数量
     */
    public List<RemotingElement> getChannelCachesByMasterClientId(String masterClientId) {
        return remotingRoster.stream().filter(item ->
                masterClientId.equals(item.getMasterElement().getClientId())).collect(Collectors.toList());
    }

    /**
     * 受控端是否正在远程工作中
     * @param slaverClientId 受控端身份识别码
     * @return 工作中:true
     */
    public boolean isSlaverWorking(String slaverClientId){
        return remotingRoster.stream().filter(item ->
                slaverClientId.equals(item.getSlaveElement().getClientId())).findAny().isPresent();
    }
}
