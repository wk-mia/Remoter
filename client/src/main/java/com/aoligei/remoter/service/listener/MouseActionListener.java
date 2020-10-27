package com.aoligei.remoter.service.listener;

import com.aoligei.remoter.event.MouseActionEvent;
import com.aoligei.remoter.event.MouseActionEvent.MouseActionEnum;
import com.aoligei.remoter.manage.SingleTaskManage;
import com.aoligei.remoter.manage.ThreadPoolManage;
import com.aoligei.remoter.util.AccessConfigUtil;
import com.aoligei.remoter.util.AccessConfigUtil.Config;
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

    /**
     * 单击次数的定义
     */
    private static final int SINGLE_CLICK_COUNT = 1;
    /**
     * 双击次数的定义
     */
    private static final int DOUBLE_CLICK_COUNT = 2;
    /**
     * 鼠标双击检查延时时间
     */
    private static final int DOUBLE_CHECK_DELAY = AccessConfigUtil.getNumber(Config.PARAM,"task.mouse.check.delay");
    /**
     * 鼠标左键点击计数器
     */
    private AtomicInteger leftClickCounter = new AtomicInteger(0);

    /**当前连接编码*/
    private String connectionId;

    /**单次任务管理器*/
    private SingleTaskManage single;

    public MouseActionListener(String connectionId){
        this.single = SpringBeanUtil.getBean(SingleTaskManage.class);
        this.connectionId = connectionId;
    }

    /**
     * 鼠标点击事件
     * @param e 源事件
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getButton() == e.BUTTON1){
            /**鼠标左键需要考虑双击情况*/
            leftButtonClicked(e);
        }else {
            /**其它键直接发送单击事件*/
            this.mouseSingleClicked(e);
        }
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

    /**
     * 鼠标左键事件
     * @param e 源事件
     */
    private void leftButtonClicked(MouseEvent e){

        if(leftClickCounter.incrementAndGet() == DOUBLE_CLICK_COUNT){
            /**点击次数加1如果等于双击定义的次数，则该事件为双击事件*/
            this.mouseDoubleClicked(e);
        }else {
            /**启用延时任务检查：如果过了一定的时间该键仍然是没有被点击过，则表示该鼠标事件为单击*/
            ThreadPoolManage.submit(()->{
                if(leftClickCounter.get() == SINGLE_CLICK_COUNT){
                    /**在一段时间内，左键没有再被点击过，则表示为单击*/
                    this.mouseSingleClicked(e);
                }
            }, DOUBLE_CHECK_DELAY);
        }
    }

    /**
     * 鼠标左键双击事件
     * @param event 源事件
     */
    private void mouseDoubleClicked(MouseEvent event){
        /**事件发生后将记录器清零*/
        this.leftClickCounter.set(0);
        /**发送鼠标双击事件*/
        final MouseActionEvent mouseAction = new MouseActionEvent();
        mouseAction.setMouseAction(MouseActionEnum.DOUBLE_CLICK);
        int[] site = new int[]{event.getX(),event.getY()};
        mouseAction.setSite(site);

    }

    /**
     * 鼠标单击事件
     * @param event 源事件
     */
    private void mouseSingleClicked(MouseEvent event){
        /**事件发生后将记录器清零*/
        this.leftClickCounter.set(0);
        /**发送鼠标双击事件*/
        final MouseActionEvent mouseAction = new MouseActionEvent();
        MouseActionEnum actionEnum = MouseActionEvent.convert(event.getButton());
        mouseAction.setMouseAction(actionEnum);
        int[] site = new int[]{event.getX(),event.getY()};
        mouseAction.setSite(site);

    }
}
