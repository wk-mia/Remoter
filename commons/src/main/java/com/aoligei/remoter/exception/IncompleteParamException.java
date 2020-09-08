package com.aoligei.remoter.exception;

/**
 * @author wk-mia
 * 2020-9-8
 * 客户端发来的请求参数不健全异常
 */
public class IncompleteParamException extends ServerException{
    /**
     * 无参构造函数
     */
    public IncompleteParamException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public IncompleteParamException(String message){
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个异常
     */
    public IncompleteParamException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * 用指定原因构造一个异常
     */
    public IncompleteParamException(Throwable cause) {
        super(cause);
    }
}
