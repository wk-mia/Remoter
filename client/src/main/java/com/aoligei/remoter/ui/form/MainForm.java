package com.aoligei.remoter.ui.form;

import com.aoligei.remoter.ui.IHomePage;
import com.aoligei.remoter.ui.panel.MainConnectPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

/**
 * @author wk-mia
 * 2020-8-28
 * 主窗体的定义
 */
public class MainForm extends JFrame implements IHomePage,WindowListener {

    private static Logger log = LoggerFactory.getLogger(MainForm.class);

    /**
     * 窗体定义
     * @param title 窗体名称
     */
    public MainForm(String title){
        this.setTitle(title);
        initStyle();
    }

    /**
     * 初始化窗体样式
     */
    public void initStyle(){
        this.setSize(600,400);
        this.setLocationRelativeTo(getOwner());
        this.setResizable(false);
        this.addWindowListener(this);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.getContentPane().add(new MainConnectPanel());
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
        log.info("******客户端实例 [ " + this.getTitle() + " ] 即将关闭");
        /**
         * 销毁实例
         */
        this.dispose();
        /**
         * 程序退出日志
         */
        log.info("******程序即将结束");
        /**
         * 程序退出
         */
        System.exit(0);
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

    @Override
    public void start() {
        try {
            /**
             * 展示窗体
             */
            SwingUtilities.invokeAndWait(() -> {
                setVisible(true);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register() {

    }

    @Override
    public void connect() {

    }

    @Override
    public void control() {

    }
}
