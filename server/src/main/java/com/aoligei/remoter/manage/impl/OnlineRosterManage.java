package com.aoligei.remoter.manage.impl;

import com.aoligei.remoter.beans.OnlineElement;
import com.aoligei.remoter.business.ResponseProcessor;
import com.aoligei.remoter.constant.IllegalRequestConstants;
import com.aoligei.remoter.constant.MissingParamConstants;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IllegalRequestException;
import com.aoligei.remoter.exception.MissingParamException;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.manage.IOnlineRoster;
import io.netty.channel.Channel;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wk-mia
 * 2020-9-7
 * 在线连接管理器
 * 用于管理所有的与服务器保持连接的实例，当有用户连接或断开服务器时，在管理器中维护该实例；
 * 在该管理器中可做在线用户数量控制，以及当大量连接请求存在时，对新增连接请求的限流等。
 */
@Component
public class OnlineRosterManage implements IOnlineRoster {

    private static Logger log = LoggerFactory.getLogger(OnlineRosterManage.class);

    /**
     * 客户端基本信息账册
     */
    @Autowired
    private RosterManage rosterManage;

    /**
     * 所有的在线连接
     */
    private CopyOnWriteArrayList<OnlineElement> onlineRoster = new CopyOnWriteArrayList<>();

    /**
     * 添加客户端的连接元数据
     * @param clientId 客户端身份识别码
     * @param channel 通道
     * @param terminalTypeEnum 终端类型
     * @throws RemoterException
     */
    @Override
    public void add(String clientId, Channel channel, TerminalTypeEnum terminalTypeEnum)throws RemoterException {
        /**
         * 通道异常
         */
        if(channel == null || !channel.isOpen()){
            throw new RemoterException(IllegalRequestConstants.CLIENT_WORK_ERROR);
        }
        /**
         * 当客户端身份识别码能在基本信息的账册中找到，添加进在线连接集合中
         */
        if(rosterManage.getRoster().stream().filter(item -> clientId.equals(item.getClientId())).findAny().isPresent()){
            OnlineElement onlineElement = ResponseProcessor.buildMetaCache(clientId, channel, terminalTypeEnum);
            onlineRoster.add(onlineElement);
        }else {
            throw new RemoterException(IllegalRequestConstants.CLIENT_NOT_REGISTER);
        }
    }

    /**
     * 从在线列表中销毁连接
     * @param clientId 客户端身份识别码
     * @throws RemoterException
     */
    @Override
    public void remove(String clientId)throws RemoterException {
        if(clientId == null || "".equals(clientId)){
            throw new MissingParamException(MissingParamConstants.CLIENT_ID_CANNOT_BE_EMPTY);
        }
        if (onlineRoster.stream().filter(item -> clientId.equals(item.getClientId())).findAny().isPresent()) {
            /**
             * 从onlineConn中移除所有clientId的元数据，尽管理论上一个客户端只有会存在一个连接的元数据
             */
            Iterator<OnlineElement> iterator = onlineRoster.iterator();
            while (iterator.hasNext()){
                OnlineElement next = iterator.next();
                if(clientId.equals(next.getClientId())){
                    onlineRoster.remove(next);
                    log.info(MessageFormat.format("the client: {0} has been offline",clientId));
                }
            }
        }else {
            throw new IllegalRequestException(IllegalRequestConstants.CLIENT_NOT_FIND);
        }

    }

    /**
     * 从在线列表中销毁连接
     * @param channel 通道
     */
    public void remove(Channel channel){
        if(channel == null){
            return;
        }
        if (onlineRoster.stream().filter(item -> channel == item.getChannel()).findAny().isPresent()){
            /**
             * 从onlineConn中移除所有通道为channel的元数据，尽管理论上一个客户端只有会存在一个连接的元数据
             */
            Iterator<OnlineElement> iterator = onlineRoster.iterator();
            while (iterator.hasNext()){
                OnlineElement next = iterator.next();
                if(channel == next.getChannel()){
                    onlineRoster.remove(next);
                    log.info(MessageFormat.format("the client: {0} has been offline",next.getClientId()));
                }
            }
        }
    }

    /**
     * 获取所有与服务器保持通信的在线连接
     * @return onlineConn
     */
    public CopyOnWriteArrayList<OnlineElement> getOnlineRoster(){
        return this.onlineRoster;
    }

    /**
     * 通过受控端身份识别码获取在线连接
     * @param slaveClientId 受控端身份识别码
     * @return 受控端连接
     * @throws RemoterException 异常信息
     */
    public OnlineElement getSlaveInfoBySlaveClientId(String slaveClientId)throws RemoterException {
        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new MissingParamException(MissingParamConstants.NO_SLAVER_SPECIFIED);
        }

        OnlineElement slaveMeta = onlineRoster.stream().filter(item -> slaveClientId.equals(item.getClientId())).findAny().get();
        if(slaveMeta == null){
            throw new IllegalRequestException(IllegalRequestConstants.SLAVE_NOT_FIND);
        }
        return slaveMeta;
    }

    /**
     * 客户端是否在线
     * @param clientId 客户端身份识别码
     * @return 在线:true
     */
    public boolean isOnline(String clientId){
        return onlineRoster.stream().filter(item ->
                clientId.equals(item.getClientId())).findAny().isPresent();
    }
}
