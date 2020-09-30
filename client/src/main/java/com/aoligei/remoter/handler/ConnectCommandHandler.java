package com.aoligei.remoter.handler;


import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandSponsor;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.manage.TerminalManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-13
 * 点对点模式客户端-连接处理器
 * 负责接受到服务器对于连接请求的处理结果。
 */
@Component(value = "ConnectCommandHandler")
public class ConnectCommandHandler extends AbstractClientHandler {

    /**客户端身份管理器*/
    private TerminalManage terminalManage;
    @Autowired
    private void setTerminalManage(TerminalManage terminalManage){
        this.terminalManage = terminalManage;
    }

    /**
     * 特定的处理器-连接处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        /**更新客户端缓存*/
        terminalManage.setConnectionId(baseResponse.getConnectionId());
        try {
            ICommandSponsor cycleSponsor = CommandFactory.getCommandSponsor(CommandEnum.HEART_BEAT);
            /**记录通道：所有Sponsor记录下context*/
            cycleSponsor.setContext(channelHandlerContext);
            /**连接成功后开始心跳任务*/
            cycleSponsor.sponsor(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
