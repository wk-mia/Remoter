package com.aoligei.remoter.netty.handler;

import com.aoligei.remoter.account.ClientAccountBooks;
import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.command.ICommandHandler;
import com.aoligei.remoter.util.ClientInfoCheckUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;

/**
 * @author wk-mia
 * 2020-9-2
 * 点对点模式服务端-命令处理器
 * 负责维护所有连接和处理命令
 */
public abstract class AbstractServerCommandC2CHandler implements ICommandHandler<BaseRequest> {

    private static Logger log;

    public AbstractServerCommandC2CHandler(){
        /**
         * 初始化实现类的日志处理器
         */
        log = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * 客户端已经注册过的账册
     */
    @Autowired
    private ClientAccountBooks clientAccountBooks;

    /**
     * 处理命令
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest Channel输入对象
     * @throws NettyServerException
     */
    @Override
    public void handle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException {
        /**
         * 检查当前身份识别码是否已经注册过
         * 若已注册，处理命令
         * 若未注册过，封装错误信息并返回给客户端
         */
        if(ClientInfoCheckUtil.isClientIdExist(clientAccountBooks.allClientInformation(),baseRequest.getClientId())){

        }else {
            this.logError(baseRequest, ExceptionMessageConstants.CLIENT_ID_EMPTY);
            this.back(baseRequest,channelHandlerContext,ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }
    }

    /**
     * 丢弃命令
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseBound Channel输入对象
     */
    @Override
    public void abandon(ChannelHandlerContext channelHandlerContext, BaseRequest baseBound) {

    }

    /**
     * Info级别日志
     * @param baseRequest 请求体
     * @param info 输出信息
     */
    private void logInfo(BaseRequest baseRequest,String info){
        log.info(MessageFormat.format("{0};message:{1}",baseRequest,info));
    }

    /**
     * Error级别日志
     * @param baseRequest 请求体
     * @param error 异常信息
     */
    private void logError(BaseRequest baseRequest,String error){
        log.error(MessageFormat.format("{0};message:{1}",baseRequest,error));
    }

    /**
     * 封装原始消息和异常信息并写回给客户端通道
     * @param baseRequest 原始消息
     * @param channelHandlerContext 通道上下文
     * @param exceptionMessage 异常信息
     */
    protected void back(BaseRequest baseRequest,ChannelHandlerContext channelHandlerContext,String exceptionMessage){
        /**
         * 构建返回消息体
         */
        BaseResponse baseResponse = new BaseResponse(){{
            setClientId(baseRequest.getClientId());
            setCommandEnum(baseRequest.getCommandEnum());
            setData(baseRequest.getData());
            setNettyServerException(new NettyServerException(exceptionMessage));
        }};
        /**
         * 写回客户端
         */
        channelHandlerContext.writeAndFlush(baseRequest);
    }

    protected abstract void handle0(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws NettyServerException;
}
