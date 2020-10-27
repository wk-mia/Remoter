package com.aoligei.remoter.command;


import com.aoligei.remoter.constant.HandlerLoadConstants;
import com.aoligei.remoter.exception.HandlerLoadException;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.util.SpringBeanUtil;
import org.springframework.util.StringUtils;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wk-mia
 * 2020-9-4
 * 命令分发工厂
 */
public class CommandFactory {

    /**
     * handler配置路径
     */
    private static final String COMMAND_HANDLER_PATH = "handler/commandHandlers";

    /**
     * sponsor配置路径
     */
    private static final String COMMAND_SPONSOR_PATH = "handler/commandSponsors";

    /**
     * 接收命令处理器的缓存
     * 当请求到达时，先查缓存，缓存中没有再初始化相应的处理器
     */
    private static final ConcurrentHashMap<Enum<CommandEnum>,ICommandHandler> handlerCache = new ConcurrentHashMap<>();

    /**
     * 发起命令处理器的缓存
     * 当请求到达时，先查缓存，缓存中没有再初始化相应的处理器
     */
    private static final ConcurrentHashMap<Enum<CommandEnum>,ICommandSponsor> sponsorCache = new ConcurrentHashMap<>();

    /**
     * 获取相应发起命令的处理器
     * @param command 命令
     * @return 处理器
     * @throws HandlerLoadException 异常信息
     */
    public static ICommandSponsor getCommandSponsor(Enum<CommandEnum> command) throws HandlerLoadException{
        if(command == null){
            return null;
        }else {
            ICommandSponsor commandSponsor = null;
            /**从缓存中取处理器*/
            commandSponsor = sponsorCache.get(command);
            if(commandSponsor == null){
                /**加载处理器，放入缓存中*/
                return loadCommandSponsor(command);
            }else {
                return commandSponsor;
            }
        }
    }

    /**
     * 获取相应接收命令的处理器
     * @param command 命令
     * @return 处理器
     * @throws HandlerLoadException 异常信息
     */
    public static ICommandHandler getCommandHandler(Enum<CommandEnum> command) throws HandlerLoadException {

        if(command == null){
            return null;
        }else {
            ICommandHandler commandHandler = null;
            /**从缓存中取处理器*/
            commandHandler = handlerCache.get(command);
            if(commandHandler == null){
                /**加载处理器，放入缓存中*/
                return loadCommandHandler(command);
            }else {
                return commandHandler;
            }
        }
    }

    /**
     * 加载并返回一个命令接收处理器
     * @param command 命令
     * @return 处理器
     * @throws HandlerLoadException 异常信息
     */
    private static synchronized ICommandHandler loadCommandHandler(Enum<CommandEnum> command)throws HandlerLoadException {
        /**双重校验锁，防止在getCommandHandler方法中获取后，其他的线程新加载了handler*/
        ICommandHandler iCommandHandler = handlerCache.get(command);
        if (iCommandHandler != null){
            return iCommandHandler;
        }
        return CommandFactory.loadCommand(command,ICommandHandler.class);
    }

    /**
     * 加载并返回一个命令发起处理器
     * @param command 命令
     * @return 处理器
     * @throws HandlerLoadException 异常信息
     */
    private static synchronized ICommandSponsor loadCommandSponsor(Enum<CommandEnum> command)throws HandlerLoadException {
        /**双重校验锁，防止在getCommandHandler方法中获取后，其他的线程新加载了handler*/
        ICommandSponsor iCommandSponsor = sponsorCache.get(command);
        if (iCommandSponsor != null){
            return iCommandSponsor;
        }
        return CommandFactory.loadCommand(command,ICommandSponsor.class);
    }

    /**
     * 加载并返回一个命令处理器
     * @param command 命令
     * @param handlerClass 处理器类型
     * @param <T> 处理器类型
     * @return 命令处理器
     * @throws HandlerLoadException
     */
    private static <T> T loadCommand(Enum<CommandEnum> command, Class<T> handlerClass)throws HandlerLoadException {
        /**加载配置*/
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String commandConfigPath = handlerClass == ICommandHandler.class
                ? COMMAND_HANDLER_PATH
                : COMMAND_SPONSOR_PATH;
        InputStream stream = classLoader.getResourceAsStream(commandConfigPath);
        if(stream == null){
            throw new HandlerLoadException(HandlerLoadConstants.HANDLER_NOT_FOUND);
        }else {
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "utf-8"))){
                String lineValue = null;
                while (! StringUtils.isEmpty(lineValue = bufferedReader.readLine())){
                    final String[] split =lineValue.split("::");
                    /**配置文件出错*/
                    if(split.length < 2 || split.length > 2 ||
                            StringUtils.isEmpty(split[0]) || StringUtils.isEmpty(split[1])){
                        throw new HandlerLoadException(HandlerLoadConstants.CONFIG_ERROR);
                    }
                    String commandName = split[0];
                    String handlerName = split[1];
                    if(commandName.equals(command.name())){
                        /**加载handler*/
                        final String handlerBeanName = Class.forName(handlerName).getSimpleName();
                        if(handlerClass == ICommandHandler.class){
                            ICommandHandler iCommandHandler = SpringBeanUtil.getBean(handlerBeanName,ICommandHandler.class);
                            /**加入缓存并返回处理器*/
                            handlerCache.put(command,iCommandHandler);
                            return (T) iCommandHandler;
                        }else if(handlerClass == ICommandSponsor.class){
                            ICommandSponsor iCommandSponsor = SpringBeanUtil.getBean(handlerBeanName,ICommandSponsor.class);
                            /**加入缓存并返回处理器*/
                            sponsorCache.put(command,iCommandSponsor);
                            return (T) iCommandSponsor;
                        }else {
                            /**非法的处理器类型定义*/
                        }
                    }
                }
                /**找不到该命令的配置*/
                throw new HandlerLoadException(HandlerLoadConstants.COMMAND_NOT_CONFIG);
            }catch (Exception e){
                throw new HandlerLoadException(e.getMessage(),e);
            }
        }
    }
}
