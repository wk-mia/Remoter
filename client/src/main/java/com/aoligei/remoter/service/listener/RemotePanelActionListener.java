package com.aoligei.remoter.service.listener;

import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.manage.SingleTaskManage;
import com.aoligei.remoter.ui.form.DialogPage;
import com.aoligei.remoter.service.action.IConnect;
import com.aoligei.remoter.service.action.IControl;
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

    /**单次任务管理器*/
    private SingleTaskManage single;
    @Autowired
    public RemotePanelActionListener(SingleTaskManage single){
        this.single = single;
    }

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
            single.startConnect();
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
            single.startControl("14927006-002");
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
            single.stopControl("14927006-002", TerminalTypeEnum.MASTER);
        }catch (Exception e){
            DialogPage.errorDialog("dis-control-error",e.getMessage());
        }
    }

}
