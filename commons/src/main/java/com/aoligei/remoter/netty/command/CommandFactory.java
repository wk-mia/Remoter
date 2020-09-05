package com.aoligei.remoter.netty.command;

import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.CommandEnum;
import com.aoligei.remoter.netty.handler.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wk-mia
 * 2020-9-4
 * 命令分发工厂
 */
public class CommandFactory {

    /**
     * 处理器的缓存
     * 当请求到达时，先查缓存，缓存中没有再初始化相应的处理器
     */
    private static final Map<Enum<CommandEnum>,ICommandHandler> handlerCache = new ConcurrentHashMap<>();

    /**
     * 获取相应命令的处理器
     * @param command 命令
     * @return 处理器
     * @throws NettyServerException 异常信息
     */
    public static ICommandHandler getCommandHandler(Enum<CommandEnum> command) throws NettyServerException{
        if(command == null){
            return null;
        }else {
            if(handlerCache.get(command) == null){
                /**
                 * 加载处理器，放入缓存中
                 */
                return loadCommandHandler(command);
            }else {
                /**
                 * 从缓存中取处理器
                 */
                return handlerCache.get(command);
            }
        }
    }

    /**
     * 加载并返回一个命令处理器
     * @param command 命令
     * @return 处理器
     * @throws NettyServerException 异常信息
     */
    private static ICommandHandler loadCommandHandler(Enum<CommandEnum> command)throws NettyServerException{
        ICommandHandler commandHandler = null;
        switch(CommandEnum.valueOf(command.name())){
            case CONNECT:
                commandHandler = new ConnectCommandHandler();
                break;
            case CONTROL:
                commandHandler = new ControlCommandHandler();
                break;
            case HEART_BEAT:
                commandHandler = new HeartbeatCommandHandler();
                break;
            case SCREEN_SHOTS:
                commandHandler = new ScreenShotsCommandHandler();
                break;
            case VOICE_OUTPUT:
                commandHandler = new VoiceCommandHandler();
                break;
            case MOUSE_INPUT:
                commandHandler = new MouseCommandHandler();
                break;
            case KEYBOARD_INPUT:
                commandHandler = new KeyboardCommandHandler();
                break;
            case DISCONNECT:
                commandHandler = new DisConnectCommandHandler();
                break;
            default:
                throw new NettyServerException(ExceptionMessageConstants.UNKNOWN_COMMAND);
        }
        if(commandHandler != null){
            handlerCache.put(command,commandHandler);
        }
        return commandHandler;
    }
}
