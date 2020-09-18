package com.aoligei.remoter.command;


import com.aoligei.remoter.constant.HandlerLoadConstants;
import com.aoligei.remoter.exception.HandlerLoadException;
import com.aoligei.remoter.enums.CommandEnum;
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
     * 接收命令处理器的缓存
     * 当请求到达时，先查缓存，缓存中没有再初始化相应的处理器
     */
    private static final ConcurrentHashMap<Enum<CommandEnum>,ICommandHandler> handlerCache = new ConcurrentHashMap<>();

    /**
     * 发起命令处理器的缓存
     * 当请求到达时，先查缓存，缓存中没有再初始化相应的处理器
     */
    private static final ConcurrentHashMap<Enum<CommandEnum>,ICommandHandler> sponsorCache = new ConcurrentHashMap<>();


    /**
     * 获取相应命令的处理器
     * @param command 命令
     * @param handlerType 处理器类型
     * @return 处理器
     * @throws ServerException 异常信息
     */
    public static ICommandHandler getCommandHandler(Enum<CommandEnum> command, String handlerType) throws HandlerLoadException {
        /**
         * 处理器类型校验，只能在handler和sponsor中取值
         */
        if(!StringUtils.isEmpty(handlerType) && !(handlerType.equals("handler")) && !(handlerType.equals("sponsor"))){
            throw  new HandlerLoadException(HandlerLoadConstants.HANDLER_TYPE_ERROR);
        }

        if(command == null){
            return null;
        }else {
            ICommandHandler commandHandler = null;
            /**
             * 从缓存中取处理器
             */
            if(handlerType.equals("handler")){
                commandHandler = handlerCache.get(command);
            }else{
                commandHandler = sponsorCache.get(command);
            }
            if(commandHandler == null){
                /**
                 * 加载处理器，放入缓存中
                 */
                return loadCommandHandler(command);
            }else {
                return commandHandler;
            }
        }
    }

    /**
     * 加载并返回一个命令处理器
     * @param command 命令
     * @return 处理器
     * @throws ServerException 异常信息
     */
    private static synchronized ICommandHandler loadCommandHandler(Enum<CommandEnum> command)throws HandlerLoadException {
        /**
         * 双重校验锁，防止在getCommandHandler方法中获取后，其他的线程新加载了handler
         */
        ICommandHandler iCommandHandler = handlerCache.get(command);
        if (iCommandHandler != null){
            return iCommandHandler;
        }
        /**
         * 加载配置
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream stream = classLoader.getResourceAsStream(COMMAND_HANDLER_PATH);
        if(stream == null){
            throw new HandlerLoadException(HandlerLoadConstants.HANDLER_NOT_FOUND);
        }else {
            try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "utf-8"))){
                String lineValue = null;
                while (! StringUtils.isEmpty(lineValue = bufferedReader.readLine())){
                    final String[] command_type_handler =lineValue.split("::");
                    /**
                     * 配置文件出错
                     */
                    if(command_type_handler.length < 3 || command_type_handler.length > 3 ||
                            StringUtils.isEmpty(command_type_handler[0]) || StringUtils.isEmpty(command_type_handler[1]) ||
                            StringUtils.isEmpty(command_type_handler[2])){
                        throw new HandlerLoadException(HandlerLoadConstants.CONFIG_ERROR);
                    }
                    String commandName = command_type_handler[0];
                    String typeName = command_type_handler[1];
                    String handlerName = command_type_handler[2];
                    if(commandName.equals(command.name())){
                        /**
                         * 加载handler
                         */
                        final Class<?> handlerClass = Class.forName(handlerName);
                        iCommandHandler = (ICommandHandler) handlerClass.newInstance();
                        /**
                         * 加入缓存
                         */
                        if(typeName.equals("handler")){
                            handlerCache.put(command,iCommandHandler);
                        }else if(typeName.equals("sponsor")){
                            sponsorCache.put(command,iCommandHandler);
                        }else {
                            throw new HandlerLoadException(HandlerLoadConstants.CONFIG_ERROR);
                        }
                        return iCommandHandler;
                    }
                }
                throw new HandlerLoadException(HandlerLoadConstants.COMMAND_NOT_CONFIG);
            }catch (Exception e){
                throw  new HandlerLoadException(e.getMessage(),e);
            }
        }
    }
}
