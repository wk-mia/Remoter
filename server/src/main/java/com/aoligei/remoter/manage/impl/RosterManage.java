package com.aoligei.remoter.manage.impl;

import com.aoligei.remoter.beans.BasicClientInfo;
import com.aoligei.remoter.constant.IllegalRequestConstants;
import com.aoligei.remoter.constant.MissingParamConstants;
import com.aoligei.remoter.exception.IllegalRequestException;
import com.aoligei.remoter.exception.MissingParamException;
import com.aoligei.remoter.exception.RemoterException;
import com.aoligei.remoter.generate.IdentifyFactory;
import com.aoligei.remoter.manage.IRoster;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
        roster.add(new BasicClientInfo("14927006-001",
                "master",0,"127.0.0.1",false));
        roster.add(new BasicClientInfo("14927006-002",
                "slaver",0,"127.0.0.2",false));
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
     * @throws RemoterException
     */
    @Override
    public void register(BasicClientInfo basicClientInfo) throws RemoterException {
        /**客户端名称为空*/
        if(StringUtils.isEmpty(basicClientInfo.getClientName())){
            throw new MissingParamException(MissingParamConstants.CLIENT_NAME_NOT_INCLUDED);
        }
        /**IP为空*/
        if(StringUtils.isEmpty(basicClientInfo.getClientIp())){
            throw new MissingParamException(MissingParamConstants.CLIENT_IP_NOT_INCLUDED);
        }
        /**客户端是否已拒绝所有远程控制请求*/
        if(basicClientInfo.getRejectConnection() == null){
            throw new MissingParamException(MissingParamConstants.REJECT_CONNECTION_NOT_INCLUDED);
        }

        /*** 该客户端是否已经注册过*/
        if(roster.stream().filter(item -> basicClientInfo.getClientIp().equals(item.getClientIp())).findAny().isPresent()){
            throw new IllegalRequestException(IllegalRequestConstants.CLIENT_ALREADY_REGISTER);
        }else {
            /**生成客户端识别码并注册*/
            basicClientInfo.setClientId(IdentifyFactory.createClientId());
            this.roster.add(basicClientInfo);
        }
    }

    /**
     * 客户端注销
     * @param clientId 客户端身份识别码
     * @throws RemoterException
     */
    @Override
    public void unRegister(String clientId) throws RemoterException {
        BasicClientInfo basicClientInfo = roster.stream().filter(item ->
                clientId.equals(item.getClientId())).findAny().get();
        if(basicClientInfo == null){
            throw new IllegalRequestException(IllegalRequestConstants.CLIENT_NOT_FIND);
        }else {
            roster.remove(basicClientInfo);
        }
    }

    /**
     * 客户端刷新身份识别码
     * @param oldClientId 客户端原身份识别码
     * @return
     * @throws RemoterException
     */
    @Override
    public String refreshClientId(String oldClientId) throws RemoterException {
        // TODO: 2020/9/9 刷新身份识别码，需要考虑连接和通道组的同步问题 
        return null;
    }
}
