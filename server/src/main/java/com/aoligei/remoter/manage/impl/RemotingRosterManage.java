package com.aoligei.remoter.manage.impl;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.beans.RemotingElement;
import com.aoligei.remoter.beans.OnlineElement;
import com.aoligei.remoter.business.ResponseProcessor;
import com.aoligei.remoter.constant.IllegalRequestConstants;
import com.aoligei.remoter.constant.MissingParamConstants;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IllegalRequestException;
import com.aoligei.remoter.exception.MissingParamException;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.manage.IRemotingRoster;
import io.netty.channel.Channel;
import java.text.MessageFormat;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import org.springframework.util.StringUtils;

/**
 * @author wk-mia
 * 2020-9-2
 * 在线通道分组管理器
 */
@Component
public class RemotingRosterManage implements IRemotingRoster {

    private static Logger log = LoggerFactory.getLogger(RemotingRosterManage.class);

    /**
     * 所有的在线连接组
     */
    public CopyOnWriteArrayList<RemotingElement> remotingRoster = new CopyOnWriteArrayList<>();

    @Override
    public void registerSlave(String connectionId,String slaveClientId, Channel channel) throws RemoterException {
        /**
         * 如果连接编码在缓存中找到，则根据受控端元数据是否为空决定是否需要更新缓存；
         * 如果连接编码在缓存中未找到，新增缓存；如果客户端身份识别码为空，抛出提示。
         */
        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new MissingParamException(MissingParamConstants.CLIENT_ID_CANNOT_BE_EMPTY);
        }
        RemotingElement cache = this.getChannelCacheByConnectionId(connectionId);
        if(cache == null){
            /**
             * 新增缓存
             */
            OnlineElement slaveMeta = ResponseProcessor.buildMetaCache(slaveClientId, channel, TerminalTypeEnum.SLAVE);
            remotingRoster.add(new RemotingElement(connectionId,slaveMeta,null));
        }else {
            /**
             * 更新缓存
             */
            if(cache.getSlaveElement() == null){
                OnlineElement slaveMeta = ResponseProcessor.buildMetaCache(slaveClientId, channel, TerminalTypeEnum.SLAVE);
                cache.setSlaveElement(slaveMeta);
            }else {
                throw new IllegalRequestException(IllegalRequestConstants.SLAVE_BEING_CONTROLLED);
            }
        }
    }

    @Override
    public void registerMaster(String connectionId, String masterClientId, Channel channel) throws RemoterException {
        /**
         * 如果连接编码在缓存中找到，则根据主控端元数据是否为空决定是否需要更新缓存；
         * 如果连接编码在缓存中未找到，新增缓存；如果客户端身份识别码为空，抛出提示。
         */
        if(masterClientId == null || "".equals(masterClientId)){
            throw new MissingParamException(MissingParamConstants.CLIENT_ID_CANNOT_BE_EMPTY);
        }
        RemotingElement cache = this.getChannelCacheByConnectionId(connectionId);
        if(cache == null){
            /**
             * 新增缓存
             */
            OnlineElement masterMeta = ResponseProcessor.buildMetaCache(masterClientId, channel, TerminalTypeEnum.MASTER);
            remotingRoster.add(new RemotingElement(connectionId,null,masterMeta));
        }else {
            /**
             * 更新缓存
             */
            if(cache.getMasterElement() == null){
                OnlineElement masterMeta = ResponseProcessor.buildMetaCache(masterClientId, channel, TerminalTypeEnum.MASTER);
                cache.setMasterElement(masterMeta);
            }else {
                throw new IllegalRequestException(IllegalRequestConstants.MASTER_ALREADY_CONNECTED);
            }
        }
    }

    /**
     * 从在线通道分组管理器中注销实例
     * @param channel 通道
     * @throws RemoterException
     */
    @Override
    public void unRegister(Channel channel)throws RemoterException{
        if(channel == null){
            return;
        }
        Iterator<RemotingElement> iterator = remotingRoster.iterator();
        while (iterator.hasNext()){
            RemotingElement next = iterator.next();
            if(channel == next.getMasterElement().getChannel() || channel == next.getSlaveElement().getChannel()){
                remotingRoster.remove(next);
                log.info(MessageFormat.format("the connection: {0} has been terminated",next.getConnectionId()));
            }
        }
    }

    /**
     * 从在线通道分组管理器中注销实例
     * @param connectionId 连接编码
     * @throws RemoterException
     */
    @Override
    public void unRegister(String connectionId)throws RemoterException{
        if(StringUtils.isEmpty(connectionId)){
            return;
        }
        Iterator<RemotingElement> iterator = remotingRoster.iterator();
        while (iterator.hasNext()){
            RemotingElement next = iterator.next();
            if(connectionId == next.getConnectionId()){
                remotingRoster.remove(next);
                log.info(MessageFormat.format("the connection: {0} has been terminated",next.getConnectionId()));
            }
        }
    }


    @Override
    public void notifyAllMaster(String connectionId, BaseResponse baseResponse) throws RemoterException {
        /**
         * 找到所有的主控端，将消息转送出去
         */
        if(connectionId == null || "".equals(connectionId)){
            throw new MissingParamException(MissingParamConstants.CONNECTION_ID_CANNOT_BE_EMPTY);
        }
        List<RemotingElement> masters = this.getChannelCachesByConnectionId(connectionId);
        if(masters != null && masters.size() >= 1){
            masters.forEach(item ->
                    item.getMasterElement().getChannel().writeAndFlush(baseResponse)
            );
        }
    }

    @Override
    public void notifySlave(String connectionId,BaseResponse baseResponse)throws RemoterException {
        /**
         * 找到所有受控端，将消息转送出去
         */
        RemotingElement slaveCache = remotingRoster.stream()
                .filter(item -> connectionId.equals(item.getConnectionId()))
                .findFirst().orElse(null);
        if(slaveCache != null && slaveCache.getSlaveElement() != null){
            if(slaveCache.getSlaveElement().getChannel() != null && slaveCache.getSlaveElement().getChannel().isOpen()){
                slaveCache.getSlaveElement().getChannel().writeAndFlush(baseResponse);
            }
        }else {
            throw new IllegalRequestException(IllegalRequestConstants.SLAVE_NOT_FIND);
        }
    }

    /**
     * 通过连接编码在缓存中获取通道组
     * @param connectionId 连接编码
     * @return 通道组
     */
    public RemotingElement getChannelCacheByConnectionId(String connectionId){
        return remotingRoster.stream()
                .filter(item -> connectionId.equals(item.getConnectionId()))
                .findFirst().orElse(null);
    }


    /**
     * 获取当前连接组中所有的主控端
     * 只能通过受控端的身份标识，原因是单个Slave<——>多个Master时，每一个连接都有一个
     * connectionId，而能表示这一组连接的只有Slave的身份编码。
     * @param slaveClientId 受控端身份识别码
     * @return 数量
     */
    public List<RemotingElement> getChannelCachesBySlaveClientId(String slaveClientId){
        return remotingRoster.stream()
                .filter(item -> item.getSlaveElement() != null)
                .filter(item -> slaveClientId.equals(item.getSlaveElement().getClientId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取当前连接组中所有的主控端
     * @param connectionId 连接编码
     * @return 数量
     */
    public List<RemotingElement> getChannelCachesByConnectionId(String connectionId){
        return remotingRoster.stream()
                .filter(item -> ! StringUtils.isEmpty(item.getConnectionId()))
                .filter(item -> connectionId.equals(item.getConnectionId()))
                .collect(Collectors.toList());
    }

    /**
     * 获取当前连接组中所有的受控端
     * @param masterClientId 主控端身份识别码
     * @return 数量
     */
    public List<RemotingElement> getChannelCachesByMasterClientId(String masterClientId) {
        return remotingRoster.stream()
                .filter(item -> item.getMasterElement() != null)
                .filter(item -> masterClientId.equals(item.getMasterElement().getClientId()))
                .collect(Collectors.toList());
    }

    /**
     * 受控端是否正在远程工作中
     * @param slaverClientId 受控端身份识别码
     * @return 工作中:true
     */
    public boolean isSlaverWorking(String slaverClientId){
        return remotingRoster.stream()
                .filter(item -> item.getSlaveElement() != null)
                .anyMatch(item -> slaverClientId.equals(item.getSlaveElement().getClientId()));
    }
}
