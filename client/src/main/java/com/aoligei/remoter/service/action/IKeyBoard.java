package com.aoligei.remoter.service.action;

import java.awt.event.KeyEvent;

/**
 * @author wk-mia
 * 2020-9-26
 * 主控端键盘事件
 */
public interface IKeyBoard {

    /**
     * 发起键事件
     * @param e 源事件
     * @param keyStatus 当前键的状态：true->按下；false->释放
     */
    void sendKeyEvent(KeyEvent e, boolean keyStatus);
}
