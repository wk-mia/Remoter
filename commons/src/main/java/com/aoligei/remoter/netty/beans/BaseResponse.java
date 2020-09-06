package com.aoligei.remoter.netty.beans;

import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.NettyServerException;

/**
 * @author wk-mia
 * 2020-9-1
 * 用于Netty的自定义请求响应体
 * 服务端接受的都是BaseRequest，通过服务器的处理后封装成BaseResponse并返回
 */
public class BaseResponse extends BaseDataBody {

    /**
     * 终端类型，固定值为SERVER
     */
    private Enum<TerminalTypeEnum> terminalTypeEnum = TerminalTypeEnum.valueOf("SERVER");

    /**
     * 服务器处理时发生的异常
     */
    private NettyServerException nettyServerException;

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
                ", nettyServerException=" + nettyServerException +
                ", commandEnum=" + getCommandEnum() +
                ", data=" + getData() +
                '}';
    }

    public NettyServerException getNettyServerException() {
        return nettyServerException;
    }

    public void setNettyServerException(NettyServerException nettyServerException) {
        this.nettyServerException = nettyServerException;
    }

    public Enum<TerminalTypeEnum> getTerminalTypeEnum() {
        return terminalTypeEnum;
    }

    public void setTerminalTypeEnum(Enum<TerminalTypeEnum> terminalTypeEnum) {
        this.terminalTypeEnum = terminalTypeEnum;
    }
}
