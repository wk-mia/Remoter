package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.annotation.RequestInspect;
import com.aoligei.remoter.business.ResponseProcessor;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.manage.impl.RemotingRosterManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author wk-mia
 * 2020-9-3
 * 点对点模式服务端-屏幕截图处理器
 * 业务上：屏幕截图的信息是由Slava端发出的，服务端并不负责屏幕截图的解析和展示，仅仅
 * 负责将该屏幕截图信息转发给所有关注着这个Slave端的Master集。需注意的是，尽管在代码
 * 中，是遍历Masters集合分别转发该消息；但因目前暂不支持单个Slaver同时被多个Masters
 * 关注，所以实质上仍是点对点的业务场景。
 */
@Component(value = "ScreenShotsCommandHandler")
public class ScreenShotsCommandHandler extends AbstractServerCensorC2CHandler {

    @Autowired
    private RemotingRosterManage remotingRosterManage;

    /**
     * 特定的处理器：屏幕截图处理器
     * 检查项 = {InspectEnum.CLIENT_ID,InspectEnum.CONNECTION_ID,InspectEnum.COMMAND_ENUM,InspectEnum.TERMINAL_TYPE_ENUM,InspectEnum.DATA}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws RemoterException
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.CLIENT_ID,InspectEnum.CONNECTION_ID,
            InspectEnum.COMMAND_ENUM,InspectEnum.TERMINAL_TYPE_ENUM,InspectEnum.DATA})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws RemoterException {

        String clientId = baseRequest.getClientId();
        StringBuilder info = new StringBuilder().append("receive a screenshot from: ").append(clientId);
        logInfo(info.toString());
        /**
         * 转发消息给主控客户端
         */
        BaseResponse baseResponse = ResponseProcessor.buildResponseOK(baseRequest.getConnectionId(),
                TerminalTypeEnum.SERVER_2_MASTER,CommandEnum.SCREEN_SHOTS,baseRequest.getData(),null);
        String connectionId = baseRequest.getConnectionId();
        logInfo(MessageFormat.format("forward the screenshot to:{0}",connectionId));
        remotingRosterManage.notifyAllMaster(connectionId,baseResponse);
    }
}
