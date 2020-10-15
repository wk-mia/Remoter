package com.aoligei.remoter.netty;

import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.manage.SingleTaskManage;
import com.aoligei.remoter.util.AccessConfigUtil;
import com.aoligei.remoter.util.AccessConfigUtil.Config;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-15
 * Netty-客户端，负责客户端与服务器的通信
 */
@Component
public class NettyClient {

    private static final Logger log = LoggerFactory.getLogger(NettyClient.class);

    /**单次任务管理器*/
    private SingleTaskManage singleTaskManage;
    /**处理器初始化器*/
    private ClientChannelInitializer channelInitializer;
    @Autowired
    private NettyClient(SingleTaskManage singleTaskManage, ClientChannelInitializer channelInitializer){
        this.singleTaskManage = singleTaskManage;
        this.channelInitializer = channelInitializer;
    }

    /**
     * IO线程池
     */
    private NioEventLoopGroup group;
    /**
     * 服务器地址
     */
    private String host;
    /**
     * 服务器端口
     */
    private int port;

    /**
     * 初始化
     */
    {
        group = new NioEventLoopGroup();
        host = AccessConfigUtil.getValue(Config.PARAM,"remoter.server.ip");
        port = AccessConfigUtil.getNumber(Config.PARAM,"remoter.server.port");
    }

    /**
     * 注册
     * @throws Exception
     */
    public void register() throws Exception{

    }

    /**
     * 连接服务器
     * @throws Exception
     */
    public void connect() throws Exception{
        singleTaskManage.startConnect(host,port,group,channelInitializer);
    }

    /**
     * 向服务器发送控制请求
     * @param slaveId 请求类型
     */
    public void control(String slaveId) throws SponsorException {
        singleTaskManage.startControl(slaveId);
    }

    /**
     * 销毁连接
     */
    public void destroy(){
        group.shutdownGracefully();
    }

}
