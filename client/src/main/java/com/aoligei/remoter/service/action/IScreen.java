package com.aoligei.remoter.service.action;

/**
 * @author wk-mia
 * 2020-10-13
 * 主控端的屏幕捕获
 */
public interface IScreen {

    /**
     * 捕获屏幕截图
     * @return 屏幕截图转换后的字节数组
     */
    byte[] captureScreen();
}
