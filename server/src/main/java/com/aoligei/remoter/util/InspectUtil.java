package com.aoligei.remoter.util;

import com.aoligei.remoter.beans.ClientMeta;
import com.aoligei.remoter.constant.IncompleteParamConstants;
import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.exception.IncompleteParamException;
import com.aoligei.remoter.exception.ServerException;
import io.netty.channel.Channel;

/**
 * @author wk-mia
 * 2020-8-31
 * 参数检查工具类
 */
public class InspectUtil {

    /**
     * 待注册的客户端基本信息是否完备
     * @param clientMeta 客户端基本信息信息
     * @return true/false
     */
    public static boolean isInfoComplete(ClientMeta clientMeta)throws IncompleteParamException{
        /**
         * 客户端名称为空
         */
        if(clientMeta.getClientName() == null || "".equals(clientMeta.getClientName())){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_NAME_NOT_IN_DATA);
        }
        /**
         * IP为空
         */
        if(clientMeta.getClientIp() == null || "".equals(clientMeta.getClientIp())){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_IP_NOT_IN_DATA);
        }
        /**
         * 客户端是否已拒绝所有远程控制请求
         */
        if(clientMeta.getRejectConnection() == null){
            throw new IncompleteParamException(IncompleteParamConstants.REJECT_CONNECTION_NOT_IN_DATA);
        }
        return true;
    }

}
