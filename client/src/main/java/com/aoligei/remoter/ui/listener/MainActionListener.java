package com.aoligei.remoter.ui.listener;

import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.netty.NettyClient;
import com.aoligei.remoter.ui.IHomePage;
import com.aoligei.remoter.ui.form.DialogForm;
import com.aoligei.remoter.ui.form.MainForm;
import com.aoligei.remoter.ui.panel.MainConnectPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * 主页事件监听
 * 包含：注册、连接、远程控制、断开连接等事件的处理
 */
@Component
public class MainActionListener implements ActionListener, IHomePage {

    /**
     * 窗体对象
     */
    @Autowired
    private MainForm form;

    /**
     * 面板对象
     */
    @Autowired
    private MainConnectPanel mainConnectPanel;

    /**
     * Netty客户端
     */
    @Autowired
    private NettyClient nettyClient;

    /**
     * 事件处理
     * @param actionEvent
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        switch (CommandEnum.valueOf(actionEvent.getActionCommand())) {
            case REGISTER:
                register();
                break;
            case CONNECT:
                connect();
                break;
            case CONTROL:
                control();
                break;
            case DISCONNECT:

                break;
        }
    }

    /**
     * 启动窗体
     */
    @Override
    public void start() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                /**
                 * 面板中的按钮注册监听器
                 */
                mainConnectPanel.getConnectButton().setActionCommand(CommandEnum.CONNECT.name());
                mainConnectPanel.getConnectButton().addActionListener(this);
                /**
                 * 窗体添加面板
                 */
                form.add(mainConnectPanel);
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
     * 向服务器注册
     */
    @Override
    public void register() {

    }

    /**
     * 连接服务器
     */
    @Override
    public void connect() {
        try{
            nettyClient.connect();
            DialogForm.informationDialog("connect-ok","connect to server request ok");
        }catch (Exception e){
            DialogForm.errorDialog("connect-error",e.getMessage());
        }
    }

    /**
     * 断开与服务器的连接
     */
    @Override
    public void disconnect() {

    }

    /**
     * 向服务器发起控制请求
     */
    @Override
    public void control() {

    }

    /**
     * 断开与服务器的连接
     */
    @Override
    public void stopControl() {

    }
}
