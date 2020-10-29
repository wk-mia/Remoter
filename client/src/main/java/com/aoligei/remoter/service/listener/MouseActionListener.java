package com.aoligei.remoter.service.listener;

import com.aoligei.remoter.event.MouseActionEvent;
import com.aoligei.remoter.event.MouseActionEvent.MouseActionEnum;
import com.aoligei.remoter.event.MouseActionEvent.MouseButtonEnum;
import com.aoligei.remoter.manage.SingleTaskManage;
import com.aoligei.remoter.manage.ThreadPoolManage;
import com.aoligei.remoter.service.action.IMouse;
import com.aoligei.remoter.util.AccessConfigUtil;
import com.aoligei.remoter.util.AccessConfigUtil.Config;
import com.aoligei.remoter.util.SpringBeanUtil;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wk-mia
 * 2020-10-23
 * 主控端鼠标事件监听
 * 包含：鼠标单击、鼠标双击、鼠标移动、滚轮滚动、鼠标拖拽等事件处理
 * 在鼠标的所有事件中，只有四个基本事件：鼠标按下、鼠标释放、鼠标移动、滚轮滚动。在这个监听
 * 器中，因考虑到网络传输延迟的情况，已对其中的[鼠标按下]和[鼠标释放]进行打包处理；基本上各
 * 项应处理的鼠标事件都已经不再需要单独的"鼠标按下"这一事件支持，例如：双击事件就是两次鼠标
 * 按下和两次鼠标释放的组合，所以这里不再对MASTER（主控端）的鼠标按下这一动作进行监听。有一
 * 个事件比较特殊：鼠标拖拽事件，这个事件需要鼠标键值[左键]的支持，同时也需要鼠标动作[移动]
 * 的支持，该动作无法在单次数据传输中完成鼠标按下和释放两个动作，所以鼠标释放仍然有存在意义。
 * 事实上，如果能够忍受网络延迟带来的影响，SLAVER和MASTER完全可以只用处理鼠标按下、鼠标释放、
 * 鼠标移动、鼠标滚动四个事件，不需要对这四个基本事件进行打包处理。
 */
public class MouseActionListener extends MouseAdapter implements IMouse {

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

    /**
     * 鼠标滚轮事件
     * @param e 源事件
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        /**设置滚轮、滚动的Size*/
        final MouseActionEvent mouseAction = new MouseActionEvent();
        mouseAction.setMouseAction(MouseActionEnum.WHEEL);
        int size = e.getScrollAmount() * e.getWheelRotation();
        mouseAction.setWheelSize(size);
        /**发送鼠标滚轮事件*/
        log.debug(MessageFormat.format("mouse wheel moved: {0}", mouseAction));
        this.sendMouseEvent(mouseAction);
    }

    /**
     * 鼠标拖拽事件
     * @param e 源事件
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        /**设置拖拽、位置*/
        final MouseActionEvent mouseAction = new MouseActionEvent();
        /**拖拽的键固定为左键*/
        mouseAction.setMouseButton(MouseButtonEnum.LEFT);
        mouseAction.setMouseAction(MouseActionEnum.DRAGGED);
        int[] site = new int[]{e.getX(),e.getY()};
        mouseAction.setSite(site);
        /**发送鼠标拖拽事件*/
        log.debug(MessageFormat.format("mouse dragged: {0}", mouseAction));
        this.sendMouseEvent(mouseAction);
    }

    /**
     * 鼠标释放事件
     * @param e
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        /**设置键、按下、位置*/
        final MouseActionEvent mouseAction = new MouseActionEvent();
        mouseAction.setMouseAction(MouseActionEnum.RELEASED);
        MouseButtonEnum buttonEnum = MouseActionEvent.convertButton(e.getButton());
        mouseAction.setMouseButton(buttonEnum);
        int[] site = new int[]{e.getX(),e.getY()};
        mouseAction.setSite(site);
        /**发送鼠标松开事件*/
        log.debug(MessageFormat.format("mouse released: {0}", mouseAction));
        this.sendMouseEvent(mouseAction);
    }

    /**
     * 鼠标移动事件
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        /**设置移动、位置*/
        final MouseActionEvent mouseAction = new MouseActionEvent();
        mouseAction.setMouseAction(MouseActionEnum.MOVE);
        int[] site = new int[]{e.getX(),e.getY()};
        mouseAction.setSite(site);
        /**发送鼠标移动事件*/
        log.debug(MessageFormat.format("mouse move: {0}", mouseAction));
        this.sendMouseEvent(mouseAction);
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
        /**设置左键、双击、位置*/
        final MouseActionEvent mouseAction = new MouseActionEvent();
        mouseAction.setMouseButton(MouseButtonEnum.LEFT);
        mouseAction.setMouseAction(MouseActionEnum.DOUBLE_CLICK);
        int[] site = new int[]{event.getX(),event.getY()};
        mouseAction.setSite(site);
        /**发送鼠标双击事件*/
        log.debug(MessageFormat.format("mouse double clicked: {0}", mouseAction));
        this.sendMouseEvent(mouseAction);
    }

    /**
     * 鼠标单击事件
     * @param event 源事件
     */
    private void mouseSingleClicked(MouseEvent event){
        /**事件发生后将记录器清零*/
        this.leftClickCounter.set(0);
        /**设置键、单击、位置*/
        final MouseActionEvent mouseAction = new MouseActionEvent();
        MouseButtonEnum buttonEnum = MouseActionEvent.convertButton(event.getButton());
        mouseAction.setMouseButton(buttonEnum);
        mouseAction.setMouseAction(MouseActionEnum.CLICK);
        int[] site = new int[]{event.getX(),event.getY()};
        mouseAction.setSite(site);
        /**发送鼠标双击事件*/
        log.debug(MessageFormat.format("mouse clicked: {0}", mouseAction));
        this.sendMouseEvent(mouseAction);
    }

    /**
     * 发送鼠标事件
     * @param e 源事件
     */
    @Override
    public void sendMouseEvent(MouseActionEvent e) {
        /**发送鼠标命令*/
        single.sendMouse(connectionId,e);
    }
}
