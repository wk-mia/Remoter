package com.aoligei.remoter.util;

import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.*;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.List;

/**
 * @author wk-mia
 * 2020-9-2
 * Netty消息体工具类
 */
public class BuildUtil {

    /**
     * 构建并返回一个Response
     * @param connectionId 连接编码
     * @param terminalTypeEnum 终端类型
     * @param commandEnum 命令类型
     * @param data 数据
     * @param <T> Object对象
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponse(String connectionId, Enum<TerminalTypeEnum> terminalTypeEnum, Enum<CommandEnum> commandEnum, T data){
        BaseResponse baseResponse = BuildUtil.buildResponse(connectionId,terminalTypeEnum,commandEnum,data,null);
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
        BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getConnectionId(),baseRequest.getTerminalTypeEnum(),baseRequest.getCommandEnum(),baseRequest.getData(),new NettyServerException(exceptionMessage));
        return baseResponse;
    }

    /**
     * 构建并返回一个Response
     * @param connectionId 连接编码
     * @param terminalTypeEnum 终端类型
     * @param commandEnum 命令类型
     * @param data 数据
     * @param nettyServerException 异常
     * @param <T> Object对象
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponse(String connectionId,Enum<TerminalTypeEnum> terminalTypeEnum, Enum<CommandEnum> commandEnum, T data, NettyServerException nettyServerException){
        BaseResponse baseResponse = new BaseResponse(){{
            setConnectionId(connectionId);
            setTerminalTypeEnum(terminalTypeEnum);
            setCommandEnum(commandEnum);
            setData(data);
            setNettyServerException(nettyServerException);
        }};
        return baseResponse;
    }

    /**
     * 构建并返回一个MetaCache
     * @param clientId 身份识别码
     * @param channel 通道
     * @param scheduledFuture 监听任务
     * @return MetaCache对象
     */
    public static MetaCache buildMetaCache(String clientId, Channel channel, ScheduledFuture scheduledFuture, TerminalTypeEnum terminalTypeEnum){
        return new MetaCache(clientId,channel,scheduledFuture,terminalTypeEnum);
    }
}
