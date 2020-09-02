package com.aoligei.remoter.util;

import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.BaseResponse;
import com.aoligei.remoter.netty.beans.CommandEnum;

/**
 * @author wk-mia
 * 2020-9-2
 * Netty消息体工具类
 */
public class BaseUtil {

    /**
     * 构建并返回一个Response
     * @param clientId 身份识别码
     * @param commandEnum 命令类型
     * @param data 数据
     * @param <T> Object对象
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponse(String clientId, Enum<CommandEnum> commandEnum, T data){
        BaseResponse baseResponse = BaseUtil.buildResponse(clientId,commandEnum,data,null);
        return baseResponse;
    }

    /**
     * 构建并返回一个Response
     * @param baseRequest 客户端的请求体
     * @param exceptionMessage 异常信息
     * @param <T> Object对象
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponse(BaseRequest baseRequest, String exceptionMessage){
        BaseResponse baseResponse = BaseUtil.buildResponse(baseRequest.getClientId(),baseRequest.getCommandEnum(),baseRequest.getData(),new NettyServerException(exceptionMessage));
        return baseResponse;
    }

    /**
     * 构建并返回一个Response
     * @param clientId 身份识别码
     * @param commandEnum 命令类型
     * @param data 数据
     * @param nettyServerException 异常
     * @param <T> Object对象
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponse(String clientId, Enum<CommandEnum> commandEnum, T data, NettyServerException nettyServerException){
        BaseResponse baseResponse = new BaseResponse(){{
            setClientId(clientId);
            setCommandEnum(commandEnum);
            setData(data);
            setNettyServerException(nettyServerException);
        }};
        return baseResponse;
    }
}
