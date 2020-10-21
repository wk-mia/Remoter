package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.beans.RemotingElement;
import com.aoligei.remoter.constant.IncompleteParamConstants;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IncompleteParamException;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.manage.impl.RemotingRosterManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-10-21
 * 点对点模式客户端-停止控制处理器
 * 负责接受到服务器对于停止控制请求的处理结果。
 */
@Component(value = "StopControlCommandHandler")
public class StopControlCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 处于连接中的通道组缓存
     */
    private RemotingRosterManage remotingRosterManage;
    @Autowired
    public StopControlCommandHandler(RemotingRosterManage remotingRosterManage){
        this.remotingRosterManage = remotingRosterManage;
    }

    /**
     *
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws ServerException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws ServerException {
        /**通知各个终端停止远程控制*/
        String connectionId = baseRequest.getConnectionId();
        BaseResponse response = BuildUtil
                .buildResponseOK(connectionId, TerminalTypeEnum.SERVER, CommandEnum.STOP_CONTROL, null, null);
        remotingRosterManage.notifyAllMaster(connectionId,response);
        remotingRosterManage.notifySlave(connectionId,response);
        /**移除该连接组*/
        remotingRosterManage.unRegister(connectionId);
        logInfo(MessageFormat.format("the server has removed connection:{}",connectionId));
    }

}
