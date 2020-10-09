package com.aoligei.remoter.util;

import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.ResponseStatusEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.beans.*;
import io.netty.channel.Channel;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * @author wk-mia
 * 2020-9-2
 * Netty消息体工具类
 */
public class BuildUtil {

    /**
     * 构建并返回一个处理成功的Response
     * @param connectionId 连接编码
     * @param terminalTypeEnum 终端类型
     * @param commandEnum 命令类型
     * @param data 数据
     * @param <T> Object对象
     * @param message 处理结果描述
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponseOK(String connectionId, Enum<TerminalTypeEnum> terminalTypeEnum, Enum<CommandEnum> commandEnum, T data,String message){
        BaseResponse baseResponse = BuildUtil.buildResponse(connectionId,terminalTypeEnum,commandEnum,data, ResponseStatusEnum.OK,message);
        return baseResponse;
    }

    /**
     * 构建并返回一个处理失败的Response
     * @param connectionId 连接编码
     * @param terminalTypeEnum 终端类型
     * @param commandEnum 命令类型
     * @param data 数据
     * @param <T> Object对象
     * @param message 处理结果描述
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponseFAIL(String connectionId, Enum<TerminalTypeEnum> terminalTypeEnum, Enum<CommandEnum> commandEnum, T data,String message){
        BaseResponse baseResponse = BuildUtil.buildResponse(connectionId,terminalTypeEnum,commandEnum,data, ResponseStatusEnum.FAIL,message);
        return baseResponse;
    }

    /**
     * 构建并返回一个处理异常的Response
     * @param connectionId 连接编码
     * @param terminalTypeEnum 终端类型
     * @param commandEnum 命令类型
     * @param data 数据
     * @param <T> Object对象
     * @param message 处理结果描述
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponseERROR(String connectionId, Enum<TerminalTypeEnum> terminalTypeEnum, Enum<CommandEnum> commandEnum, T data,String message){
        BaseResponse baseResponse = BuildUtil.buildResponse(connectionId,terminalTypeEnum,commandEnum,data, ResponseStatusEnum.ERROR,message);
        return baseResponse;
    }


    /**
     * 构建并返回一个Response
     * @param connectionId 连接编码
     * @param terminalTypeEnum 终端类型
     * @param commandEnum 命令类型
     * @param data 数据
     * @param responseStatusEnum 状态
     * @param message 处理结果描述
     * @param <T> Object对象
     * @return BaseResponse对象
     */
    public static <T> BaseResponse buildResponse(String connectionId, Enum<TerminalTypeEnum> terminalTypeEnum, Enum<CommandEnum> commandEnum, T data,
                                                 ResponseStatusEnum responseStatusEnum, String message){
        BaseResponse baseResponse = new BaseResponse(){{
            setConnectionId(connectionId);
            setTerminalTypeEnum(terminalTypeEnum);
            setCommandEnum(commandEnum);
            setData(data);
            setStatus(responseStatusEnum);
            setMessage(message);
        }};
        return baseResponse;
    }

    /**
     * 构建并返回一个MetaCache
     * @param clientId 身份识别码
     * @param channel 通道
     * @return OnlineElement对象
     */
    public static OnlineElement buildMetaCache(String clientId, Channel channel, TerminalTypeEnum terminalTypeEnum){
        return new OnlineElement(clientId,channel,terminalTypeEnum);
    }
}
