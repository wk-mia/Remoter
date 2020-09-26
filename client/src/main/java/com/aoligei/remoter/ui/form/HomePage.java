package com.aoligei.remoter.ui.form;


import org.springframework.stereotype.Component;
import javax.swing.*;

/**
 * @author wk-mia
 * 2020-8-28
 * 主窗体的定义
 */
@Component
public class HomePage extends JFrame {

    /**
     * 窗体定义
     */
    public HomePage(){
        initStyle();
    }

    /**
     * 初始化窗体样式
     */
    public void initStyle(){
        this.setTitle("Remoter");
        this.setSize(1200,800);
        this.setLocationRelativeTo(getOwner());
        this.setResizable(false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }


}
