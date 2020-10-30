package com.aoligei.remoter.ui.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * @author wk-mia
 * 2020-10-14
 * 远程屏幕面板
 */
public class SlaverScreenPanel extends JPanel {

    /**屏幕截图*/
    private BufferedImage image;

    public SlaverScreenPanel(int width, int height){
        initStyle(width,height);
    }

    /**
     * 设置面板样式
     * @param width 宽
     * @param height 高
     */
    private void initStyle(int width, int height){
        this.setPreferredSize(new Dimension(width,height));
    }

    /**
     * 设置屏幕截图
     * @param image 屏幕
     */
    public void setImage(BufferedImage image){
        this.image = image;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(image != null){
            g.drawImage(image,0,0,null);
        }
    }
}
