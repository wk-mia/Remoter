package com.aoligei.remoter.beans;

import com.aoligei.remoter.enums.TerminalTypeEnum;

import java.io.Serializable;

/**
 * @author wk-mia
 * 2020-9-1
 * 用于Netty的自定义请求体
 * 客户端发起的都是BaseRequest，接受的都是BaseResponse
 */
public class BaseRequest extends BaseDataBody  implements Serializable {

    /**
     * 序列化常量
     */
    private static final long serialVersionUID = -3911255650485738676L;

    /**
     * 客户端编码
     */
    private String clientId;

    /**
     * 终端类型
     */
    private Enum<TerminalTypeEnum> terminalTypeEnum;


    /**
     * 重写BaseDataBody的toString()方法,将
     * 该方法的返回内容包装为请求体
     * @return BaseRequest对象的字符串输出
     */
    @Override
    public String toString() {
        return "BaseRequest{" +
                "connectionId='" + getConnectionId() + '\'' +
                ", clientId='" + clientId + '\'' +
                ", terminalTypeEnum=" + terminalTypeEnum +
                ", commandEnum=" + getCommandEnum() +
                ", data=" + getData() +
                '}';
    }

    public Enum<TerminalTypeEnum> getTerminalTypeEnum() {
        return terminalTypeEnum;
    }

    public void setTerminalTypeEnum(Enum<TerminalTypeEnum> terminalTypeEnum) {
        this.terminalTypeEnum = terminalTypeEnum;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
