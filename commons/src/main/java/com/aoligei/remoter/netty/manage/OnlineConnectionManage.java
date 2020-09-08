package com.aoligei.remoter.netty.manage;

import com.aoligei.remoter.constant.IncompleteParamConstants;
import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.dto.ClientInformation;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IncompleteParamException;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author wk-mia
 * 2020-9-7
 * 在线连接管理器
 * 用于管理所有的与服务器保持连接的实例，当有用户连接或断开服务器时，在管理器中维护该实例；
 * 在该管理器中可做在线用户数量控制，以及当大量连接请求存在时，对新增连接请求的限流等。
 */
@Component
public class OnlineConnectionManage {

    /**
     * 所有的在线连接
     */
    private CopyOnWriteArrayList<ClientInformation> onlineConn = new CopyOnWriteArrayList<>();

    /**
     * 注册客户端的连接元数据
     * @param clientId 客户端身份识别码
     * @param channel 通道
     * @param terminalTypeEnum 终端类型
     * @throws ServerException
     */
    public void register(String clientId, Channel channel, ScheduledFuture scheduledFuture, TerminalTypeEnum terminalTypeEnum)throws ServerException {
        if(clientId == null || "".equals(clientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        if(channel == null || !channel.isOpen()){
            throw new ServerException(ServerExceptionConstants.CLIENT_WORK_ERROR);
        }
        /**
         * 当客户端身份识别码能在账册中找到，添加进在线连接集合中
         */
        ClientInformation clientInformation = BuildUtil.buildClientInfo();
        onlineConn.add(clientInformation);
    }

    /**
     * 从在线列表中销毁连接
     * @param clientId 客户端身份识别码
     * @throws ServerException
     */
    public void destory(String clientId)throws ServerException {
        if(clientId == null || "".equals(clientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        if (onlineConn.stream().filter(item -> clientId.equals(item.getClientId())).findAny().isPresent()) {
            /**
             * 从onlineConn中移除所有clientId的元数据，尽管理论上一个客户端只有会存在一个连接的元数据
             */
            Iterator<ClientInformation> iterator = onlineConn.iterator();
            while (iterator.hasNext()){
                if(clientId.equals(iterator.next().getClientId())){
                    iterator.remove();
                }
            }
        }else {
            throw new ServerException(ServerExceptionConstants.CLIENT_NOT_FIND);
        }

    }

    /**
     * 获取所有与服务器保持通信的在线连接
     * @return onlineConn
     */
    public CopyOnWriteArrayList<ClientInformation> getOnlineConn(){
        return this.onlineConn;
    }

    /**
     * 通过主控端身份识别码获取在线连接
     * @param slaveClientId 主控端身份识别码
     * @return 主控端连接
     * @throws ServerException 异常信息
     */
    public ClientInformation getSlaveInfoBySlaveClientId(String slaveClientId)throws ServerException {
        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new IncompleteParamException(IncompleteParamConstants.NO_SLAVER_SPECIFIED);
        }
        ClientInformation slaveInfo = onlineConn.stream().filter(item -> slaveClientId.equals(item.getClientId())).findAny().get();
        if(slaveInfo == null){
            throw new ServerException(ServerExceptionConstants.SLAVE_NOT_FIND);
        }
        return slaveInfo;
    }


    /**
     * 定义一个处理器的监听任务
     * @param channelHandlerContext 通道上下文
     * @param timeout 超时时间
     * @return
     */
    public ScheduledFuture getScheduled(ChannelHandlerContext channelHandlerContext, int timeout){
        return channelHandlerContext.executor().schedule(
                () -> channelHandlerContext.channel().close(),timeout, TimeUnit.SECONDS
        );
    }
}
