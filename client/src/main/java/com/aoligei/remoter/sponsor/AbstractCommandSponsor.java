package com.aoligei.remoter.sponsor;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.command.ICommandHandler;
import com.aoligei.remoter.command.ICommandSponsor;
import com.aoligei.remoter.constant.SponsorConstants;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.exception.SponsorException;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author wk-mia
 * 2020-9-13
 * 点对点模式客户端-命令发起处理器
 * 负责客户端向服务器发起命令，包括MASTER以及SLAVE的各种业务命令。该处理器仅仅负责发起命令，
 * 不负责处理客户端给出的应答。
 */
@Component(value = "AbstractSponsorCommandHandler")
public abstract class AbstractCommandSponsor implements ICommandSponsor<BaseRequest> {

    private static Logger log;


    public AbstractCommandSponsor(){
        /**
         * 初始化实现类的日志处理器
         */
        log = LoggerFactory.getLogger(this.getClass());
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
     * @param request 请求体
     * @throws SponsorException 发起命令异常
     */
    @Override
    public void sponsor(BaseRequest request) throws SponsorException {
        particularSponsor(request);
    }


    /**
     * 特定的发起器
     * @param request 待发送消息
     * @throws SponsorException 异常信息
     */
    protected abstract void particularSponsor(BaseRequest request) throws SponsorException;
}
