package com.aoligei.remoter.netty.beans;

/**
 * @author wk-mia
 * 2020-9-1
 * 用于Netty的自定义请求体
 * 客户端发起的都是BaseRequest，接受的都是BaseResponse
 */
public class BaseRequest extends BaseDataBody {

    /**
     * 重写BaseDataBody的toString()方法,将
     * 该方法的返回内容包装为请求体
     * @return BaseRequest对象的字符串输出
     */
    @Override
    public String toString() {
        return "BaseRequest{" +
                "clientId='" + getClientId() + '\'' +
                ", targetClientIds=" + getTargetClientIds() +
                ", commandEnum=" + getCommandEnum() +
                ", data=" + getData() +
                '}';
    }
}
