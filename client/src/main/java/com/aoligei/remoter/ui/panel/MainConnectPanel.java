package com.aoligei.remoter.ui.panel;

import com.aoligei.remoter.constant.RequestUrlConstants;
import com.aoligei.remoter.net.http.HttpRequestFactory;
import com.aoligei.remoter.ui.form.RemoteForm;
import com.aoligei.remoter.util.Result;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * @author wk-mia
 * 2020-8-31
 * 主窗体中远程连接区面板
 */
public class MainConnectPanel extends JPanel {

    /**
     * 面板构造函数
     */
    public MainConnectPanel(){
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
        //textField.setBounds(0,0,200,20);
        textField.setSize(200,30);
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setBackground(Color.WHITE);
        this.add(textField);
        /**
         * 连接按钮
         */
        JButton button = new JButton("连接");
        button.setFont(new Font("宋体",Font.PLAIN,12));
        button.setSize(60,30);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * 检查身份识别码是否合法
                 */
                HashMap hashMap = new HashMap(){{
                    put("name","zhangyang");
                    put("code","900");
                }};
                Result<String> result =
                HttpRequestFactory.get("localhost","60000", RequestUrlConstants.GET_CLIENT_ID,hashMap);
                if(result != null){

                }
            }
        });
        this.add(button);
    }

}
