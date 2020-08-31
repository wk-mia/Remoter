package com.aoligei.remoter.account;

import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.dto.ClientInformation;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.util.ClientInfoCheckUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wk-mia
 * 2020-8-31
 * 客户端信息账册
 */
@Component
public class ClientAccountBooks implements IAccountAlter {

    /**
     * 所有客户端信息
     */
    private CopyOnWriteArrayList<ClientInformation> clientInformationList;

    /**
     * 如所有客户端的信息都已经持久化
     * 可在代码块中加载所有客户端信息
     */
    {
        clientInformationList = new CopyOnWriteArrayList<ClientInformation>();
    }

    /**
     * 获取所有客户端账册
     * @return 客户端账册
     */
    public CopyOnWriteArrayList<ClientInformation> allClientInformation(){
        return clientInformationList;
    }

    /**
     * 刷新身份识别码
     * @param clientIp 客户端IP地址
     * @return
     * @throws RemoterException
     */
    @Override
    public ClientInformation refreshClientId(String clientIp) throws RemoterException {
        /**
         * 更新身份识别码
         */
        ClientInformation clientInformation = ClientInfoCheckUtil.getClientInformationByClientIp(this.clientInformationList,clientIp);
        if(clientInformation == null){
            throw new RemoterException(ExceptionMessageConstants.CLIENT_NOT_FIND);
        }else {
            clientInformation.setClientId(ClientInfoCheckUtil.createClientId());
            return clientInformation;
        }
    }

    /**
     * 注册客户端信息
     * @param clientInformation 客户端信息
     * @return true/false
     * @throws RemoterException
     */
    @Override
    public boolean register(ClientInformation clientInformation) throws RemoterException {
        if(ClientInfoCheckUtil.isInfoNull(clientInformation)){
            return false;
        }else {
            /**
             * 未指定除客户端标识码之外的其他信息
             */
            if(ClientInfoCheckUtil.isInfoComplete(clientInformation)){
                throw new RemoterException(ExceptionMessageConstants.CLIENT_INFO_NOT_COMPLETE);
            }
            /**
             * 该客户端是否已经存在
             */
            if(ClientInfoCheckUtil.isClientIpExist(this.clientInformationList,clientInformation.getClientIp())){
                throw new RemoterException(ExceptionMessageConstants.CLIENT_ALREADY_EXISTS);
            }
            /**
             * 生成客户端识别码并注册
             */
            clientInformation.setClientId(ClientInfoCheckUtil.createClientId());
            this.clientInformationList.add(clientInformation);
            return true;
        }
    }

    /**
     * 销毁客户端信息
     * @param clientId 客户端身份识别码
     * @return true/false
     * @throws RemoterException
     */
    @Override
    public boolean destroy(String clientId) throws RemoterException {
        /**
         * 销毁客户端信息
         */
        ClientInformation clientInformation = ClientInfoCheckUtil.getClientInformationByClientId(this.clientInformationList,clientId);
        if(clientInformation == null){
            throw new RemoterException(ExceptionMessageConstants.CLIENT_ID_NOT_FIND);
        }else {
            clientInformationList.remove(clientInformation);
            return true;
        }
    }
}
