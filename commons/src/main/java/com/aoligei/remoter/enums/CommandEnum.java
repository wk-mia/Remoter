package com.aoligei.remoter.enums;

/**
 * @author wk-mia
 * 2020-9-1
 * 传输实体对象中包含的所有命令枚举
 *
 * 对于控制端(Master):因为不涉及截取屏幕，所以不会用到SCREEN_SHOTS、VOICE_OUTPUT命令
 * 对于受控端(Slave):因为不涉及控制其他终端，所以不会用到CONTROL、KEYBOARD_INPUT、MOUSE_INPUT命令
 */
public enum CommandEnum {
    /**
     * 注册到服务器命令
     */
    REGISTER,
    /**
     * 连接服务器命令
     */
    CONNECT,

    /**
     * 断开与服务器的连接命令
     */
    DISCONNECT,

    /**
     * 远程控制的命令
     */
    CONTROL,

    /**
     * 停止远程控制的命令
     */
    STOP_CONTROL,

    /**
     * 发送心跳包时的命令
     */
    HEART_BEAT,

    /**
     * 发送屏幕截图时的命令
     */
    SCREEN_SHOTS,

    /**
     * 键盘输入命令
     */
    KEYBOARD_INPUT,

    /**
     * 鼠标输入命令
     */
    MOUSE_INPUT,

    /**
     * 声音输出命令
     */
    VOICE_OUTPUT,

    /**
     * 异常通知
     */
    EXCEPTION;

}
