package com.aoligei.remoter.manage;

import com.aoligei.remoter.beans.BasicClientInfo;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;

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
     * 客户端与服务器的连接信息：{String:连接编码}
     */
    private CopyOnWriteArraySet<String> connectionIds = new CopyOnWriteArraySet<String>();

    /**
     * 是否正在远程工作中
     */
    private boolean remotingFlag = false;

    /**
     * 加载客户端信息到当前管理器
     */
    {
        clientInfo = new BasicClientInfo("14927006-001",
                "master",0,"127.0.0.1",false);
//        clientInfo = new BasicClientInfo("14927006-002",
//                "slaver",0,"127.0.0.2",false);
    }

    /**
     * 获取客户端信息
     * @return
     */
    public BasicClientInfo getClientInfo(){
        return clientInfo;
    }

    /**
     * 移除远程控制的连接
     * @param connectionId 连接编码
     */
    public void removeConnection(String connectionId) {
        connectionIds.remove(connectionId);
    }

    /**
     * 新建远程控制的连接
     * @param connectionId 连接编码
     */
    public void createConnection(String connectionId){
        connectionIds.add(connectionId);
    }

    public boolean getRemotingFlag(){return remotingFlag;}

    public void setRemotingFlag(boolean remotingFlag){this.remotingFlag = remotingFlag;}
}
