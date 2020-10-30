package com.aoligei.remoter.event;

/**
 * @author wk-mia
 * 2020-10-30
 * 远程控制应答事件
 */
public class ControlEvent {

    /**
     * 受控端是否同意远程请求
     */
    private boolean accepted = false;

    /**
     * 受控端屏幕宽度
     */
    private int screenWidth;

    /**
     * 受控端屏幕高度
     */
    private int screenHeight;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ControlEvent{");
        sb.append("accepted=").append(accepted);
        sb.append(", screenWidth=").append(screenWidth);
        sb.append(", screenHeight=").append(screenHeight);
        sb.append('}');
        return sb.toString();
    }
}
