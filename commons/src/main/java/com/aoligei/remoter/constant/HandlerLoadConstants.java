package com.aoligei.remoter.constant;

/**
 * @author wk-mia
 * 2020-9-17
 * 命令处理器加载常量配置类
 */
public class HandlerLoadConstants {
    /**
     * 不支持的处理器类型
     */
    public static final String HANDLER_TYPE_ERROR = "unsupported handler type";
    /**
     * 没有找到命令处理器相关的配置
     */
    public static final String HANDLER_NOT_FOUND = "not find the command-handler config";
    /**
     * 没有找到命令处理器相关的配置
     */
    public static final String CONFIG_ERROR = "the config file cannot be resolved";
    /**
     * 没有找到相关命令处理器的配置
     */
    public static final String COMMAND_NOT_CONFIG = "config file cannot contains this command";
}
