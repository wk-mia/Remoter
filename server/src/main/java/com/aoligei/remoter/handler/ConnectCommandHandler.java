package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.annotation.RequestInspect;
import com.aoligei.remoter.business.ResponseProcessor;
import com.aoligei.remoter.constant.MessageConstants;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.manage.impl.OnlineRosterManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-7
 * 点对点模式服务端-连接处理器
 * 负责初始话连接信息并维护所有客户端与服务器连接信息。这里的连接信息并不代表主控端
 * 与受控端的连接，而是客户端与服务器的连接；从逻辑上来看，主控端和受控端的连接包含
 * 了：主控端和服务器的连接、受控端和服务器的连接。具体参见GroupCacheManage和
 * OnlineConnectionManage的定义。
 */
@Component(value = "ConnectCommandHandler")
public class ConnectCommandHandler extends AbstractServerCensorC2CHandler  {


    private OnlineRosterManage onlineConnectionManage;
    @Autowired
    public void setOnlineConnectionManage(OnlineRosterManage onlineConnectionManage){
        this.onlineConnectionManage = onlineConnectionManage;
    }

    /**
     * 特定的处理器：连接处理器
     * 初始化连接并维护这个连接信息
     * 检查项 = {InspectEnum.CLIENT_ID, InspectEnum.COMMAND_ENUM}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws RemoterException 异常信息
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.CLIENT_ID, InspectEnum.COMMAND_ENUM})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws RemoterException {
        /**
         * 连接时不需要设备类型，设备类型在Control处理器中指定
         */
        TerminalTypeEnum terminalTypeEnum = TerminalTypeEnum.UNKNOWN;
        onlineConnectionManage.add(baseRequest.getClientId(),channelHandlerContext.channel(),terminalTypeEnum);

        BaseResponse baseResponse = ResponseProcessor.buildResponseOK(null, TerminalTypeEnum.SERVER,baseRequest.getCommandEnum(),
                null, MessageConstants.CLIENT_CONNECT_SUCCEEDED);
        channelHandlerContext.writeAndFlush(baseResponse);

        logInfo("client [" + baseRequest.getClientId() + "] connected success");
    }
}
