package com.aoligei.remoter.service.listener;


import com.aoligei.remoter.manage.SingleTaskManage;
import com.aoligei.remoter.service.action.IKeyBoard;
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
public class KeyBoardActionListener extends KeyAdapter implements IKeyBoard {

    private static final Logger log = LoggerFactory.getLogger(KeyBoardActionListener.class);

    /**当前连接编码*/
    private String connectionId;

    /**单次任务管理器*/
    private SingleTaskManage single;
    public KeyBoardActionListener(String connectionId){
        this.connectionId = connectionId;
        this.single = SpringBeanUtil.getBean(SingleTaskManage.class);
    }

    /**
     * 按键按下
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        String desc = KeyEvent.getKeyText(e.getKeyCode()).concat(" pressed");
        log.debug(MessageFormat.format("debug: {0}", desc));
        this.sendKeyEvent(e, true);
    }

    /**
     * 按键释放
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        String desc = KeyEvent.getKeyText(e.getKeyCode()).concat(" released");
        log.debug(MessageFormat.format("debug: {0}", desc));
        this.sendKeyEvent(e, false);
    }

    /**
     * 发起键事件
     * @param e 源事件
     * @param keyStatus 当前键的状态：true->按下；false->释放
     */
    @Override
    public void sendKeyEvent(KeyEvent e, boolean keyStatus){
        final KeyBoardEvent event = new KeyBoardEvent();
        event.setKeyCode(e.getKeyCode());
        event.setKeyStatus(keyStatus);
        /**发送键命令*/
        single.sendKeyBoard(connectionId,event);
    }
}
