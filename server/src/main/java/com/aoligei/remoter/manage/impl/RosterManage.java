package com.aoligei.remoter.manage.impl;

import com.aoligei.remoter.beans.BasicClientInfo;
import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.generate.IdentifyFactory;
import com.aoligei.remoter.manage.IRoster;
import com.aoligei.remoter.util.InspectUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wk-mia
 * 2020-9-9
 * 客户端信息账册管理器
 */
@Component
public class RosterManage implements IRoster {

    /**
     * 所有客户端信息
     */
    private CopyOnWriteArrayList<BasicClientInfo> roster = new CopyOnWriteArrayList<BasicClientInfo>();

    /**
     * 如所有客户端的信息都已经持久化
     * 可在代码块中加载所有客户端信息
     */
    {
        /**
         * 测试数据
         */
        roster.add(new BasicClientInfo("3f791e1b-3819-45f4-b37c-f757c371c728",
                "ClientA",0,"127.0.0.1",false));
    }

    /**
     * 获取所有花名册
     * @return 客户端账册
     */
    public CopyOnWriteArrayList<BasicClientInfo> getRoster(){
        return roster;
    }

    /**
     * 客户端注册
     * @param basicClientInfo 客户端信息
     * @throws ServerException
     */
    @Override
    public void register(BasicClientInfo basicClientInfo) throws ServerException {
        if(InspectUtil.isInfoComplete(basicClientInfo)){
            /**
             * 该客户端是否已经注册过
             */
            if(roster.stream().filter(item -> basicClientInfo.getClientIp().equals(item.getClientIp())).findAny().isPresent()){
                throw new ServerException(ServerExceptionConstants.CLIENT_ALREADY_REGISTER);
            }else {
                /**
                 * 生成客户端识别码并注册
                 */
                basicClientInfo.setClientId(IdentifyFactory.createClientId());
                this.roster.add(basicClientInfo);
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
        BasicClientInfo basicClientInfo = roster.stream().filter(item ->
                clientId.equals(item.getClientId())).findAny().get();
        if(basicClientInfo == null){
            throw new ServerException(ServerExceptionConstants.CLIENT_NOT_FIND);
        }else {
            roster.remove(basicClientInfo);
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
