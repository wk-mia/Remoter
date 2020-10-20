package com.aoligei.remoter.ui.service.listener;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.business.RequestProcessor;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.netty.NettyClient;
import com.aoligei.remoter.ui.form.DialogPage;
import com.aoligei.remoter.ui.service.action.IConnect;
import com.aoligei.remoter.ui.service.action.IControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author wk-mia
 * 2020-9-26
 * 远程区面板事件监听
 * 包含：连接、断开连接、远程控制等
 */
@Component
public class RemotePanelActionListener implements ActionListener, IConnect, IControl {

    /**
     * Netty客户端
     */
    @Autowired
    private NettyClient nettyClient;

    /**
     * 远程面板区控件的事件处理
     * @param e 事件
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        /**新开线程处理任务，防止界面假死*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                allotEvent(e);
            }
        }).start();
    }

    /**
     * 分发事件进行处理
     * @param e 事件
     */
    private void allotEvent(ActionEvent e){
        switch (CommandEnum.valueOf(e.getActionCommand())) {
            case CONNECT:
                connect();
                break;
            case DISCONNECT:
                disconnect();
                break;
            case CONTROL:
                control();
                break;
            case STOP_CONTROL:
                stopControl();
                break;
        }
    }

    /**
     * 连接服务器
     */
    @Override
    public void connect() {
        try{
            nettyClient.connect();
        }catch (Exception e){
            DialogPage.errorDialog("connect-error",e.getMessage());
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
        try{
            nettyClient.control("14927006-002");
        }catch (Exception e){
            DialogPage.errorDialog("control-error",e.getMessage());
        }
    }

    /**
     * 断开与服务器的连接
     */
    @Override
    public void stopControl() {
        try{
            nettyClient.stopControl("14927006-002");
        }catch (Exception e){
            DialogPage.errorDialog("dis-control-error",e.getMessage());
        }
    }

}
