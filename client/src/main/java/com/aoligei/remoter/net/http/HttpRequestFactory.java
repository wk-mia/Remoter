package com.aoligei.remoter.net.http;

import org.springframework.web.client.RestTemplate;

/**
 * @author wk-mia
 * 2020-8-31
 * 向服务器发起HTTP请求的构造工厂，用于访问服务器的HTTP接口
 */
public class HttpRequestFactory {

    private static RestTemplate restTemplate;

    /**
     * 向服务器发起GET请求
     * @param ip 服务器的IP地址
     * @param port 服务端口
     * @param requestUrlConstant 接口的地址
     * @param parameter 参数
     * @param <T> 返回的结果类型
     * @param <M> 请求的参数类型
     * @return Result结果
     */
    public static <T,M> Result<T> get(String ip,String port,String requestUrlConstant,M parameter){
        String url = ip.concat(port).concat(requestUrlConstant);
        restTemplate = new RestTemplate();
        Result<T> result = new Result<T>();
        result = restTemplate.getForEntity(url,parameter,Result.class);
        return result;
    }

    /**
     * 向服务器发起POST请求
     * @param ip 服务器的IP地址
     * @param port 服务端口
     * @param requestUrlConstant 接口的地址
     * @param parameter 参数
     * @param <T> 返回的结果类型
     * @param <M> 请求的参数类型
     * @return Result结果
     */
    public static <T,M> Result<T> post(String ip,String port,String requestUrlConstant,M parameter){
        String url = ip.concat(port).concat(requestUrlConstant);
        restTemplate = new RestTemplate();
        Result<T> result = new Result<T>();
        result = restTemplate.postForObject(url,parameter,Result<T>.class);
        return result;
    }
}
