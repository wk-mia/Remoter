package com.aoligei.remoter.bu.service.impl;

import com.aoligei.remoter.account.ClientAccountBooks;
import com.aoligei.remoter.bu.service.ClientService;
import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.constant.SystemTypeConstants;
import com.aoligei.remoter.dto.ClientInformation;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.util.ClientInfoCheckUtil;
import com.aoligei.remoter.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author wk-mia
 * 2020-8-28
 * 客户端的编码相关的请求处理
 */
@Service
public class ClientServiceImpl implements ClientService {

    private static Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Autowired
    private ClientAccountBooks clientAccountBooks;

    /**
     * 给客户端生成一个唯一编码
     * @return 返回字符序列
     * @throws Exception
     */
    @Override
    public String getClientId(HttpServletRequest httpServletRequest) throws Exception {

        ClientInformation clientInformation = new ClientInformation();
        clientInformation.setClientIp(IpUtil.getIpAddr(httpServletRequest));
        clientInformation.setClientName("name");
        clientInformation.setClientSystemType(SystemTypeConstants.WINDOWS);
        clientInformation.setRejectAllConnections(false);
        clientInformation.setClientPort(60000);
        /**
         * 生成客户端身份标识码并注册
         */
        clientAccountBooks.register(clientInformation);
        log.info("***** " + clientInformation.getClientIp() + " 的身份标识码为：" + clientInformation.getClientId());
        /**
         * 将得到的身份信息持久化,此处请根据实际情况持久化
         */
        return clientInformation.getClientId();
    }


    /**
     * 检查身份标识码是否合法
     * @param map Key->clientId Value->身份标识码
     * @return true/false
     * @throws Exception
     */
    @Override
    public Boolean isClientIdLegal(Map map) throws Exception {

        if(map == null || map.get("clientId") == null){
            throw new RemoterException(ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }
        /**
         * 得到所有的身份信息，检查身份识别码是否在列表中
         */
        if(ClientInfoCheckUtil.isClientIdExist(clientAccountBooks.allClientInformation(),map.get("clientId").toString())){
            return true;
        }else {
            return false;
        }
    }
}
