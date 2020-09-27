package com.aoligei.remoter.business;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BasicClientInfo;
import com.aoligei.remoter.business.aop.SponsorRequestInspect;
import com.aoligei.remoter.enums.CommandEnum;
import com.aoligei.remoter.enums.SponsorInspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.manage.TerminalManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wk-mia
 * 2020-9-14
 * 负责组装向服务器发起请求的参数
 */
@Component
public class RequestProcessor {

    @Autowired
    private TerminalManage terminalManage;

    /**
     * 连接请求
     * @return 请求主体
     * @throws SponsorException
     */
    @SponsorRequestInspect(inspectItem = {SponsorInspectEnum.CLIENT_ID})
    public BaseRequest buildConnectRequest() throws SponsorException {
        /**
         * 向服务器发起连接时，只需指定身份识别码
         */
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(null);
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(null);
            setCommandEnum(CommandEnum.CONNECT);
            setData(null);
        }};
        return baseRequest;
    }

    /**
     * 注册请求
     * @return 请求主体
     * @throws SponsorException
     */
    @SponsorRequestInspect(inspectItem = {SponsorInspectEnum.CLIENT_NAME,SponsorInspectEnum.CLIENT_IP,SponsorInspectEnum.IS_REJECT_CONNECTION})
    public BaseRequest buildRegisterRequest() throws SponsorException{
        /**
         * 向服务器发起注册请求时，只需指定客户端的主要信息，放入Data域中。
         */
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(null);
            setClientId(null);
            setTerminalTypeEnum(null);
            setCommandEnum(CommandEnum.REGISTER);
            setData(
                    new BasicClientInfo(null, terminalManage.getClientInfo().getClientName(),
                            terminalManage.getClientInfo().getClientSystemType(), terminalManage.getClientInfo().getClientIp(),
                            terminalManage.getClientInfo().getRejectConnection())
            );
        }};
        return baseRequest;
    }

    /**
     * 控制请求，该请求为MASTER发起。
     * @param slaveClientId 受控端身份识别码
     * @return 请求主体
     * @throws SponsorException
     */
    @SponsorRequestInspect(inspectItem = {SponsorInspectEnum.CLIENT_ID})
    public BaseRequest buildControlRequest(String slaveClientId)throws SponsorException{
        /**
         * 向服务器发起控制请求时，需指定主控端的身份识别码、受控端的身份识别码、终端
         * 类型。
         */
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(null);
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(TerminalTypeEnum.MASTER);
            setCommandEnum(CommandEnum.CONTROL);
            setData(slaveClientId);
        }};
        return baseRequest;
    }

    /**
     * 心跳请求
     * @return 请求主体
     * @throws SponsorException
     */
    public BaseRequest buildHeartbeatRequest()throws SponsorException{
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(terminalManage.getConnectionId());
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(TerminalTypeEnum.UNKNOWN);
            setCommandEnum(CommandEnum.HEART_BEAT);
            setData(new Date().toString());
        }};
        return baseRequest;
    }

}
