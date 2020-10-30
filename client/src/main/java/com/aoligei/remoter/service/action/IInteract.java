package com.aoligei.remoter.service.action;

/**
 * @author wk-mia
 * 2020-10-13
 * 主控端的远程工作，与受控端的互动
 */
public interface IInteract {

    /**
     * 调出远程窗口
     * @param pageTitle 窗口标题
     * @param screenWidth 屏幕截图宽度
     * @param screenHeight 屏幕截图高度
     */
    void call(String pageTitle, int screenWidth, int screenHeight);

    /**
     * 发出键盘指令
     */

    /**
     * 发出鼠标指令
     */

    /**
     * 播放屏幕截图
     * @param connectionId 连接编码
     * @param bytes 屏幕截图字节流
     */
    void play(String connectionId,byte[] bytes);

    /**
     * 播放声音
     */

    /**
     * 关闭远程控制
     * @param connectionId
     */
    void closeControl(String connectionId);
}
