package com.aoligei.remoter.service.listener;

import com.aoligei.remoter.manage.SingleTaskManage;
import com.aoligei.remoter.util.SpringBeanUtil;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wk-mia
 * 2020-10-23
 * 主控端鼠标事件监听
 * 包含：鼠标单击、双击、移动、滚轮等
 */
public class MouseActionListener extends MouseAdapter {

    private static final Logger log = LoggerFactory.getLogger(MouseActionListener.class);

    /**单次任务管理器*/
    private SingleTaskManage task;
    public MouseActionListener(){
        this.task = SpringBeanUtil.getBean(SingleTaskManage.class);
    }

    /**
     * 鼠标点击计数器
     */
    private AtomicInteger clickCounter = new AtomicInteger(0);

    /**
     * 是否已经双击
     */
    private volatile boolean isDoubleClicked;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
