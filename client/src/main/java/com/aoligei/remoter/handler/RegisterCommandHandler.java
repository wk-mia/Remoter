package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.enums.StatusEnum;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.manage.ClientManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wk-mia
 * 2020-9-14
 * 点对点模式客户端-注册处理器
 * 负责接受到服务器对于注册请求的处理结果。
 */
public class RegisterCommandHandler extends AbstractClientHandler {

    @Autowired
    private ClientManage clientManage;

    /**
     * 特定的处理器-注册处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        if(baseResponse.getStatus() == StatusEnum.OK){
            /**
             * 注册成功，服务器会将客户端身份识别码放入Data域中。
             */
            logInfo(baseResponse,baseResponse.getMessage());

            clientManage.getClientInfo().setClientId((String) baseResponse.getData());
        }else {
            logError(baseResponse,baseResponse.getMessage());
        }
    }
}
