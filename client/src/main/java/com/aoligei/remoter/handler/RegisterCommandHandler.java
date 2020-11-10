package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.manage.TerminalManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-14
 * 点对点模式客户端-注册处理器
 * 负责接受到服务器对于注册请求的处理结果。
 */
@Component(value = "RegisterCommandHandler")
public class RegisterCommandHandler extends AbstractClientHandler {

    @Autowired
    private TerminalManage terminalManage;

    /**
     * 特定的处理器-注册处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws RemoterException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException {
        /**
         * 注册成功，服务器会将客户端身份识别码放入Data域中。
         */
        terminalManage.getClientInfo().setClientId((String) baseResponse.getData());
    }
}
