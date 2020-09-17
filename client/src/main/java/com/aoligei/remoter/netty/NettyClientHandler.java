package com.aoligei.remoter.netty;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.command.CommandFactory;
import com.aoligei.remoter.command.ICommandHandler;
import com.aoligei.remoter.constant.HandlerLoadConstants;
import com.aoligei.remoter.exception.HandlerLoadException;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.handler.AbstractSponsorCommandHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<BaseResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws Exception {

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
