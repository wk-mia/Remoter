package com.aoligei.remoter.manage;

import com.aoligei.remoter.beans.ClientMeta;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-14
 * 客户端身份管理器
 */
@Component
public class ClientManage {

    /**
     * 当前客户端信息
     */
    private ClientMeta clientInfo;

    /**
     * 客户端与服务器的连接编码
     */
    private String connectionId;

    /**
     * 加载客户端信息到当前管理器
     */
    {
        clientInfo = null;
    }

    /**
     * 获取客户端信息
     * @return
     */
    public ClientMeta getClientInfo(){
        return clientInfo;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId){
        this.connectionId = connectionId;
    }
}
