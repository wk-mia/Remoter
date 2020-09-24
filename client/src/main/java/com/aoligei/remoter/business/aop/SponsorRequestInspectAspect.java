package com.aoligei.remoter.business.aop;

import com.aoligei.remoter.constant.SponsorConstants;
import com.aoligei.remoter.enums.SponsorInspectEnum;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.manage.TerminalManage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.lang.reflect.Method;

/**
 * @author wk-mia
 * 2020-9-17
 * 发起请求检查
 */
@Aspect
@Component
public class SponsorRequestInspectAspect {

    @Autowired
    private TerminalManage terminalManage;

    /**
     * 指定切入点
     */
    @Pointcut("@annotation(com.aoligei.remoter.business.aop.SponsorRequestInspect)")
    public void pointcut(){
    }

    /**
     * 因为检查请求的各项配置工作都应在业务执行之前，所以所有的检查工作都应在
     * 方法执行前完成。
     * @param joinPoint 连接点
     * @throws SponsorException 异常信息
     */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) throws SponsorException{
        /**
         * 获取到该方法的Method对象
         */
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        SponsorRequestInspect sponsorRequestInspect = method.getAnnotation(SponsorRequestInspect.class);
        /**
         * 获取检查项，不指定检查项表示不需要检查
         */
        SponsorInspectEnum[] inspectEnums = sponsorRequestInspect.inspectItem();
        if(inspectEnums.length == 0){
            return;
        }
        /**
         * 开始检查
         */
        inspect(inspectEnums);
    }

    /**
     * 检查各个待检查项
     * @param inspectEnums 待检查项
     * @throws SponsorException 异常信息
     */
    private void inspect(SponsorInspectEnum[] inspectEnums) throws SponsorException{
        for (int i = 0; i < inspectEnums.length; i++){
            switch (inspectEnums[i]){
                case CLIENT_ID:
                    inspectClientId();
                    break;
                case CONNECTION_ID:
                    inspectConnectionId();
                    break;
                case CLIENT_IP:
                    inspectClientIp();
                    break;
                case CLIENT_NAME:
                    inspectClientName();
                    break;
                case IS_REJECT_CONNECTION:
                    inspectIsRejectConnection();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 客户端信息非空检查
     * @throws SponsorException
     */
    private void inspectClientInfo()throws SponsorException{
        if(terminalManage == null || terminalManage.getClientInfo() == null){
            throw new SponsorException(SponsorConstants.CLIENT_INFO_NULL);
        }
    }

    /**
     * 客户端身份识别码非空检查
     * @throws SponsorException
     */
    private void inspectClientId()throws SponsorException{
        this.inspectClientInfo();
        if(StringUtils.isEmpty(terminalManage.getClientInfo().getClientId())){
            throw new SponsorException(SponsorConstants.CLIENT_ID_NULL);
        }
    }

    /**
     * 连接编码非空检查
     * @throws SponsorException
     */
    private void inspectConnectionId()throws SponsorException{
        if(terminalManage == null || StringUtils.isEmpty(terminalManage.getConnectionId())){
            throw new SponsorException(SponsorConstants.CONNECTION_ID_NULL);
        }
    }

    /**
     * 客户端ip地址非空检查
     * @throws SponsorException
     */
    private void inspectClientIp()throws SponsorException{
        this.inspectClientInfo();
        if(StringUtils.isEmpty(terminalManage.getClientInfo().getClientIp())){
            throw new SponsorException(SponsorConstants.CLIENT_IP_NULL);
        }
    }

    /**
     * 客户端名称非空检查
     * @throws SponsorException
     */
    private void inspectClientName()throws SponsorException{
        this.inspectClientInfo();
        if(StringUtils.isEmpty(terminalManage.getClientInfo().getClientName())){
            throw new SponsorException(SponsorConstants.CLIENT_NAME_NULL);
        }
    }

    /**
     * 客户端是否拒绝所有控制请求非空检查
     * @throws SponsorException
     */
    private void inspectIsRejectConnection()throws SponsorException{
        this.inspectClientInfo();
        if(terminalManage.getClientInfo().getRejectConnection() == null){
            throw new SponsorException(SponsorConstants.IS_REJECT_CONNECTION_NULL);
        }
    }
}
