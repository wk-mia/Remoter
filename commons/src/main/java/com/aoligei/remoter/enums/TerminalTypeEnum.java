package com.aoligei.remoter.enums;

/**
 * @author wk-mia
 * 2020-9-5
 * 终端类型
 */
public enum TerminalType {
    /**
     * 主控端
     */
    MASTER,
    /**
     * 受控端
     */
    SLAVE,
    /**
     * 服务器
     */
    SERVER;
    private TerminalType(){};
}
