package com.aoligei.remoter.enums;

/**
 * @author wk-mia
 * 2020-9-17
 * 发起请求检查项枚举
 */
public enum SponsorInspectEnum {
    /**
     * 客户端身份识别码
     */
    CLIENT_ID,
    /**
     * 客户端名称
     */
    CLIENT_NAME,
    /**
     * 客户端ip地址
     */
    CLIENT_IP,
    /**
     * 客户端是否已拒绝所有连接
     */
    IS_REJECT_CONNECTION;
    private SponsorInspectEnum(){}
}
