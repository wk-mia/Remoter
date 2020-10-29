package com.aoligei.remoter.event;

import java.awt.event.MouseEvent;

/**
 * @author wk-mia
 * 2020-10-27
 * 鼠标事件
 */
public class MouseActionEvent {

    /**
     * 鼠标键
     */
    private MouseButtonEnum mouseButton;

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
     * 鼠标键值转换
     * @param button 鼠标键值
     * @return MouseButtonEnum类型
     */
    public static MouseButtonEnum convertButton(int button){
        switch (button){
            case MouseEvent.BUTTON1:
                return MouseButtonEnum.LEFT;
            case MouseEvent.BUTTON2:
                return MouseButtonEnum.MIDDLE;
            case MouseEvent.BUTTON3:
                return MouseButtonEnum.RIGHT;
            default:
                throw new RuntimeException("not supported mouse button code");
        }
    }

    /**
     * 鼠标键值枚举
     */
    public enum MouseButtonEnum{
        /**鼠标左键*/
        LEFT(MouseEvent.BUTTON1_DOWN_MASK),
        /**鼠标中键*/
        MIDDLE(MouseEvent.BUTTON2_DOWN_MASK),
        /**鼠标右键*/
        RIGHT(MouseEvent.BUTTON3_DOWN_MASK);
        private int code;
        private MouseButtonEnum(int code){ this.code = code; }
        /**获取值*/
        public int getCode(){return this.code;}
    }

    /**
     * 鼠标动作枚举
     */
    public enum MouseActionEnum{
        /**鼠标单击*/
        CLICK(1001),
        /**鼠标双击*/
        DOUBLE_CLICK(1002),
        /**鼠标按下*/
        PRESSED(1003),
        /**鼠标松开*/
        RELEASED(1004),
        /**鼠标拖拽*/
        DRAGGED(1005),
        /**鼠标滚轮*/
        WHEEL(1006),
        /**鼠标移动*/
        MOVE(1007);
        private int code;
        private MouseActionEnum(int code){ this.code = code; }
    }

    public MouseButtonEnum getMouseButton() {
        return mouseButton;
    }

    public void setMouseButton(MouseButtonEnum mouseButton) {
        this.mouseButton = mouseButton;
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
        final StringBuffer sb = new StringBuffer("MouseActionEvent{");
        sb.append("mouseButton=").append(mouseButton);
        sb.append(", mouseAction=").append(mouseAction);
        sb.append(", site=");
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
