package com.aoligei.remoter.business;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.ClientMeta;
import com.aoligei.remoter.constant.ClientConstants;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.ClientException;
import com.aoligei.remoter.manage.ClientManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wk-mia
 * 2020-9-14
 * 负责组装向服务器发起请求的参数
 */
@Component
public class RequestProcessor {

    @Autowired
    private ClientManage clientManage;

    /**
     * 连接请求
     * @return 请求主体
     * @throws ClientException
     */
    public BaseRequest buildConnectRequest() throws ClientException {
        if(StringUtils.isEmpty(clientManage.getClientInfo().getClientId())){
            throw new ClientException(ClientConstants.NEED_REGISTER);
        }
        /**
         * 向服务器发起连接时，只需指定身份识别码
         */
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(null);
            setClientId(clientManage.getClientInfo().getClientId());
            setTerminalTypeEnum(null);
            setCommandEnum(CommandEnum.CONNECT);
            setData(null);
        }};
        return baseRequest;
    }

    /**
     * 注册请求
     * @return 请求主体
     * @throws ClientException
     */
    public BaseRequest buildRegisterRequest() throws ClientException{
        /**
         * 向服务器发起注册请求时，只需指定客户端的主要信息，放入Data域中。
         */
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(null);
            setClientId(null);
            setTerminalTypeEnum(null);
            setCommandEnum(CommandEnum.REGISTER);
            setData(
                    new ClientMeta(null,clientManage.getClientInfo().getClientName(),
                            clientManage.getClientInfo().getClientSystemType(),clientManage.getClientInfo().getClientIp(),
                            clientManage.getClientInfo().getRejectConnection())
            );
        }};
        return baseRequest;
    }

    /**
     * 控制请求，该请求为MASTER发起。
     * @param slaveClientId 受控端身份识别码
     * @return 请求主体
     * @throws ClientException
     */
    public BaseRequest buildControlRequest(String slaveClientId)throws ClientException{
        /**
         * 向服务器发起控制请求时，需指定主控端的身份识别码、受控端的身份识别码、终端
         * 类型。
         */
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(null);
            setClientId(clientManage.getClientInfo().getClientId());
            setTerminalTypeEnum(TerminalTypeEnum.MASTER);
            setCommandEnum(CommandEnum.CONTROL);
            setData(slaveClientId);
        }};
        return baseRequest;
    }

}
