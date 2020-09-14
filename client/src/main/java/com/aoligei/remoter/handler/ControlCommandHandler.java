package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.StatusEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.manage.ClientManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wk-mia
 * 2020-9-14
 * 点对点模式客户端-控制处理器
 * 负责接受到服务器对于控制请求的处理结果。
 */
public class ControlCommandHandler extends AbstractClientHandler {

    @Autowired
    private ClientManage clientManage;

    /**
     * 特定的处理器-控制处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        if(baseResponse.getStatus() == StatusEnum.OK){
            if(baseResponse.getTerminalTypeEnum() == TerminalTypeEnum.SERVER_2_SLAVE){
                /**
                 * 受控端的业务：
                 * 拿到连接编码，交由客户决定是否拒绝主控端发来的连接请求，并将结果返回服务器。
                 * 此处暂时为客户端直接同意。
                 * 同意后，就开始发送屏幕截图信息。
                 */
                if(baseResponse.getConnectionId() != null || "".equals(baseResponse.getConnectionId())){
                    BaseRequest baseRequest = new BaseRequest(){{
                        setConnectionId(baseResponse.getConnectionId());
                        setCommandEnum(CommandEnum.CONTROL);
                        setClientId(clientManage.getClientInfo().getClientId());
                        setTerminalTypeEnum(TerminalTypeEnum.SLAVE);
                        setData(Boolean.TRUE);
                    }};
                    channelHandlerContext.writeAndFlush(baseRequest);

                    clientManage.setConnectionId(baseResponse.getConnectionId());
                    logInfo(baseResponse,"客户端已同意远程连接");
                    /**
                     * 开始发送屏幕截图
                     */




                }else {

                }
            }else if(baseResponse.getTerminalTypeEnum() == TerminalTypeEnum.SERVER_2_MASTER){
                /**
                 * 主控端，拿到连接编码。
                 */
                clientManage.setConnectionId(baseResponse.getConnectionId());
                logInfo(baseResponse,baseResponse.getMessage());
            }
        }else if(baseResponse.getStatus() == StatusEnum.ERROR) {
            logError(baseResponse,baseResponse.getMessage());
        }else {
        }
    }
}
