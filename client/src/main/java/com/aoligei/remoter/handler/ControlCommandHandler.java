package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.business.RequestProcessor;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandSponsor;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.ResponseStatusEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.event.ControlEvent;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.manage.TerminalManage;
import com.aoligei.remoter.service.action.IInteract;
import com.aoligei.remoter.service.listener.SlaverPageActionListener;
import io.netty.channel.ChannelHandlerContext;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wk-mia
 * 2020-9-14
 * 点对点模式客户端-控制处理器
 * 负责接受到服务器对于控制请求的处理结果。
 */
@Component(value = "ControlCommandHandler")
public class ControlCommandHandler extends AbstractClientHandler {

    /**客户端身份管理器*/
    private TerminalManage terminalManage;
    /**请求组装器*/
    private RequestProcessor processor;
    /**远程窗口事件监听器*/
    private SlaverPageActionListener remoteListener;
    @Autowired
    public ControlCommandHandler(TerminalManage terminalManage, SlaverPageActionListener remoteListener, RequestProcessor processor){
        this.terminalManage = terminalManage;
        this.remoteListener = remoteListener;
        this.processor = processor;
    }

    /**
     * 特定的处理器-控制处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws RemoterException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException {
        if(baseResponse.getTerminalTypeEnum() == TerminalTypeEnum.SERVER_2_SLAVE){
            /**受控端的业务逻辑*/
            this.slaverHandle(channelHandlerContext,baseResponse);
        }else if(baseResponse.getTerminalTypeEnum() == TerminalTypeEnum.SERVER_2_MASTER){
            /**主控端的业务逻辑*/
            this.masterHandle(channelHandlerContext,baseResponse);
        }
    }

    /**
     * 受控端的业务处理：
     * 1. 选择是否接受该连接，并返回服务器结果；
     * 2. 如接受，更新客户端相关信息，开始发送屏幕截图信息。
     * @param channelHandlerContext 通道上下文
     * @param baseResponse 原始返回
     */
    private void slaverHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse){
        if(! StringUtils.isEmpty(baseResponse.getConnectionId())){
            /**此处为客户端直接同意控制请求*/
            Boolean agree = Boolean.TRUE;
            ControlEvent event = new ControlEvent();
            event.setAccepted(agree);
            if(agree){
                /**获取屏幕分辨率*/
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                event.setScreenWidth(screenSize.width);
                event.setScreenHeight(screenSize.height);
            }
            BaseRequest baseRequest = processor.buildAnswerControlRequest(baseResponse.getConnectionId(),event);
            channelHandlerContext.writeAndFlush(baseRequest);
            /**更新客户端连接编码、当前受控状态*/
            terminalManage.createConnection(baseResponse.getConnectionId());
            terminalManage.setRemotingFlag(true);
            logInfo("the client has agreed to the remote connection,the screen shots mission is about to begin...");
            /**开始发送屏幕截图：屏幕截图任务需要连接编码*/
            try{
                ICommandSponsor cycleSponsor = CommandFactory.getCommandSponsor(CommandEnum.SCREEN_SHOTS);
                BaseRequest request = new BaseRequest(){{setConnectionId(baseResponse.getConnectionId());}};
                cycleSponsor.sponsor(request);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 主控端的业务处理：
     * 1. 如受控端同意连接，保存连接编码并启动远程窗口，准备展示屏幕截图；
     * 2. 如连接失败，则记录日志。
     * @param channelHandlerContext 通道上下文
     * @param baseResponse 原始返回
     */
    private void masterHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse){
        if(baseResponse.getStatus() == ResponseStatusEnum.OK){
            ControlEvent event = (ControlEvent) baseResponse.getData();
            logInfo(baseResponse.getMessage());
            /**成功，设置连接编码。*/
            terminalManage.createConnection(baseResponse.getConnectionId());
            /**启动远程窗口,窗口标题为连接编码,面板大小为受控端的屏幕大小*/
            IInteract remotePage = remoteListener;
            remotePage.call(baseResponse.getConnectionId(), event.getScreenWidth(), event.getScreenHeight());
        }else {
            logError(baseResponse.getMessage());
        }

    }
}
