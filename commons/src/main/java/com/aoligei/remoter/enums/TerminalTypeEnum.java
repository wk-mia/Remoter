package com.aoligei.remoter.enums;

/**
 * @author wk-mia
 * 2020-9-5
 * 终端类型
 */
public enum TerminalTypeEnum {
    /**
     * 主控端-服务器
     */
    MASTER,
    /**
     * 受控端-服务器
     */
    SLAVE,
    /**
     * 服务器-受控端/主控端
     */
    SERVER,
    /**
     * 服务器-主控端
     */
    SERVER_2_MASTER,
    /**
     * 服务器-受控端
     */
    SERVER_2_SLAVE;
    private TerminalTypeEnum(){};
}
