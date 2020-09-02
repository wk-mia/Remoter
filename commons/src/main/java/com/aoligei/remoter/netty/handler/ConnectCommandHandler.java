package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.beans.GroupCacheManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wk-mia
 * 2020-9-2
 * 点对点模式服务端-连接处理器
 * 负责初始话连接信息并维护所有连接信息。
 */
public class ConnectCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 当前的所有连接
     */
    @Autowired
    private GroupCacheManage groupChannelManage;

    /**
     * 特定的处理器：连接处理器
     * 初始话连接并维护这个连接信息
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws NettyServerException 异常信息
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
        /**
         * 如果当前请求为Master(主控端)的连接请求，则把这个连接加入到连接组并向Slave(受控端)发送连接请求；
         * 如果当前连接对象为Slave(受控端)，则检查当前连接组中是否已有受控对象，如果有，拒绝加入连接组并返回异常信息，如果没有，则加入连接组；
         * 如果无法判定当前连接请求是主控端或者受控端，拒绝加入连接组并返回异常信息
         */
        if(true){

        }else if(true){
            BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getClientId(), baseRequest.getCommandEnum(), "", null);
            channelHandlerContext.writeAndFlush(baseResponse);
        }else {

        }
    }
}
