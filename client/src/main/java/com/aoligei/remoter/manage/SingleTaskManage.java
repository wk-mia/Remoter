package com.aoligei.remoter.manage;

import com.aoligei.remoter.bean.KeyBoardEvent;
import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.business.RequestProcessor;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandSponsor;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.HandlerLoadException;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.netty.ClientChannelInitializer;
import com.aoligei.remoter.netty.NettyClient;
import com.aoligei.remoter.util.SpringBeanUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-28
 * 单次任务管理器：注册、连接、控制、断开等。
 */
@Component
public class SingleTaskManage {

    private static final Logger log = LoggerFactory.getLogger(SingleTaskManage.class);

    /**请求组装器*/
    private RequestProcessor processor;
    @Autowired
    private void setProcessor(RequestProcessor processor){
        this.processor = processor;
    }

    /**
     * 发起连接请求
     * @throws Exception
     */
    public void startConnect(){
        try{
            NettyClient nettyClient = SpringBeanUtil.getBean(NettyClient.class);
            nettyClient.connect(processor.buildConnectRequest());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 销毁连接
     */
    public void destroy(){
        try{
            NettyClient nettyClient = SpringBeanUtil.getBean(NettyClient.class);
            nettyClient.destroy();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 控制请求
     * @param slaveId 目标终端身份识别码
     */
    public void startControl(String slaveId){
        try {
            ICommandSponsor sponsor = CommandFactory.getCommandSponsor(CommandEnum.CONTROL);
            sponsor.sponsor(processor.buildControlRequest(slaveId));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 停止控制请求
     * @param connectionId 连接编码
     * @param terminal 终端类型
     */
    public void stopControl(String connectionId, TerminalTypeEnum terminal){
        try{
            ICommandSponsor sponsor = CommandFactory.getCommandSponsor(CommandEnum.STOP_CONTROL);
            sponsor.sponsor(processor.buildStopControlRequest(connectionId,terminal));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 键盘事件
     * @param connectionId 连接编码
     * @param event 源事件
     */
    public void sendKeyBoard(String connectionId, KeyBoardEvent event){
        try{
            ICommandSponsor sponsor = CommandFactory.getCommandSponsor(CommandEnum.STOP_CONTROL);
            sponsor.sponsor(processor.buildKeyBoardRequest(connectionId,event));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
