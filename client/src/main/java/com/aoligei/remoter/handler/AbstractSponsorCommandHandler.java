package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.exception.SponsorException;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wk-mia
 * 2020-9-13
 * 点对点模式客户端-命令发起处理器
 * 负责客户端向服务器发起命令，包括MASTER以及SLAVE的各种业务命令。该处理器仅仅负责发起命令，
 * 不负责处理客户端给出的应答。
 */
public abstract class AbstractSponsorCommandHandler extends AbstractClientHandler{

    /**
     * 不负责处理服务器给出的应答，此处不作任何处理。
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        return;
    }

    /**
     * 发起请求
     * @param baseRequest 请求体
     * @throws SponsorException 发起命令异常
     */
    protected abstract void sponsor(BaseRequest baseRequest) throws SponsorException;
}
