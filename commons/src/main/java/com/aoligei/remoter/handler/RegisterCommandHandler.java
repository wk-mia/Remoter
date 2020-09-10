package com.aoligei.remoter.handler;

import com.aoligei.remoter.aop.RequestInspect;
import com.aoligei.remoter.beans.BaseResponse;
import com.aoligei.remoter.beans.ClientMeta;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.manage.ClientMetaManage;
import com.aoligei.remoter.util.BuildUtil;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wk-mia
 * 2020-9-7
 * 点对点模式服务端-注册处理器
 * 负责注册一个用户，生成客户端身份识别码并返回给用户；该客户端身份识别码作为客户端的唯一标识，
 * 与客户端进行交互。客户端请求注册时，在Data中附带客户端名称、客户端的系统类型、客户端IP、此
 * 客户端是否已拒绝所有连接请求等信息。
 */
public class RegisterCommandHandler extends AbstractServerCensorC2CHandler {

    /**
     * 客户端信息账册管理器
     */
    @Autowired
    private ClientMetaManage clientMetaManage;

    /**
     * 特定的处理器：注册处理器
     * 检查项 = {REGISTER_PARAMS}
     * @param channelHandlerContext 当前连接的处理器上下文
     * @param baseRequest 原始消息
     * @throws ServerException
     */
    @Override
    @RequestInspect(inspectItem = {InspectEnum.REGISTER_PARAM})
    protected void particularHandle(ChannelHandlerContext channelHandlerContext, BaseRequest baseRequest) throws ServerException {
        ClientMeta clientMeta = (ClientMeta) baseRequest.getData();
        clientMetaManage.register(clientMeta);
        BaseResponse baseResponse = BuildUtil.buildResponseOK(null, TerminalTypeEnum.SERVER,
                CommandEnum.REGISTER,clientMeta.getClientId(),"the server accepted the registration request");
        channelHandlerContext.writeAndFlush(baseResponse);
    }
}
