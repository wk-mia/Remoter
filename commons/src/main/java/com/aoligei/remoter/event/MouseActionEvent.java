package com.aoligei.remoter.event;

import java.awt.event.MouseEvent;

/**
 * @author wk-mia
 * 2020-10-27
 * 鼠标事件
 */
public class MouseActionEvent {

    /**
     * 鼠标动作
     */
    private MouseActionEnum mouseAction;

    /**
     * 鼠标位置
     */
    private int[] site = new int[2];

    /**
     * 滚轮滚动的大小
     */
    private int wheelSize;

    /**
     * 事件转换
     * @param button 事件值
     * @return MouseActionEnum类型
     */
    public static MouseActionEnum convert(int button){
        switch (button){
            case MouseEvent.BUTTON1:
                return MouseActionEnum.LEFT;
            case MouseEvent.BUTTON2:
                return MouseActionEnum.MIDDLE;
            case MouseEvent.BUTTON3:
                return MouseActionEnum.RIGHT;
            case -1:
                return MouseActionEnum.DOUBLE_CLICK;
            case MouseEvent.MOUSE_DRAGGED:
                return MouseActionEnum.DRAGGED;
            case MouseEvent.MOUSE_MOVED:
                return MouseActionEnum.MOVE;
            case MouseEvent.MOUSE_WHEEL:
                return MouseActionEnum.WHEEL;
            default:
                throw new RuntimeException("not supported mouse action code");
        }
    }

    /**
     * 值转换
     * @param mouseAction MouseActionEnum
     * @return 事件值
     */
    public static int deConvert(MouseActionEnum mouseAction){
        return mouseAction.code;
    }

    /**
     * 鼠标动作枚举
     */
    public enum MouseActionEnum{
        /**鼠标拖拽*/
        DRAGGED(MouseEvent.MOUSE_DRAGGED),
        /**鼠标左键*/
        LEFT(MouseEvent.BUTTON1),
        /**鼠标中键*/
        MIDDLE(MouseEvent.BUTTON2),
        /**鼠标右键*/
        RIGHT(MouseEvent.BUTTON3),
        /**鼠标滚轮*/
        WHEEL(MouseEvent.MOUSE_WHEEL),
        /**鼠标双击*/
        DOUBLE_CLICK(-1),
        /**鼠标移动*/
        MOVE(MouseEvent.MOUSE_MOVED);
        private int code;
        private MouseActionEnum(int code){ this.code = code; }
    }

    public MouseActionEnum getMouseAction() {
        return mouseAction;
    }

    public void setMouseAction(MouseActionEnum mouseAction) {
        this.mouseAction = mouseAction;
    }

    public int[] getSite() {
        return site;
    }

    public void setSite(int[] site) {
        this.site = site;
    }

    public int getWheelSize() {
        return wheelSize;
    }

    public void setWheelSize(int wheelSize) {
        this.wheelSize = wheelSize;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("MouseEvent{");
        sb.append("mouseAction=").append(mouseAction);
        sb.append(", location=");
        if (site == null) {
            sb.append("null");
        } else {
            sb.append('[');
            for (int i = 0; i < site.length; ++i) {
                sb.append(i == 0 ? "" : ", ").append(site[i]);
            }
            sb.append(']');
        }
        sb.append(", wheelSize=").append(wheelSize);
        sb.append('}');
        return sb.toString();
    }
}
