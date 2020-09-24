package com.aoligei.remoter.manage;

import com.aoligei.remoter.beans.BasicClientInfo;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author wk-mia
 * 2020-9-14
 * 客户端身份管理器
 */
@Component
public class TerminalManage implements Serializable {

    /**
     * 序列化常量
     */
    private static final long serialVersionUID = -3911255650485738676L;

    /**
     * 当前客户端信息
     */
    private BasicClientInfo clientInfo;

    /**
     * 客户端与服务器的连接编码
     */
    private String connectionId;

    /**
     * 加载客户端信息到当前管理器
     */
    {
        clientInfo = new BasicClientInfo("3f791e1b-3819-45f4-b37c-f757c371c728",
                "ClientA",0,"127.0.0.1",false);
    }

    /**
     * 获取客户端信息
     * @return
     */
    public BasicClientInfo getClientInfo(){
        return clientInfo;
    }

    public String getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(String connectionId){
        this.connectionId = connectionId;
    }
}
