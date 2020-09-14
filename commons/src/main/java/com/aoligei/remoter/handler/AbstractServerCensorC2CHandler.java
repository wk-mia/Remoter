package com.aoligei.remoter.handler;

import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.command.ICommandHandler;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author wk-mia
 * 2020-9-2
 * 点对点模式服务端-命令审查处理器
 * 负责审查消息体是否合法：主要包含消息体中的身份识别码是否合法，如果合
 * 法则执行相应的命令处理器，如果不合法，则给客户端返回响应的错误信息。
 */
public abstract class AbstractServerCensorC2CHandler implements ICommandHandler<BaseRequest> {

    private static Logger log;

    public AbstractServerCensorC2CHandler(){
        /**
         * 初始化实现类的日志处理器
         */
        log = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * 分发至相应的处理器进行处理
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest Channel输入对象
     * @throws ServerException
     */
    @Override
    public void handle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws ServerException {
        particularHandle(channelHandlerContext,baseRequest);
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
    protected void logInfo(BaseRequest baseRequest,String info){
        log.info(MessageFormat.format("{0};message:{1}",baseRequest,info));
    }

    /**
     * Error级别日志
     * @param baseRequest 请求体
     * @param error 异常信息
     */
    protected void logError(BaseRequest baseRequest,String error){
        log.error(MessageFormat.format("{0};message:{1}",baseRequest,error));
    }


    /**
     * 审查完后具体的处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws ServerException 异常信息
     */
    protected abstract void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws ServerException;
}
