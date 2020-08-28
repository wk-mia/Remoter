package com.aoligei.remoter.bu.service.impl;

import com.aoligei.remoter.bu.service.ClientService;
import org.springframework.stereotype.Service;

/**
 * @author wk-mia
 * 2020-8-28
 * 客户端的编码相关的请求处理
 */
@Service
public class ClientServiceImpl implements ClientService {

    /**
     * 给客户端生成一个唯一编码
     * @return 返回字符序列
     * @throws RuntimeException
     */
    @Override
    public String getId() throws RuntimeException {
        return null;
    }
}
