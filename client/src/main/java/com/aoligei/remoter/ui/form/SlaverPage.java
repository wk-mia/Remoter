package com.aoligei.remoter.ui.form;

import com.aoligei.remoter.ui.panel.SlaverScreenPanel;
import javax.swing.*;
import java.awt.*;

/**
 * @author wk-mia
 * 2020-8-28
 * 远程控制窗体的定义
 */
public class SlaverPage extends JFrame{

    /**远程屏幕面板*/
    private SlaverScreenPanel panel;

    /**
     * 窗体定义
     * @param title 窗体名称
     * @param panel 远程屏幕面板
     * @param frameWidth 页面宽度
     * @param frameHeight 页面高度
     */
    public SlaverPage(String title, SlaverScreenPanel panel, int frameWidth, int frameHeight){
        this.panel = panel;
        /**预留出窗体标题栏、滚动条的空间，让远程截图铺满页面*/
        initStyle(title,frameWidth + 40,frameHeight + 70);
        addComponent(panel);
    }

    /**
     * 初始化窗体样式
     * @param title 窗体名称
     * @param frameWidth 页面宽度
     * @param frameHeight 页面高度
     */
    private void initStyle(String title, int frameWidth, int frameHeight){
        this.setTitle(title);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(getOwner());
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setPreferredSize(new Dimension(frameWidth,frameHeight));
        this.pack();
    }

    /**
     * 添加组件
     */
    private void addComponent(SlaverScreenPanel panel){
        /**滚动条：始终显示*/
        JScrollPane scroll = new JScrollPane(panel);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        /**面板加入JFrame中*/
        this.getContentPane().add(scroll);
    }

    public SlaverScreenPanel getPanel() {
        return panel;
    }
}
