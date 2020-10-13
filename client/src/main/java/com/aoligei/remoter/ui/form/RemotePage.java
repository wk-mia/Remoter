package com.aoligei.remoter.ui.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author wk-mia
 * 2020-8-28
 * 远程控制窗体的定义
 */
public class RemotePage extends JFrame{

    private static Logger log = LoggerFactory.getLogger(RemotePage.class);

    /**
     * 窗体定义
     * @param title 窗体名称
     */
    public RemotePage(String title){
        initStyle(title);
    }

    /**
     * 初始化窗体样式
     */
    public void initStyle(String title){
        this.setTitle(title);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(getOwner());
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
