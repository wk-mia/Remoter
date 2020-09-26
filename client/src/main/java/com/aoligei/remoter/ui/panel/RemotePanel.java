package com.aoligei.remoter.ui.panel;


import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

/**
 * @author wk-mia
 * 2020-8-31
 * 远程连接区面板
 */
@Component
public class RemotePanel extends JPanel {

    /**
     * 连接按钮
     */
    private JButton connectButton;

    /**
     * 面板构造函数
     */
    public RemotePanel(){
        this.setBackground(Color.WHITE);
        this.setSize(300,200);
        /**
         * 添加控件
         */
        this.addComponent();
    }

    /**
     * 给当前面板添加组件
     */
    private void addComponent(){
        /**
         * 身份识别码文本框
         */
        JTextField textField = new JTextField(36);
        textField.setFont(new Font("宋体",Font.PLAIN,12));
        textField.setSize(200,30);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBackground(Color.WHITE);
        this.add(textField);
        /**
         * 连接按钮
         */
        connectButton = new JButton("连接");
        connectButton.setFont(new Font("宋体",Font.PLAIN,12));
        connectButton.setSize(60,30);
        this.add(connectButton);
    }

    /**
     * 获取连接按钮
     */
    public JButton getConnectButton() {
        return connectButton;
    }
}
