package com.aoligei.remoter.bu.controller;

import com.aoligei.remoter.bu.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.aoligei.remoter.util.Result;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wk-mia
 * 2020-8-28
 * 客户端的编码相关的请求处理
 */
@RestController
@RequestMapping(value = "/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * 客户端向服务器索要一个唯一编码
     * @return 编码字符串
     */
    @RequestMapping(value = "/getClientId", method = RequestMethod.GET)
    public Result getClientId(HttpServletRequest httpServletRequest){
        try{
            return new Result(Result.Status.OK,clientService.getClientId(httpServletRequest),"生成编码成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(Result.Status.FAIL,null,e.getMessage());
        }
    }


    /**
     * 身份标识码是否合法
     * @param map Key->clientId Value->身份标识码
     * @return data->Boolean
     */
    @RequestMapping(value = "/isClientIdLegal", method = RequestMethod.POST)
    public Result isClientIdLegal(@RequestBody Map map){
        try{
            return new Result(Result.Status.OK,clientService.isClientIdLegal(map),"验证成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(Result.Status.FAIL,null,e.getMessage());
        }
    }
}
