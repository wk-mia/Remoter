package com.aoligei.remoter.ui.form;

import javax.swing.*;

/**
 * @author wk-mia
 * 2020-8-31
 * 常用的弹窗类
 */
public class DialogForm {

    /**
     * 警告信息弹窗
     * @param title 标题
     * @param message 消息
     */
    public static void warningDialog(String title,String message){
        JOptionPane.showMessageDialog(null, message, title,JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 错误信息弹窗
     * @param title 标题
     * @param message 消息
     */
    public static void errorDialog(String title,String message){
        JOptionPane.showMessageDialog(null, message, title,JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 询问信息弹窗
     * @param title 标题
     * @param message 消息
     */
    public static void questionDialog(String title,String message){
        JOptionPane.showMessageDialog(null, message, title,JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * 纯信息弹窗
     * @param title 标题
     * @param message 消息
     */
    public static void informationDialog(String title,String message){
        JOptionPane.showMessageDialog(null, message, title,JOptionPane.INFORMATION_MESSAGE);
    }


}
