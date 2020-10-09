package com.aoligei.remoter.handler;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.ResponseStatusEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.manage.TerminalManage;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wk-mia
 * 2020-9-14
 * 点对点模式客户端-控制处理器
 * 负责接受到服务器对于控制请求的处理结果。
 */
@Component(value = "ControlCommandHandler")
public class ControlCommandHandler extends AbstractClientHandler {

    @Autowired
    private TerminalManage terminalManage;

    /**
     * 特定的处理器-控制处理器
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseResponse 原始消息
     * @throws ClientException
     */
    @Override
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseResponse baseResponse) throws ClientException {
        if(baseResponse.getStatus() == ResponseStatusEnum.OK){
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
                        setClientId(terminalManage.getClientInfo().getClientId());
                        setTerminalTypeEnum(TerminalTypeEnum.SLAVE);
                        setData(Boolean.TRUE);
                    }};
                    channelHandlerContext.writeAndFlush(baseRequest);

                    terminalManage.setConnectionId(baseResponse.getConnectionId());
                    logInfo("the client has agreed to the remote connection,the screen shots mission is about to begin");
                    /**
                     * 开始发送屏幕截图
                     */




                }else {

                }
            }else if(baseResponse.getTerminalTypeEnum() == TerminalTypeEnum.SERVER_2_MASTER){
                /**
                 * 主控端，拿到连接编码。
                 */
                terminalManage.setConnectionId(baseResponse.getConnectionId());
                logInfo(baseResponse.getMessage());
            }
        }else if(baseResponse.getStatus() == ResponseStatusEnum.ERROR) {
            logError(baseResponse.getMessage());
        }else {
        }
    }
}
