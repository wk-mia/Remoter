package com.aoligei.remoter.annotation;

import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.constant.MissingParamConstants;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.exception.MissingParamException;
import com.aoligei.remoter.exception.RemoterException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author wk-mia
 * 2020-9-4
 * 请求检查
 */
@Aspect
@Component
public class RequestInspectAspect {

    /**
     * 指定切入点
     */
    @Pointcut("@annotation(com.aoligei.remoter.annotation.RequestInspect)")
    public void pointcut(){
    }

    /**
     * 因为检查请求的各项配置工作都应在业务执行之前，所以所有的检查工作都应在
     * 方法执行前完成。
     * @param joinPoint 连接点
     * @throws RemoterException 异常信息
     */
    @Before("pointcut()")
    public void beforePointcut(JoinPoint joinPoint) throws RemoterException {
        /**
         * 获取到该方法的Method对象
         */
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RequestInspect requestInspect = method.getAnnotation(RequestInspect.class);
        /**
         * 获取检查项，不指定检查项表示不需要检查
         */
        InspectEnum[] inspectEnums = requestInspect.inspectItem();
        if(inspectEnums.length == 0){
            return;
        }
        /**
         * 获取原始请求，没有原始请求抛出异常
         */
        BaseRequest baseRequest = null;
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            if(joinPoint.getArgs()[i] instanceof BaseRequest){
                baseRequest = (BaseRequest)joinPoint.getArgs()[i];
            }
        }
        if(baseRequest == null){
            throw new MissingParamException(MissingParamConstants.REQUEST_CANNOT_BE_EMPTY);
        }
        /**
         * 开始各项检查
         */
        inspect(inspectEnums,baseRequest);
    }

    /**
     * 着手检查各个待检查项
     * @param inspectEnums 待检查项
     * @param baseRequest 原始请求
     * @throws RemoterException 异常信息
     */
    private void inspect(InspectEnum[] inspectEnums, BaseRequest baseRequest)throws RemoterException {
        for (int i = 0; i < inspectEnums.length; i++) {
            switch (inspectEnums[i]){
                case CLIENT_ID:
                    inspectClientId(baseRequest);
                    break;
                case CONNECTION_ID:
                    inspectConnectionId(baseRequest);
                    break;
                case COMMAND_ENUM:
                    inspectCommandEnum(baseRequest);
                    break;
                case TERMINAL_TYPE_ENUM:
                    inspectTerminalTypeEnum(baseRequest);
                    break;
                case DATA:
                    inspectData(baseRequest);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 客户端身份识别码检查
     * @param baseRequest
     */
    private void inspectClientId(BaseRequest baseRequest) throws MissingParamException{
        if(StringUtils.isEmpty(baseRequest.getClientId())){
            throw new MissingParamException(MissingParamConstants.CLIENT_ID_CANNOT_BE_EMPTY);
        }
    }

    /**
     * 连接编码检查
     * @param baseRequest
     */
    private void inspectConnectionId(BaseRequest baseRequest) throws MissingParamException{
        if(StringUtils.isEmpty(baseRequest.getConnectionId())){
            throw new MissingParamException(MissingParamConstants.CLIENT_ID_CANNOT_BE_EMPTY);
        }
    }

    /**
     * 命令类型检查
     * @param baseRequest
     */
    private void inspectCommandEnum(BaseRequest baseRequest) throws MissingParamException{
        if(baseRequest.getCommandEnum() == null){
            throw new MissingParamException(MissingParamConstants.CLIENT_ID_CANNOT_BE_EMPTY);
        }
    }

    /**
     * 终端类型检查
     * @param baseRequest
     */
    private void inspectTerminalTypeEnum(BaseRequest baseRequest) throws MissingParamException{
        if(baseRequest.getTerminalTypeEnum() == null){
            throw new MissingParamException(MissingParamConstants.CLIENT_ID_CANNOT_BE_EMPTY);
        }
    }

    /**
     * 数据区检查
     * @param baseRequest
     */
    private void inspectData(BaseRequest baseRequest) throws MissingParamException{
        if(baseRequest.getData() == null){
            throw new MissingParamException(MissingParamConstants.CLIENT_ID_CANNOT_BE_EMPTY);
        }
    }


}
