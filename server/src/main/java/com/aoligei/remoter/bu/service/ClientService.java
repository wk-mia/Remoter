package com.aoligei.remoter.bu.service;


import javax.servlet.http.HttpServletRequest;

/**
 * @author wk-mia
 * 2020-8-28
 * 客户端的编码相关的请求处理
 */
public interface ClientService {

    /**
     * 给客户端生成一个唯一编码
     * @return 返回字符序列
     * @throws RuntimeException
     */
    String getClientId(HttpServletRequest httpServletRequest)throws Exception;
}
