package com.aoligei.remoter.exception;


/**
 * @author wk-mia
 * 2020-9-8
 * 客户端连接服务器时异常
 */
public class ConnectException extends ClientException{

    /**
     * 无参构造函数
     */
    public ConnectException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public ConnectException(String message){
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个异常
     */
    public ConnectException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * 用指定原因构造一个异常
     */
    public ConnectException(Throwable cause) {
        super(cause);
    }
}
