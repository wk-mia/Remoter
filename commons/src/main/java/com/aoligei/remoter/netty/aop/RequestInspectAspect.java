package com.aoligei.remoter.netty.aop;

import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.GroupCacheManage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
     * 当前的所有连接
     */
    @Autowired
    private GroupCacheManage groupChannelManage;

    /**
     * 指定切入点
     */
    @Pointcut("@annotation(RequestInspect)")
    public void pointcut(){
    }

    /**
     * 因为检查请求的各项配置工作都应在业务执行之前，所以所有的检查工作都应在
     * 方法执行前完成。
     * @param joinPoint 连接点
     * @throws NettyServerException 异常信息
     */
    @Before("pointcut()")
    public void beforePointcut(JoinPoint joinPoint) throws NettyServerException {
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
            throw new NettyServerException(ExceptionMessageConstants.INTERNAL_ERROR);
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
     * @throws NettyServerException 异常信息
     */
    private void inspect(InspectEnum[] inspectEnums, BaseRequest baseRequest)throws NettyServerException{
        for (int i = 0; i < inspectEnums.length; i++) {
            switch (inspectEnums[i]){
                case NO_CLEAR_CLIENT_ID:
                    noClearClientId(baseRequest);
                    break;
                case SLAVE_NOT_WORK:
                    slaveNotWork(baseRequest);
                    break;
                case MASTER_TO_SLAVES:
                    masterToSlaves(baseRequest);
                    break;
                case MASTER_NOT_IN_GROUP:
                    masterNotInGroup(baseRequest);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 请求实体中clientId字段非空检查
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void noClearClientId(BaseRequest baseRequest)throws NettyServerException{
        if(baseRequest.getClientId() == null || "".equals(baseRequest.getClientId())){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }
    }

    /**
     * 受控端没有在缓存中找到
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void slaveNotWork(BaseRequest baseRequest)throws NettyServerException{
        /**
         * 当目标客户端为空时，表示该请求是受控端发出来的，此时clientId就是受控端的身份识别码；
         * 否则，该请求是主控方发出来的，此时targetClientIds就是受控端的身份识别码。
         */
        String slaveClientId;
        if(baseRequest.getTargetClientIds() == null || baseRequest.getTargetClientIds().size() == 0){
            slaveClientId = baseRequest.getClientId();
        }else {
            slaveClientId = (String) baseRequest.getTargetClientIds().get(0);
        }

        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }else {
            if(!groupChannelManage.cache.containsKey(slaveClientId)){
                throw new NettyServerException(ExceptionMessageConstants.CLIENT_NOT_WORK);
            }
        }
    }

    /**
     * 主控端同时控制多个受控端
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void masterToSlaves(BaseRequest baseRequest)throws NettyServerException{
        if(baseRequest.getTargetClientIds() == null || baseRequest.getTargetClientIds().size() == 0){
            throw new NettyServerException(ExceptionMessageConstants.TARGET_CLIENTS_EMPTY);
        }else {
            if(baseRequest.getTargetClientIds().size() > 1){
                throw new NettyServerException(ExceptionMessageConstants.NOT_SUPPORT_MASTER_CONTROL_MULTIPLE_SLAVE);
            }
        }
    }

    /**
     * 主控端不在通道组中，表示未连接
     * @param baseRequest
     * @throws NettyServerException
     */
    private void masterNotInGroup(BaseRequest baseRequest)throws NettyServerException{
        masterToSlaves(baseRequest);
        String slaveClientId = (String) baseRequest.getTargetClientIds().get(0);
        if(slaveClientId == null || "".equals(slaveClientId) || !groupChannelManage.cache.containsKey(slaveClientId)){
            throw new NettyServerException(ExceptionMessageConstants.SLAVE_NOT_FIND);
        }else {
            boolean exist = groupChannelManage.cache.get(slaveClientId).getMasterChannels().stream()
                    .filter(item -> slaveClientId.equals(item.getClientId()))
                    .findAny().isPresent();
            if(!exist){
                throw new NettyServerException(ExceptionMessageConstants.MASTER_NOT_IN_SLAVE_GROUP);
            }
        }
    }


}
