package com.aoligei.remoter.handler.slave;

import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.event.MouseActionEvent;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.handler.AbstractClientHandler;
import com.aoligei.remoter.service.action.IReplay;
import com.aoligei.remoter.service.driver.EventReplay;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-14
 * 点对点模式受控端-鼠标输入处理器
 * 负责接受鼠标输入并处理
 */
@Component(value = "MouseCommandHandler")
public class MouseCommandHandler extends AbstractClientHandler {

    /**事件重播器*/
    private EventReplay eventReplay;
    @Autowired
    public MouseCommandHandler(EventReplay eventReplay){
        this.eventReplay = eventReplay;
    }

    /**
     * 特定的处理器-鼠标事件处理器，供SLAVER使用。
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws RemoterException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws RemoterException {
        /**重放收到的鼠标事件*/
        if(baseResponse.getData() instanceof MouseActionEvent){
            MouseActionEvent event = (MouseActionEvent) baseResponse.getData();
            IReplay replay = eventReplay;
            replay.replayMouseActionEvent(event);
        }
    }
}
