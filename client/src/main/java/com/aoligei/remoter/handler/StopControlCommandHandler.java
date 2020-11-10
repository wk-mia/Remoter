package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandSponsor;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.manage.TerminalManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wk-mia
 * 2020-10-21
 * 点对点模式客户端-停止控制处理器
 * 负责接受到服务器对于停止控制请求的处理结果。
 */
@Component(value = "StopControlCommandHandler")
public class StopControlCommandHandler extends AbstractClientHandler {

    /**客户端身份管理器*/
    private TerminalManage terminalManage;
    @Autowired
    public StopControlCommandHandler(TerminalManage terminalManage){
        this.terminalManage = terminalManage;
    }

    /**
     * 特定的处理器-停止控制处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws RemoterException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException {
        /**
         * 停止远程控制的业务处理：
         * 1. 移除该远程连接
         * 2. 如当前客户端已没有任何远程工作，更新客户端维护的远程信息
         * 3. 如当前客户端已没有任何远程工作，切换心跳任务
         */
        if( ! StringUtils.isEmpty(baseResponse.getConnectionId())){
            String connectionId = baseResponse.getConnectionId();
            /**移除该远程连接*/
            terminalManage.removeConnection(connectionId);
            if(terminalManage.getConnectionIds() == null || terminalManage.getConnectionIds().size() == 0){
                /**更新客户端维护的远程信息*/
                terminalManage.setRemotingFlag(false);
                /**切换心跳任务*/
                try{
                    logInfo("the server has removed all connections,the heartbeat mission is about to begin...");
                    ICommandSponsor cycleSponsor = CommandFactory.getCommandSponsor(CommandEnum.HEART_BEAT);
                    cycleSponsor.sponsor(new BaseRequest());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
