package com.aoligei.remoter.net.http;

import com.aoligei.remoter.ui.form.DialogForm;
import com.aoligei.remoter.ui.form.MainForm;
import com.aoligei.remoter.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Iterator;
import java.util.Map;

/**
 * @author wk-mia
 * 2020-8-31
 * 向服务器发起HTTP请求的构造工厂，用于访问服务器的HTTP接口
 */
public class HttpRequestFactory {

    private static Logger log = LoggerFactory.getLogger(HttpRequestFactory.class);

    private static RestTemplate restTemplate;

    /**
     * 向服务器发起GET请求
     * @param ip 服务器的IP地址
     * @param port 服务端口
     * @param requestUrlConstant 接口的地址
     * @param parameter 参数
     * @param <T> 返回的结果类型
     * @return Result结果
     */
    public static <T> Result<T> get(String ip, String port, String requestUrlConstant, Map parameter){
        /**
         * 构造请求的地址
         * 如果项目里用的Https请将第一项改为http并构造相应的安全项
         * 这里并没有设置请求头相关的内容，如有需要，请自行构造
         */
        StringBuilder urlBuilder = new StringBuilder().append("http://").append(ip).append(":").append(port).append(requestUrlConstant);
        /**
         * 请求地址后拼接参数
         */
        if(parameter != null || parameter.size() > 0){
            urlBuilder.append("?");
            Iterator<Map.Entry<String, Object>> iterator = parameter.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, Object> entry = iterator.next();
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        /**
         * 得到全址
         */
        String url = urlBuilder.toString().endsWith("&")
                ? urlBuilder.toString().substring(0,urlBuilder.toString().length()-1)
                : urlBuilder.toString();
        log.info("*****GET请求的地址为：" + url);
        /**
         * 发起请求,如发生异常通过弹框通知用户
         */
        restTemplate = new RestTemplate();
        try{
            Result<T> result = restTemplate.getForObject(url,Result.class,parameter);
            return result;
        }catch (RestClientException e){
            DialogForm.errorDialog("发起请求异常",e.getMessage());
            return null;
        }
    }


    /**
     * 向服务器发起POST请求
     * @param ip 服务器的IP地址
     * @param port 服务端口
     * @param requestUrlConstant 接口的地址
     * @param parameter 参数
     * @param <T> 返回的结果类型
     * @return Result结果
     */
    public static <T> Result<T> post(String ip,String port,String requestUrlConstant,Map parameter){
        /**
         * 构造请求的地址
         * 如果项目里用的Https请将第一项改为http并构造相应的安全项
         * 这里并没有设置请求头相关的内容，如有需要，请自行构造
         */
        StringBuilder urlBuilder = new StringBuilder().append("http://").append(ip).append(":").append(port).append(requestUrlConstant);
        String url = urlBuilder.toString();
        /**
         * 发起请求,如发生异常通过弹框通知用户
         */
        restTemplate = new RestTemplate();
        try{
            Result<T> result = restTemplate.postForObject(url,parameter,Result.class);
            return result;
        }catch (RestClientException e){
            DialogForm.errorDialog("发起请求异常",e.getMessage());
            return null;
        }
    }
}
