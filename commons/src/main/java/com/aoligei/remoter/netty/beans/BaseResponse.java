package com.aoligei.remoter.netty.beans;

import com.aoligei.remoter.exception.NettyServerException;

/**
 * @author wk-mia
 * 2020-9-1
 * 用于Netty的自定义请求响应体
 */
public class BaseResponse extends BaseDataBody {

    /**
     * Netty网络传输中发生的异常
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
                "clientId='" + getClientId() + '\'' +
                ", clientName='" + getClientName() + '\'' +
                ", commandEnum=" + getCommandEnum() +
                ", data=" + getData() +
                ", exception=" + this.nettyServerException +
                '}';
    }

    public NettyServerException getNettyServerException() {
        return nettyServerException;
    }

    public void setNettyServerException(NettyServerException nettyServerException) {
        this.nettyServerException = nettyServerException;
    }

}
