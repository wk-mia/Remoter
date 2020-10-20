package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.beans.RemotingElement;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandHandler;
import com.aoligei.remoter.constant.ExceptionStyleConstants;
import com.aoligei.remoter.constant.ResponseConstants;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.manage.impl.OnlineRosterManage;
import com.aoligei.remoter.manage.impl.RemotingRosterManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wk-mia
 * 2020-9-1
 * 点对点模式服务端-转发数据处理器
 * 适用于远程控制的业务场景，一个终端控制另一个终端，在这个过程中，不允许第三方终端的加入
 */
@Component
@ChannelHandler.Sharable
public class ServerChannelC2CHandler extends SimpleChannelInboundHandler<BaseRequest> {

    private static Logger log = LoggerFactory.getLogger(ServerChannelC2CHandler.class);

    /**
     * 连接组管理器
     */
    private RemotingRosterManage remotingRoster;
    /**
     * 在线连接管理器
     */
    private OnlineRosterManage onlineRoster;
    @Autowired
    public ServerChannelC2CHandler(RemotingRosterManage remotingRoster, OnlineRosterManage onlineRoster){
        this.remotingRoster = remotingRoster;
        this.onlineRoster = onlineRoster;
    }

    /**
     * 每个信息入站时都会被调用。
     * 服务器的处理主要包括有：维护当前在线的所有连接、转发命令。
     * 事实上，还应有拦截非法/敏感信息、安全检查、持久化等工作，可在该方法中进行扩展。
     * @param channelHandlerContext ChannelHandler和ChannelPipeline之间的上下文联系
     * @param baseRequest 请求体
     * @throws Exception 异常信息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws Exception {
        /**
         * 分发命令并进行处理
         */
        ICommandHandler commandHandler = CommandFactory.getCommandHandler(baseRequest.getCommandEnum());
        commandHandler.handle(channelHandlerContext,baseRequest);
    }

    /**
     * 异常处理：记录异常信息，关闭连接
     * 当异常发生时，很难从未知的异常信息中让连接恢复，所以这里直接关闭连接；
     * 当然，如果有某些特定的异常情况在掌控范围内，可直接在这里尝试恢复这个错误。
     * @param channelHandlerContext ChannelHandler和ChannelPipeline之间的上下文联系
     * @param cause 当前异常信息
     * @throws Exception 往上层抛异常信息
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause) throws Exception {
        log.error(cause.getMessage(),cause);
        if(cause.getClass().getSimpleName().equals(ExceptionStyleConstants.CONTROLLABLE)){
            /**可控制的异常类型,仅通知客户端异常信息*/
            BaseResponse baseResponse = BuildUtil.buildResponseFAIL(null, TerminalTypeEnum.SERVER,
                    CommandEnum.EXCEPTION, null, cause.getMessage());
            if(channelHandlerContext != null){
                channelHandlerContext.writeAndFlush(baseResponse);
            }
        }else{
            /**需要断开连接的异常,通知客户端异常信息并断开连接*/
            String message = cause.getMessage() + ", " + ResponseConstants.WILL_BE_DISCONNECTED;
            BaseResponse baseResponse = BuildUtil.buildResponseERROR(null, TerminalTypeEnum.SERVER,
                    CommandEnum.EXCEPTION, null, message);
            if(channelHandlerContext != null){
                channelHandlerContext.writeAndFlush(baseResponse);
            }
            log.info(ResponseConstants.WILL_BE_DISCONNECTED);
            channelHandlerContext.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        /**从连接组列表中移除连接组*/
        remotingRoster.unRegister(ctx.channel());
        /**从在线连接列表中移除连接*/
        onlineRoster.remove(ctx.channel());
    }
}
