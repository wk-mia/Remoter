package com.aoligei.remoter.util;

import java.io.Serializable;

/**
 * @author wk-mia
 * 2020-8-28
 * 统一返回格式
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 5905715228490291386L;

    /**
     * 服务器响应的状态
     */
    private Result.Status status;
    /**
     * 服务器返回的业务数据
     */
    private Object data;
    /**
     * 服务器给出的消息
     */
    private String message;

    public Result() {
    }

    public Result(Result.Status status, Object data) {
        this.status = status;
        this.data = data;
    }

    public Result(Result.Status status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


    public Result.Status getStatus() {
        return this.status;
    }

    public void setStatus(Result.Status status) {
        this.status = status;
    }

    public Object getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 状态的定义。OK：成功处理；FAIL：失败；ERROR：发生错误。
     */
    public static enum Status {
        OK,
        FAIL,
        ERROR;
        private Status() {
        }
    }
}
