package com.aoligei.remoter.exception;

/**
 * @author wk-mia
 * 2020-8-28
 * RemoterException异常类定义
 */
public class RemoterException extends Exception {

    /**
     * 无参构造函数
     */
    public RemoterException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public RemoterException(String message){
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个异常
     */
    public RemoterException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * 用指定原因构造一个异常
     */
    public RemoterException(Throwable cause) {
        super(cause);
    }
}
