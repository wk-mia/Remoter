package com.aoligei.remoter.bu.service.impl;

import com.aoligei.remoter.bu.service.ClientService;
import com.aoligei.remoter.generate.IIdentifierProvider;
import com.aoligei.remoter.generate.impl.UUIDIdentifier;
import com.aoligei.remoter.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wk-mia
 * 2020-8-28
 * 客户端的编码相关的请求处理
 */
@Service
public class ClientServiceImpl implements ClientService {

    private static Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    /**
     * 给客户端生成一个唯一编码
     * @return 返回字符序列
     * @throws RuntimeException
     */
    @Override
    public String getClientId(HttpServletRequest httpServletRequest) throws Exception {
        /**
         * 自定义生成方法时，需要实现IIdentifierProvider接口的provide方法
         */
        IIdentifierProvider iIdentifierProvider = new UUIDIdentifier();
        String clientId = iIdentifierProvider.provide();
        log.info("***** " + IpUtil.getIpAddr(httpServletRequest) + " 的身份标识码为：" + clientId);
        /**
         * 将得到的身份信息持久化，此处根据实际需要持久化
         */
        // TODO: 2020/8/29 AOP持久化实现
        return clientId;
    }
}
