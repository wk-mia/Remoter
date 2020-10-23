package com.aoligei.remoter.ui.service.listener;


import com.aoligei.remoter.bean.KeyBoardEvent;
import com.aoligei.remoter.manage.SingleTaskManage;
import com.aoligei.remoter.util.SpringBeanUtil;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wk-mia
 * 2020-10-23
 * 主控端键盘事件监听
 */
public class KeyBoardActionListener extends KeyAdapter {

    private static final Logger log = LoggerFactory.getLogger(KeyBoardActionListener.class);

    /**单次任务管理器*/
    private SingleTaskManage task;
    public KeyBoardActionListener(){
        this.task = SpringBeanUtil.getBean(SingleTaskManage.class);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String desc = KeyEvent.getKeyText(e.getKeyCode()).concat(" pressed");
        log.debug(MessageFormat.format("debug: {0}", desc));
        this.keyEvent(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        String desc = KeyEvent.getKeyText(e.getKeyCode()).concat(" released");
        log.debug(MessageFormat.format("debug: {0}", desc));
        this.keyEvent(e, false);
    }

    /**
     * 键事件
     * @param e 源事件
     * @param keyStatus 当前键的状态：true->按下；false->释放
     */
    private void keyEvent(KeyEvent e, boolean keyStatus){
        final KeyBoardEvent event = new KeyBoardEvent();
        event.setKeyCode(e.getKeyCode());
        event.setKeyStatus(keyStatus);
        /**发送键命令*/
        task.sendKeyboard();
    }
}
