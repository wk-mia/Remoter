package com.aoligei.remoter.bu.service;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wk-mia
 * 2020-8-28
 * 客户端的编码相关的请求处理
 */
public interface ClientService {

    /**
     * 给客户端生成一个唯一编码
     * @return 返回字符序列
     * @throws Exception
     */
    String getClientId(HttpServletRequest httpServletRequest)throws Exception;


    /**
     * 检查身份标识码是否合法
     * @param map Key->clientId Value->身份标识码
     * @return true/false
     * @throws Exception
     */
    Boolean isClientIdLegal(Map map)throws Exception;
}
