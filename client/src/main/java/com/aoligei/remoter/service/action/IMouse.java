package com.aoligei.remoter.service.action;

import com.aoligei.remoter.event.MouseActionEvent;

/**
 * @author wk-mia
 * 2020-10-27
 * 主控端鼠标事件
 */
public interface IMouse {

    /**
     * 发送鼠标事件
     * @param e 源事件
     */
    void sendMouseEvent(MouseActionEvent e);
}
