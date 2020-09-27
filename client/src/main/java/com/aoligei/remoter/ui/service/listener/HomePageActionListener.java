package com.aoligei.remoter.ui.service.listener;


import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.ui.form.HomePage;
import com.aoligei.remoter.ui.panel.RemotePanel;
import com.aoligei.remoter.ui.panel.Tab;
import com.aoligei.remoter.ui.service.action.IStart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

/**
 * @author wk-mia
 * 2020-9-26
 * 主页窗体事件监听
 * 包含：启动、关闭
 */
@Component
public class HomePageActionListener implements WindowListener, IStart {

    private static Logger log = LoggerFactory.getLogger(HomePage.class);

    /**窗体对象*/
    private HomePage form;
    /**选项卡*/
    private Tab tab;
    /**远程区面板*/
    private RemotePanel remotePanel;
    /**远程面板区事件监听*/
    private RemotePanelActionListener remoteListener;

    /**
     * 注入Bean
     * @param form 窗体对象
     * @param tab 选项卡对象
     * @param remotePanel 远程区面板
     * @param remoteListener 远程面板区事件监听
     */
    @Autowired
    HomePageActionListener(HomePage form,Tab tab,RemotePanel remotePanel,RemotePanelActionListener remoteListener){
        this.form = form;
        this.tab = tab;
        this.remotePanel = remotePanel;
        this.remoteListener = remoteListener;
    }


    /**
     * 启动窗体
     */
    @Override
    public void start() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                this.addListener();
                this.addPanel();
                /**
                 * 展示窗体
                 */
                form.setVisible(true);
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给面板中的控件及窗体注册监听器
     */
    private void addListener(){
        remotePanel.getConnectButton().setActionCommand(CommandEnum.CONNECT.name());
        remotePanel.getConnectButton().addActionListener(remoteListener);

        remotePanel.getControlButton().setActionCommand(CommandEnum.CONTROL.name());
        remotePanel.getControlButton().addActionListener(remoteListener);

        form.addWindowListener(this);
    }

    /**
     * 窗体添加面板
     */
    private void addPanel(){
        form.add(tab);
        tab.setComponentAt(0,remotePanel);
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
        log.info("******客户端实例 [ " + form.getTitle() + " ] 即将关闭");
        /**
         * 销毁实例
         */
        form.dispose();
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
}
