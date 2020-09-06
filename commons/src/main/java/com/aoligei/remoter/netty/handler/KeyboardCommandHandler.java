package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.netty.aop.RequestInspect;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.netty.beans.GroupCacheManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wk-mia
 * 2020-9-4
 * 点对点模式服务端-键盘输入处理器
 * 业务上：键盘输入的信息是由Master端发出的，服务端并不负责键盘输入的解析工作，仅仅
 * 负责将该键盘输入信息转发给与之对应的Slave端。需注意的是，尽管在代码中是遍历
 * targetClientId分别转发该消息；但因目前暂不支持单个Master同时关注多个Slaver，
 * 所以实质上仍是点对点的业务场景。这部分的控制在RequestInspect的相关内容中。
 */
public class KeyboardCommandHandler extends AbstractServerCensorC2CHandler {

    @Autowired
    private GroupCacheManage groupCacheManage;

    /**
     * 特定的处理器：键盘输入处理器
     * 检查项 = {REQUEST_IS_ILLEGAL,MASTER_TO_SLAVES,MASTER_NOT_IN_GROUP}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws NettyServerException
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.REQUEST_IS_ILLEGAL,InspectEnum.MASTER_TO_SLAVES,InspectEnum.MASTER_NOT_IN_GROUP})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
        /**
         * 转发消息给受控客户端
         */
        BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getConnectionId(),baseRequest.getTerminalTypeEnum(), CommandEnum.KEYBOARD_INPUT,baseRequest.getData(),null);
        groupCacheManage.notifySlave(baseRequest.getConnectionId(),baseResponse);
    }
}
