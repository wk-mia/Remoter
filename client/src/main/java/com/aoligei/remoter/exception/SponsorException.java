package com.aoligei.remoter.exception;

/**
 * @author wk-mia
 * 2020-9-8
 * 客户端发起请求异常
 */
public class SponsorException extends Exception {
    /**
     * 无参构造函数
     */
    public SponsorException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public SponsorException(String message){
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个异常
     */
    public SponsorException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * 用指定原因构造一个异常
     */
    public SponsorException(Throwable cause) {
        super(cause);
    }
}
