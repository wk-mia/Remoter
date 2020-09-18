package com.aoligei.remoter.exception;

import java.io.Serializable;

/**
 * @author wk-mia
 * 2020-9-8
 * 服务器处理异常
 */
public class ServerException extends Exception implements Serializable {

    /**
     * 序列化常量
     */
    private static final long serialVersionUID = -3911255650485738676L;

    /**
     * 无参构造函数
     */
    public ServerException(){
        super();
    }

    /**
     * 用详细信息指定一个异常
     */
    public ServerException(String message){
        super(message);
    }

    /**
     * 用指定的详细信息和原因构造一个异常
     */
    public ServerException(String message, Throwable cause){
        super(message,cause);
    }

    /**
     * 用指定原因构造一个异常
     */
    public ServerException(Throwable cause) {
        super(cause);
    }
}
