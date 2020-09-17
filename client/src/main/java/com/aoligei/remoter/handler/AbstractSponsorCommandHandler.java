package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.constant.SponsorConstants;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.exception.SponsorException;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author wk-mia
 * 2020-9-13
 * 点对点模式客户端-命令发起处理器
 * 负责客户端向服务器发起命令，包括MASTER以及SLAVE的各种业务命令。该处理器仅仅负责发起命令，
 * 不负责处理客户端给出的应答。
 */
public abstract class AbstractSponsorCommandHandler extends AbstractClientHandler{

    private static Logger log;

    public AbstractSponsorCommandHandler(){
        /**
         * 初始化实现类的日志处理器
         */
        log = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * 不负责处理服务器给出的应答，此处不作任何处理。
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        return;
    }

    /**
     * 发送请求
     * @param request 请求体
     * @throws SponsorException
     */
    protected void sendRequest(BaseRequest request) throws SponsorException {
        ChannelHandlerContext chc = super.getChannelHandlerContext();
        if (chc != null && chc.channel() != null && chc.channel().isOpen()){
            logInfo(request, SponsorConstants.PREPARE_SEND);
            chc.writeAndFlush(request);
        }else{
            logError(request, SponsorConstants.LOST_CONNECTION);
            throw new SponsorException(SponsorConstants.LOST_CONNECTION);
        }
    }

    /**
     * Info级别日志，发起任务处理器使用
     * @param object 对象
     * @param info 输出信息
     */
    protected void logInfo(Object object, String info){
        log.info(MessageFormat.format("{0};message:{1}",object.getClass().getCanonicalName(),info));
    }

    /**
     * Error级别日志，发起任务处理器使用
     * @param object 对象
     * @param error 异常信息
     */
    protected void logError(Object object,String error){
        log.error(MessageFormat.format("{0};error:{1}",object.getClass().getCanonicalName(),error));
    }

    /**
     * 发起请求
     * @param baseRequest 请求体
     * @throws SponsorException 发起命令异常
     */
    public abstract void sponsor(BaseRequest baseRequest) throws SponsorException;
}
