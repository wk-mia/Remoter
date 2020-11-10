package com.aoligei.remoter.exception;

/**
 * @author wk-mia
 * 2020-11-8
 * 非法请求异常->Server端使用
 * 该异常发生时，将断开连接。
 */
public class IllegalRequestException extends RemoterException {

    /**
     * 无参构造函数
     */
    public IllegalRequestException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public IllegalRequestException(String message){
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个异常
     */
    public IllegalRequestException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * 用指定原因构造一个异常
     */
    public IllegalRequestException(Throwable cause) {
        super(cause);
    }
}
