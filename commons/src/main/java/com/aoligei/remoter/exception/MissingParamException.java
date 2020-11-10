package com.aoligei.remoter.exception;

/**
 * @author wk-mia
 * 2020-11-8
 * 请求参数缺失异常->Server端使用
 * 该异常发生时，仅仅通知用户异常信息。
 */
public class MissingParamException extends RemoterException {

    /**
     * 无参构造函数
     */
    public MissingParamException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public MissingParamException(String message){
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个异常
     */
    public MissingParamException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * 用指定原因构造一个异常
     */
    public MissingParamException(Throwable cause) {
        super(cause);
    }
}

