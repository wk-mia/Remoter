package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.command.ICommandHandler;
import com.aoligei.remoter.exception.RemoterException;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author wk-mia
 * 2020-9-13
 * 点对点模式客户端-命令审查处理器
 * 负责审查消息体是否合法：主要包含消息体中的身份识别码是否合法，如果合
 * 法则执行相应的命令处理器，如果不合法，则给服务器返回响应的错误信息。
 */
@Component(value = "AbstractClientHandler")
public abstract class AbstractClientHandler implements ICommandHandler<BaseResponse> {

    private Logger log = LoggerFactory.getLogger(AbstractClientHandler.class);

    /**
     * 分发至相应的处理器进行处理
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse Channel输入对象
     * @throws RemoterException
     */
    @Override
    public void handle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException {
        log.debug(MessageFormat.format("response: {0}",baseResponse));
        particularHandle(channelHandlerContext,baseResponse);
    }

    /**
     * 丢弃命令
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseBound Channel输入对象
     */
    @Override
    public void abandon(ChannelHandlerContext channelHandlerContext, BaseResponse baseBound) {

    }

    /**
     * Info级别日志
     * @param info 输出信息
     */
    protected void logInfo(String info){
        log.info(MessageFormat.format("info: {0}",info));
    }

    /**
     * Error级别日志
     * @param error 异常信息
     */
    protected void logError(String error){
        log.error(MessageFormat.format("error: {0}",error));
    }


    /**
     * 审查完后具体的处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws RemoterException 异常信息
     */
    protected abstract void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException;
}
