package com.aoligei.remoter.manage;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.business.RequestProcessor;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandSponsor;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.exception.HandlerLoadException;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.netty.ClientChannelInitializer;
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
 * 发起命令处理器管理器
 * 负责向Sponsors处理器发起命令，由具体的处理器发起请求。目前，Sponsor只有一个通用的处理器。
 */
@Component
public class SponsorManage {

    private static final Logger log = LoggerFactory.getLogger(SponsorManage.class);

    /**
     * 请求组装器
     */
    private RequestProcessor processor;
    @Autowired
    private void setProcessor(RequestProcessor processor){
        this.processor = processor;
    }

    /**
     * Sponsor
     */
    private ICommandSponsor sponsor;

    public void setContext(ChannelHandlerContext context){
        try {
            sponsor = CommandFactory.getCommandSponsor(CommandEnum.CONNECT);
            sponsor.setContext(context);
        }catch (HandlerLoadException e){

        }
    }

    /**
     * 发起连接请求
     * @param host 服务器主机
     * @param port 服务器端口
     * @param group IO线程池
     * @param channelInitializer 处理器初始化器
     * @throws Exception
     */
    public void startConnect(String host, int port, NioEventLoopGroup group, ClientChannelInitializer channelInitializer)throws Exception{
        final Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(channelInitializer);
        final ChannelFuture sync = bootstrap.connect(host, port).sync();
        final BaseRequest connectRequest = processor.buildConnectRequest();
        sync.channel().writeAndFlush(connectRequest);
        try {
            sync.channel().closeFuture();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw e;
        }
    }

    /**
     * 心跳请求
     */
    public void startHeartbeat(){
        try {
            sponsor.sponsor(processor.buildHeartbeatRequest());
        }catch (SponsorException e){

        }catch (Exception e){

        }
    }

    /**
     * 控制请求
     * @param slaveId 目标终端身份识别码
     */
    public void startControl(String slaveId){
        try {
            sponsor.sponsor(processor.buildControlRequest(slaveId));
        }catch (SponsorException e){

        }catch (Exception e){

        }
    }
}
