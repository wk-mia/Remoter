package com.aoligei.remoter.manage;

import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.generate.IdentifyFactory;
import com.aoligei.remoter.beans.ClientMeta;
import com.aoligei.remoter.util.InspectUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wk-mia
 * 2020-9-9
 * 客户端信息账册管理器
 */
@Component
public class ClientMetaManage implements IClientMetaManage {

    /**
     * 所有客户端信息
     */
    private CopyOnWriteArrayList<ClientMeta> clientMetas;

    /**
     * 如所有客户端的信息都已经持久化
     * 可在代码块中加载所有客户端信息
     */
    {
        clientMetas = new CopyOnWriteArrayList<ClientMeta>();
    }

    /**
     * 获取所有客户端账册
     * @return 客户端账册
     */
    public CopyOnWriteArrayList<ClientMeta> allClientMetas(){
        return clientMetas;
    }

    /**
     * 客户端注册
     * @param clientMeta 客户端信息
     * @throws ServerException
     */
    @Override
    public void register(ClientMeta clientMeta) throws ServerException {
        if(InspectUtil.isInfoComplete(clientMeta)){
            /**
             * 该客户端是否已经注册过
             */
            if(clientMetas.stream().filter(item -> clientMeta.getClientIp().equals(item.getClientIp())).findAny().isPresent()){
                throw new ServerException(ServerExceptionConstants.CLIENT_ALREADY_REGISTER);
            }else {
                /**
                 * 生成客户端识别码并注册
                 */
                clientMeta.setClientId(IdentifyFactory.createClientId());
                this.clientMetas.add(clientMeta);
            }
        }
    }

    /**
     * 客户端注销
     * @param clientId 客户端身份识别码
     * @throws ServerException
     */
    @Override
    public void unRegister(String clientId) throws ServerException {
        ClientMeta clientMeta = clientMetas.stream().filter(item ->
                clientId.equals(item.getClientId())).findAny().get();
        if(clientMeta == null){
            throw new ServerException(ServerExceptionConstants.CLIENT_NOT_FIND);
        }else {
            clientMetas.remove(clientMeta);
        }
    }

    /**
     * 客户端刷新身份识别码
     * @param oldClientId 客户端原身份识别码
     * @return
     * @throws ServerException
     */
    @Override
    public String refreshClientId(String oldClientId) throws ServerException {
        // TODO: 2020/9/9 刷新身份识别码，需要考虑连接和通道组的同步问题 
        return null;
    }
}
