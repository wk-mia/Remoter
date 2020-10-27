package com.aoligei.remoter.service.action;

import com.aoligei.remoter.event.KeyBoardEvent;
import com.aoligei.remoter.event.MouseActionEvent;

/**
 * @author wk-mia
 * 2020-10-26
 * 受控端的事件重放
 */
public interface IReplay {

    /**
     * 重放键盘事件
     * @param event 键盘事件
     */
    void replayKeyBoardEvent(KeyBoardEvent event);

    /**
     * 重放鼠标事件
     * @param event 鼠标事件
     */
    void replayMouseActionEvent(MouseActionEvent event);
}
