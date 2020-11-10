package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.enums.ResponseStatusEnum;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.manage.TerminalManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wk-mia
 * 2020-9-24
 * 点对点模式客户端-异常处理器
 * 负责处理服务器给出的异常
 */
@Component(value = "ExceptionHandler")
public class ExceptionHandler extends AbstractClientHandler  {

    private TerminalManage terminalManage;
    @Autowired
    public ExceptionHandler(TerminalManage terminalManage){
        this.terminalManage = terminalManage;
    }

    /**
     * 特定的处理器-异常处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws RemoterException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException {
        /**通知用户*/

        logError(baseResponse.getMessage());
        if(baseResponse.getStatus() == ResponseStatusEnum.ERROR){
            /**服务器处理请求时发生需关闭连接的异常，此处同步客户端的通道信息*/
            if(! StringUtils.isEmpty(baseResponse.getConnectionId())) {
                terminalManage.removeConnection(null);
            }
            terminalManage.setRemotingFlag(false);
        }
    }
}
