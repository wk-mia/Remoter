package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandHandler;
import com.aoligei.remoter.command.ICommandSponsor;
import com.aoligei.remoter.constant.HandlerLoadConstants;
import com.aoligei.remoter.exception.HandlerLoadException;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.sponsor.AbstractCommandSponsor;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<BaseResponse> {

    private static Logger log = LoggerFactory.getLogger(NettyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws Exception {
        log.info(baseResponse.toString());
        /**
         * 分发命令并进行处理
         */
        ICommandHandler commandHandler = CommandFactory.getCommandHandler(baseResponse.getCommandEnum());
        commandHandler.handle(channelHandlerContext,baseResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

}
