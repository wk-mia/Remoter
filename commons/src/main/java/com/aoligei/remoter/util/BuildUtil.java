package com.aoligei.remoter.util;

import com.aoligei.remoter.dto.ClientInformation;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.netty.beans.*;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

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
        BaseResponse baseResponse = BuildUtil.buildResponse(baseRequest.getConnectionId(),baseRequest.getTerminalTypeEnum(),baseRequest.getCommandEnum(),baseRequest.getData(),new ServerException(exceptionMessage));
        return baseResponse;
    }

    /**
     * 构建并返回一个Response
     * @param connectionId 连接编码
     * @param terminalTypeEnum 终端类型
     * @param commandEnum 命令类型
     * @param data 数据
     * @param serverException 异常
     * @param <T> Object对象
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponse(String connectionId,Enum<TerminalTypeEnum> terminalTypeEnum, Enum<CommandEnum> commandEnum, T data, ServerException serverException){
        BaseResponse baseResponse = new BaseResponse(){{
            setConnectionId(connectionId);
            setTerminalTypeEnum(terminalTypeEnum);
            setCommandEnum(commandEnum);
            setData(data);
            setServerException(serverException);
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

    /**
     * 构建并返回一个ClientInformation
     * @param clientSystemType 客户端类型
     * @param clientName 客户端名称
     * @param clientId 客户端身份识别码
     * @param clientIp 客户端IP
     * @param clientPort 客户端远程端口
     * @param isRejectAllConnections 此客户端是否已拒绝所有连接请求
     * @param channel 连接通道
     * @param scheduledFuture 监听任务
     * @param terminalTypeEnum 终端类型
     * @return
     */
    public static ClientInformation buildClientInfo(int clientSystemType, String clientName, String clientId, String clientIp, Integer clientPort, Boolean isRejectAllConnections, Channel channel, ScheduledFuture scheduledFuture, TerminalTypeEnum terminalTypeEnum){
        return new ClientInformation(clientSystemType,clientName,clientId,clientIp,clientPort,isRejectAllConnections,channel,scheduledFuture,terminalTypeEnum);
    }
}
