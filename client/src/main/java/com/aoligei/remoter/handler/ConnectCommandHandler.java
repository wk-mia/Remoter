package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.enums.StatusEnum;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.exception.ServerException;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-13
 * 点对点模式客户端-连接处理器
 * 负责接受到服务器对于连接请求的处理结果。
 */
public class ConnectCommandHandler extends AbstractClientHandler {
    /**
     * 特定的处理器-连接处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        if(baseResponse.getStatus() == StatusEnum.OK){
            logInfo(baseResponse,baseResponse.getMessage());
        }else {
            logError(baseResponse,baseResponse.getMessage());
        }
    }
}