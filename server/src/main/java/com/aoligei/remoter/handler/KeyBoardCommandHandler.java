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
import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-4
 * 点对点模式服务端-键盘输入处理器
 * 业务上：键盘输入的信息是由Master端发出的，服务端并不负责键盘输入的解析工作，仅仅
 * 负责将该键盘输入信息转发给与之对应的Slave端。需注意的是，尽管在代码中是遍历
 * targetClientId分别转发该消息；但因目前暂不支持单个Master同时关注多个Slaver，
 * 所以实质上仍是点对点的业务场景。这部分的控制在RequestInspect的相关内容中。
 */
@Component(value = "KeyBoardCommandHandler")
public class KeyBoardCommandHandler extends AbstractServerCensorC2CHandler {

    private RemotingRosterManage remotingRosterManage;
    @Autowired
    public KeyBoardCommandHandler(RemotingRosterManage remotingRosterManage){
        this.remotingRosterManage = remotingRosterManage;
    }

    /**
     * 特定的处理器：键盘输入处理器
     * 检查项 = {InspectEnum.CLIENT_ID,InspectEnum.CONNECTION_ID,InspectEnum.COMMAND_ENUM,
     *                              InspectEnum.TERMINAL_TYPE_ENUM,InspectEnum.DATA}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws RemoterException
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.CLIENT_ID,InspectEnum.CONNECTION_ID,
            InspectEnum.COMMAND_ENUM,InspectEnum.TERMINAL_TYPE_ENUM,InspectEnum.DATA})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws RemoterException {
        String clientId = baseRequest.getClientId();
        StringBuilder info = new StringBuilder().append("receive a keyboard action from: ").append(clientId);
        logInfo(info.toString());
        /**转发消息给受控客户端*/
        BaseResponse baseResponse = ResponseProcessor.buildResponseOK(baseRequest.getConnectionId(),
                TerminalTypeEnum.SERVER, CommandEnum.KEYBOARD,baseRequest.getData(),null);
        String connectionId = baseRequest.getConnectionId();
        logInfo(MessageFormat.format("forward the keyboard action to:{0}",connectionId));
        remotingRosterManage.notifySlave(baseRequest.getConnectionId(),baseResponse);
    }
}
