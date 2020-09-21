package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandHandler;
import com.aoligei.remoter.constant.HandlerLoadConstants;
import com.aoligei.remoter.exception.HandlerLoadException;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.handler.AbstractSponsorCommandHandler;
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
        ICommandHandler commandHandler = CommandFactory.getCommandHandler(baseResponse.getCommandEnum(),"handler");
        commandHandler.handle(channelHandlerContext,baseResponse);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    protected void sponsorCommand(BaseRequest request) throws SponsorException{
        /**
         * 调用Sponsor处理器处理命令
         */
        try{
            final ICommandHandler commandHandler = CommandFactory.getCommandHandler(request.getCommandEnum(),"sponsor");
            if(commandHandler instanceof AbstractSponsorCommandHandler){
                AbstractSponsorCommandHandler handler = (AbstractSponsorCommandHandler) commandHandler;
                handler.sponsor(request);
            }else {
                throw new HandlerLoadException(HandlerLoadConstants.HANDLER_TYPE_ERROR);
            }
        }catch (HandlerLoadException e){
            throw new SponsorException(e.getMessage(),e);
        }
    }
}
