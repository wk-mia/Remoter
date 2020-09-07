package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.constant.ResponseConstants;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.aop.RequestInspect;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.manage.OnlineConnectionManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wk-mia
 * 2020-9-7
 * 点对点模式服务端-连接处理器
 * 负责初始话连接信息并维护所有客户端与服务器连接信息。这里的连接信息并不代表主控端
 * 与受控端的连接，而是客户端与服务器的连接；从逻辑上来看，主控端和受控端的连接包含
 * 了：主控端和服务器的连接、受控端和服务器的连接。具体参见GroupCacheManage和
 * OnlineConnectionManage的定义。
 */
public class ConnectCommandHandler extends AbstractServerCensorC2CHandler  {

    @Autowired
    private OnlineConnectionManage onlineConnectionManage;

    /**
     * 特定的处理器：连接处理器
     * 初始化连接并维护这个连接信息
     * 检查项 = {PARAMS_IS_COMPLETE}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws NettyServerException 异常信息
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.CONNECT_PARAMS_IS_COMPLETE})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
        /**
         * 连接时不需要设备类型，设备类型在Control处理器中指定
         */
        TerminalTypeEnum terminalTypeEnumT = null;
        onlineConnectionManage.register(baseRequest.getClientId(),channelHandlerContext.channel(),
                onlineConnectionManage.getScheduled(channelHandlerContext,3),terminalTypeEnumT);

        BaseResponse baseResponse = BuildUtil.buildResponse(null,
                TerminalTypeEnum.SERVER,baseRequest.getCommandEnum(), ResponseConstants.CLIENT_CONNECT_SUCCEEDED,null);
        channelHandlerContext.writeAndFlush(baseResponse);

        logInfo(baseRequest,"客户端[" + baseRequest.getClientId() + "]连接服务器成功");
    }
}
