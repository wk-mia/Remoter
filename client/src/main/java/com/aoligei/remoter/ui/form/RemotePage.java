package com.aoligei.remoter.ui.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author wk-mia
 * 2020-8-28
 * 远程控制窗体的定义
 */
public class RemotePage extends JFrame implements WindowListener {

    private static Logger log = LoggerFactory.getLogger(RemotePage.class);

    /**
     * 窗体定义
     * @param title 窗体名称
     */
    public RemotePage(String title){
        this.setTitle(title);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(getOwner());
        this.setVisible(true);
        this.addWindowListener(this);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    /**
     * 从系统菜单中关闭窗体时调用
     * @param e
     */
    @Override
    public void windowClosing(WindowEvent e) {
        /**
         * 客户端实例销毁日志
         */
        log.info("******远程实例 [ " + this.getTitle() + " ] 即将关闭连接");
        /**
         * 销毁实例
         */
        this.dispose();
    }

    /**
     * 窗口首次打开时调用
     * @param e
     */
    @Override
    public void windowOpened(WindowEvent e) {

    }

    /**
     * 关闭窗体时调用  API: 因对窗口调用 dispose 而将其关闭时调用
     * @param e
     */
    @Override
    public void windowClosed(WindowEvent e) {

    }

    /**
     * 窗体变为最小化时调用
     * @param e
     */
    @Override
    public void windowIconified(WindowEvent e) {

    }

    /**
     * 窗体变为正常状态时调用
     * @param e
     */
    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    /**
     * 窗体被激活时调用
     * @param e
     */
    @Override
    public void windowActivated(WindowEvent e) {

    }

    /**
     * 当窗口不再是活动Window时调用
     * @param e
     */
    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
