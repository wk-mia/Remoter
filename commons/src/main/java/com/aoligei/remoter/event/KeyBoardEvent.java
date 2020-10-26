package com.aoligei.remoter.event;


/**
 * @author wk-mia
 * 2020-10-23
 * 键盘事件
 */
public class KeyBoardEvent {

    /**键值*/
    private int keyCode;
    /**键状态：false-释放，true-按下*/
    private boolean keyStatus;

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean getKeyStatus() {
        return keyStatus;
    }

    public void setKeyStatus(boolean keyStatus) {
        this.keyStatus = keyStatus;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("KeyBoardEvent{");
        sb.append("keyCode=").append(keyCode);
        sb.append(", keyStatus=").append(keyStatus);
        sb.append('}');
        return sb.toString();
    }
}
