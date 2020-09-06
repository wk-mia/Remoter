package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.exception.NettyServerException;
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
 * 点对点模式服务端-声音输出处理器
 * 业务上：声音输出的信息是由Slava端发出的，服务端并不负责声音输出的解析和展示，仅仅
 * 负责将该声音输出信息转发给所有关注着这个Slave端的Master集。需注意的是，尽管在代码
 * 中，是遍历Masters集合分别转发该消息；但因目前暂不支持单个Slaver同时被多个Masters
 * 关注，所以实质上仍是点对点的业务场景。
 */
public class VoiceCommandHandler extends AbstractServerCensorC2CHandler {

    @Autowired
    private GroupCacheManage groupCacheManage;

    /**
     * 特定的处理器：声音输出处理器
     * 检查项 = {REQUEST_IS_ILLEGAL,CONNECTION_NOT_FIND}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws NettyServerException
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.REQUEST_IS_ILLEGAL,InspectEnum.CONNECTION_NOT_FIND})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
        /**
         * 转发消息给主控客户端
         */
        BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getClientId(),null, CommandEnum.VOICE_OUTPUT,baseRequest.getData(),null);
        groupCacheManage.notifyAllMaster(baseRequest.getClientId(),baseResponse);
    }
}
