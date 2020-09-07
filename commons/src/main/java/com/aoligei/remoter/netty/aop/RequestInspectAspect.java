package com.aoligei.remoter.netty.aop;

import com.aoligei.remoter.constant.ExceptionMessageConstants;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.NettyServerException;
import com.aoligei.remoter.netty.beans.BaseRequest;
import com.aoligei.remoter.netty.beans.ChannelCache;
import com.aoligei.remoter.netty.manage.GroupCacheManage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.util.List;

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
                case CONNECT_PARAMS_IS_COMPLETE:
                    connectParamsIsComplete(baseRequest);
                    break;
                case CONTROL_PARAMS_IS_COMPLETE:
                    controlParamsIsComplete(baseRequest);
                    break;
                case REQUEST_IS_ILLEGAL:
                    requestIsIllegal(baseRequest);
                    break;
                case CONNECTION_NOT_FIND:
                    connectionNotFind(baseRequest);
                    break;
                case MASTER_TO_SLAVES:
                    masterToSlaves(baseRequest);
                    break;
                case SLAVE_TO_MASTERS:
                    slaveToMasters(baseRequest);
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
     * 检查连接时请求参数是否齐全
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void connectParamsIsComplete(BaseRequest baseRequest)throws NettyServerException{
        if(baseRequest.getClientId() == null || "".equals(baseRequest.getClientId())){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }
        if(baseRequest.getCommandEnum() == null){
            throw new NettyServerException(ExceptionMessageConstants.COMMAND_EMPTY);
        }
    }

    /**
     * 检查控制时请求参数是否齐全
     * 当请求为主控端发来时，在Data中带受控端的身份识别码
     * 当请求为受控端发来时，在Data中带连接编码
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void controlParamsIsComplete(BaseRequest baseRequest)throws NettyServerException{
        if(baseRequest.getTerminalTypeEnum() == null){
            throw new NettyServerException(ExceptionMessageConstants.TERMINAL_TYPE_EMPTY);
        }
        if(baseRequest.getClientId() == null || "".equals(baseRequest.getClientId())){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }
        if(baseRequest.getCommandEnum() == null){
            throw new NettyServerException(ExceptionMessageConstants.COMMAND_EMPTY);
        }
        /**
         * 检查Data中是否包含数据，并不负责对数据进行校验，检查数据合法在连接处理器中
         */
        if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.MASTER){
            String slaveClientId = (String) baseRequest.getData();
            if(slaveClientId == null || "".equals(slaveClientId)){
                throw new NettyServerException(ExceptionMessageConstants.NO_SLAVER_SPECIFIED);
            }
        }
    }

    /**
     * 请求实体中合法性检查:包括身份识别码、连接编码、终端类型、命令类型
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void requestIsIllegal(BaseRequest baseRequest)throws NettyServerException{
        if(baseRequest.getClientId() == null || "".equals(baseRequest.getClientId())){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }
        if(baseRequest.getConnectionId() == null || "".equals(baseRequest.getConnectionId())){
            throw new NettyServerException(ExceptionMessageConstants.CONNECTION_ID_EMPTY);
        }
        if(baseRequest.getTerminalTypeEnum() == null){
            throw new NettyServerException(ExceptionMessageConstants.TERMINAL_TYPE_EMPTY);
        }
        if(baseRequest.getCommandEnum() == null){
            throw new NettyServerException(ExceptionMessageConstants.COMMAND_EMPTY);
        }
    }

    /**
     * 没有在缓存中找到连接
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void connectionNotFind(BaseRequest baseRequest)throws NettyServerException{
        String connectionId = baseRequest.getConnectionId();
        if(connectionId == null || "".equals(connectionId)){
            throw new NettyServerException(ExceptionMessageConstants.CONNECTION_ID_EMPTY);
        }else {
            ChannelCache channelCache = groupChannelManage.getChannelCacheByConnectionId(connectionId);
            if(channelCache == null){
                throw new NettyServerException(ExceptionMessageConstants.CONNECTION_NOT_FIND);
            }
        }
    }

    /**
     * 主控端同时控制多个受控端
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void masterToSlaves(BaseRequest baseRequest)throws NettyServerException{

        String masterClientId = baseRequest.getClientId();
        if(masterClientId == null || "".equals(masterClientId)){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }else {
            List<ChannelCache> channelCaches = groupChannelManage.getChannelCachesByMasterClientId(masterClientId);
            if(channelCaches != null) {
                if (channelCaches.size() > 1) {
                    throw new NettyServerException(ExceptionMessageConstants.NOT_SUPPORT_MASTER_CONTROL_MULTIPLE_SLAVE);
                }
            }
        }
    }

    /**
     * 受控端同时被多个主控端控制
     * @param baseRequest 原始请求
     * @throws NettyServerException 异常信息
     */
    private void slaveToMasters(BaseRequest baseRequest)throws NettyServerException{

        String slaveClientId = baseRequest.getClientId();
        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new NettyServerException(ExceptionMessageConstants.CLIENT_ID_EMPTY);
        }else {
            List<ChannelCache> channelCaches = groupChannelManage.getChannelCachesBySlaveClientId(slaveClientId);
            if(channelCaches != null) {
                if (channelCaches.size() > 1) {
                    throw new NettyServerException(ExceptionMessageConstants.NOT_SUPPORT_SLAVE_CONTROL_BY_MULTIPLE_MASTER);
                }
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
        String masterClientId = baseRequest.getClientId();
        List<ChannelCache> channelCaches = groupChannelManage.getChannelCachesByMasterClientId(masterClientId);
        if(channelCaches == null || channelCaches.size() == 0){
            throw new NettyServerException(ExceptionMessageConstants.MASTER_NOT_IN_SLAVE_GROUP);
        }
    }


}
