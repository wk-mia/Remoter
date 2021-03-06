package com.aoligei.remoter.handler.master;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.handler.AbstractClientHandler;
import com.aoligei.remoter.service.action.IInteract;
import com.aoligei.remoter.service.listener.SlaverPageActionListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-14
 * 点对点模式主控端-屏幕截图处理器
 * 负责接受屏幕截图并处理
 */
@Component(value = "ScreenShotsCommandHandler")
public class ScreenShotsCommandHandler extends AbstractClientHandler {

    /**远程窗口事件监听器*/
    private SlaverPageActionListener remoteListener;
    @Autowired
    public ScreenShotsCommandHandler( SlaverPageActionListener remoteListener){
        this.remoteListener = remoteListener;
    }

    /**
     * 特定的处理器-屏幕截图处理器，供MASTER使用。
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws RemoterException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException {
        /**播放收到的屏幕截图*/
        if(baseResponse.getData() instanceof byte[]) {
            byte[] bytes = (byte[]) baseResponse.getData();
            IInteract remotePage = remoteListener;
            remotePage.play(baseResponse.getConnectionId(), bytes);
        }
    }
}
