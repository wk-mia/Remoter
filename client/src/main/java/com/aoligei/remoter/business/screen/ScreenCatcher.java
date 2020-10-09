package com.aoligei.remoter.business.screen;

import com.aoligei.remoter.util.ImageUtil;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author wk-mia
 * 2020-10-9
 * 屏幕捕获器
 */
public class ScreenCatcher {

    /**
     *
     */
    private Robot robot;

    {
        try {
            robot = new Robot();
        }catch (AWTException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 捕获屏幕截图
     * @return 屏幕截图转换后的字节数组
     */
    public byte[] captureScreen(){
        /**屏幕分辨率*/
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        /**创建屏幕大小的区域*/
        Rectangle screenRectangle = new Rectangle(screenSize);
        /**截取屏幕*/
        BufferedImage bufferedImage = robot.createScreenCapture(screenRectangle);
        /**当前：按原图传输*/
        return ImageUtil.compress(bufferedImage,1);
    }

}
