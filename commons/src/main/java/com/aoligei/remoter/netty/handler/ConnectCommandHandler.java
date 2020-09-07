package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.constant.ResponseConstants;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.aop.RequestInspect;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.manage.GroupCacheManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.Channel;
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
     * 初始化连接并维护这个连接信息
     * 检查项 = {REQUEST_IS_ILLEGAL,MASTER_TO_SLAVES,SLAVE_TO_MASTERS}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws NettyServerException 异常信息
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.REQUEST_IS_ILLEGAL,InspectEnum.MASTER_TO_SLAVES,InspectEnum.SLAVE_TO_MASTERS})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
        /**
         * 目前暂不支持一个受控端被多个主控端控制的模式以及一个主控端同时远程
         * 多个受控终端。
         */
        if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.SLAVE){
            /**
             * 注册受控端，返回信息，输出日志
             */
            Channel channel = channelHandlerContext.channel();
            groupChannelManage.registerSlave(baseRequest.getConnectionId(),baseRequest.getClientId(),channel,
                    groupChannelManage.getScheduled(channelHandlerContext,3));

            BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getConnectionId(),
                    TerminalTypeEnum.SERVER,baseRequest.getCommandEnum(), ResponseConstants.SLAVE_CONNECT_SUCCEEDED, null);
            channelHandlerContext.writeAndFlush(baseResponse);

            logInfo(baseRequest,"Slave[" + baseRequest.getClientId() + "]连接服务器成功");
        }else if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.MASTER){
            /**
             * 注册主控端，返回信息，输出日志
             */
            Channel channel = channelHandlerContext.channel();
            groupChannelManage.registerMasters(baseRequest.getConnectionId(),baseRequest.getClientId()
                    ,channel, groupChannelManage.getScheduled(channelHandlerContext,3));

            BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getConnectionId(),
                    TerminalTypeEnum.SERVER,baseRequest.getCommandEnum(), ResponseConstants.MASTER_CONNECT_SUCCEEDED, null);
            channelHandlerContext.writeAndFlush(baseResponse);

            logInfo(baseRequest,"Master[" + baseRequest.getClientId() + "]连接服务器成功");
        }
    }
}
