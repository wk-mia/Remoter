package com.aoligei.remoter.beans;

import com.aoligei.remoter.enums.ResponseStatusEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;

import java.io.Serializable;

/**
 * @author wk-mia
 * 2020-9-1
 * 用于Netty的自定义请求响应体
 * 服务端接受的都是BaseRequest，通过服务器的处理后封装成BaseResponse并返回
 */
public class BaseResponse extends BaseDataBody implements Serializable {

    /**
     * 序列化常量
     */
    private static final long serialVersionUID = -3911255650485738676L;
    /**
     * 终端类型，固定值为SERVER
     */
    private Enum<TerminalTypeEnum> terminalTypeEnum = TerminalTypeEnum.valueOf("SERVER");
    /**
     * 服务器响应的状态
     */
    private ResponseStatusEnum status;
    /**
     * 服务器给出的消息
     */
    private String message;

    /**
     * 重写BaseDataBody的toString()方法,将
     * 该方法的返回内容包装为请求响应体
     * @return BaseResponse对象的字符串输出
     */
    @Override
    public String toString() {
        return "BaseResponse{" +
                "connectionId='" + getConnectionId() + '\'' +
                "terminalTypeEnum=" + terminalTypeEnum +
                ", status=" + status +
                ", message=" + message +
                ", commandEnum=" + getCommandEnum() +
                ", data=" + getData() +
                '}';
    }

    public ResponseStatusEnum getStatus() {
        return status;
    }

    public void setStatus(ResponseStatusEnum status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Enum<TerminalTypeEnum> getTerminalTypeEnum() {
        return terminalTypeEnum;
    }

    public void setTerminalTypeEnum(Enum<TerminalTypeEnum> terminalTypeEnum) {
        this.terminalTypeEnum = terminalTypeEnum;
    }
}
