package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.business.RequestProcessor;
import com.aoligei.remoter.enums.ResponseStatusEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.manage.TerminalManage;
import com.aoligei.remoter.sponsor.SponsorCommandHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-13
 * 点对点模式客户端-连接处理器
 * 负责接受到服务器对于连接请求的处理结果。
 */
@Component(value = "ConnectCommandHandler")
public class ConnectCommandHandler extends AbstractClientHandler {

    private TerminalManage manage;
    private RequestProcessor processor;
    private SponsorCommandHandler sponsorCommandHandler;
    @Autowired
    private void setTerminalManage(TerminalManage manage,RequestProcessor processor,SponsorCommandHandler sponsorCommandHandler){
        this.manage = manage;
        this.processor = processor;
        this.sponsorCommandHandler = sponsorCommandHandler;
    }

    /**
     * 特定的处理器-连接处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        /**记录通道*/
        sponsorCommandHandler.setContext(channelHandlerContext);
        /**更新客户端缓存*/
        manage.setConnectionId(baseResponse.getConnectionId());
        /**连接成功后，开始给服务器发送心跳*/
        BaseRequest baseRequest = processor.buildHeartbeatRequest();
        sponsorCommandHandler.sponsor(baseRequest);
    }
}
