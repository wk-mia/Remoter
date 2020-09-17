package com.aoligei.remoter.exception;

/**
 * @author wk-mia
 * 2020-9-8
 * 命令处理器加载异常类
 */
public class HandlerLoadException extends Exception {
    /**
     * 无参构造函数
     */
    public HandlerLoadException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public HandlerLoadException(String message){
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个异常
     */
    public HandlerLoadException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * 用指定原因构造一个异常
     */
    public HandlerLoadException(Throwable cause) {
        super(cause);
    }
}
