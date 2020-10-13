package com.aoligei.remoter.ui.service.listener;

import com.aoligei.remoter.ui.form.RemotePage;
import com.aoligei.remoter.ui.service.action.IInteract;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * @author wk-mia
 * 2020-10-13
 * 远程窗口事件监听
 * 包含：启动、关闭
 */
public class RemotePageActionListener implements WindowListener, IInteract {

    /**
     * 启动远程窗口
     * @param pageTitle 窗口标题
     */
    @Override
    public void call(String pageTitle) {
        RemotePage page = new RemotePage(pageTitle);
        page.setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
