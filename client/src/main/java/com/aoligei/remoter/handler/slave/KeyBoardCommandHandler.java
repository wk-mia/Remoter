package com.aoligei.remoter.handler.slave;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.event.KeyBoardEvent;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.handler.AbstractClientHandler;
import com.aoligei.remoter.service.action.IReplay;
import com.aoligei.remoter.service.driver.EventReplay;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-10-26
 * 点对点模式受控端-键盘事件处理器
 * 负责接受键盘事件并处理
 */
@Component(value = "KeyBoardCommandHandler")
public class KeyBoardCommandHandler extends AbstractClientHandler {

    /**事件重播器*/
    private EventReplay eventReplay;
    @Autowired
    public KeyBoardCommandHandler(EventReplay eventReplay){
        this.eventReplay = eventReplay;
    }

    /**
     * 特定的处理器-键盘事件处理器，供SLAVER使用。
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws RemoterException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException {
        /**重放收到的键盘事件*/
        if(baseResponse.getData() instanceof KeyBoardEvent){
            KeyBoardEvent event = (KeyBoardEvent) baseResponse.getData();
            IReplay replay = eventReplay;
            replay.replayKeyBoardEvent(event);
        }
    }
}
