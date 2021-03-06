package com.aoligei.remoter.business;

import com.aoligei.remoter.event.KeyBoardEvent;
import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.BasicClientInfo;
import com.aoligei.remoter.event.MouseActionEvent;
import com.aoligei.remoter.service.driver.ScreenCatcher;
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

    /**
     * 终端管理器
     */
    private TerminalManage terminalManage;
    /**
     * 屏幕截图捕获器
     */
    private ScreenCatcher screenCatcher;

    @Autowired
    public RequestProcessor(TerminalManage terminalManage, ScreenCatcher screenCatcher){
        this.terminalManage = terminalManage;
        this.screenCatcher = screenCatcher;
    }

    /**
     * 连接请求
     * @return 请求主体
     * @throws SponsorException
     */
    public BaseRequest buildConnectRequest(){
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
    public BaseRequest buildRegisterRequest(){
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
    public BaseRequest buildControlRequest(String slaveClientId){
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
     * 应答控制请求，该请求为SLAVER发起。
     * @param connectionId 连接编码
     * @param data 应答内容
     * @return 请求主体
     * @throws SponsorException
     */
    public BaseRequest buildAnswerControlRequest(String connectionId, Object data){
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(connectionId);
            setCommandEnum(CommandEnum.CONTROL);
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(TerminalTypeEnum.SLAVE);
            setData(data);
        }};
        return baseRequest;
    }

    /**
     * 停止控制请求，该请求为MASTER/SLAVER发起。
     * @param connectionId 连接编码
     * @param typeEnum 终端类型
     * @return 请求主体
     * @throws SponsorException
     */
    public BaseRequest buildStopControlRequest(String connectionId,TerminalTypeEnum typeEnum){
        /**向服务器发起断开控制请求时，需指定连接编码*/
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(connectionId);
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(typeEnum);
            setCommandEnum(CommandEnum.STOP_CONTROL);
            setData(null);
        }};
        return baseRequest;
    }

    /**
     * 心跳请求
     * @return 请求主体
     */
    public BaseRequest buildHeartbeatRequest(){
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(null);
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(TerminalTypeEnum.UNKNOWN);
            setCommandEnum(CommandEnum.HEART_BEAT);
            setData(new Date().toString());
        }};
        return baseRequest;
    }

    /**
     * 屏幕截图请求，该请求为SLAVER发起。
     * @return 请求主体
     */
    public BaseRequest buildScreenShotsRequest(String connectionId){
        byte[] data = screenCatcher.captureScreen();
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(connectionId);
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(TerminalTypeEnum.SLAVE);
            setCommandEnum(CommandEnum.SCREEN_SHOTS);
            setData(data);
        }};
        return baseRequest;
    }

    /**
     * 键事件请求，该请求为MASTER发起。
     * @param connectionId 连接编码
     * @param event 键事件
     * @return 请求主体
     */
    public BaseRequest buildKeyBoardRequest(String connectionId, KeyBoardEvent event){
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(connectionId);
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(TerminalTypeEnum.MASTER);
            setCommandEnum(CommandEnum.KEYBOARD);
            setData(event);
        }};
        return baseRequest;
    }

    /**
     * 鼠标事件请求，该请求为MASTER发起。
     * @param connectionId 连接编码
     * @param event 鼠标动作事件
     * @return 请求主体
     */
    public BaseRequest buildMouseRequest(String connectionId, MouseActionEvent event){
        BaseRequest baseRequest = new BaseRequest(){{
            setConnectionId(connectionId);
            setClientId(terminalManage.getClientInfo().getClientId());
            setTerminalTypeEnum(TerminalTypeEnum.MASTER);
            setCommandEnum(CommandEnum.MOUSE);
            setData(event);
        }};
        return baseRequest;
    }

}
