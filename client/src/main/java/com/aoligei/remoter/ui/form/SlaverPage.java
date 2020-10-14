package com.aoligei.remoter.ui.form;

import com.aoligei.remoter.ui.panel.SlaverScreenPanel;
import javax.swing.*;

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
     */
    public SlaverPage(String title, SlaverScreenPanel panel){
        initStyle(title);
        addComponent(panel);
    }

    /**
     * 初始化窗体样式
     */
    private void initStyle(String title){
        this.setTitle(title);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(getOwner());
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    /**
     * 添加组件
     */
    private void addComponent(SlaverScreenPanel panel){
        this.panel = panel;
        this.add(panel);
    }

    public SlaverScreenPanel getPanel() {
        return panel;
    }
}
