package com.aoligei.remoter.aop;

import com.aoligei.remoter.beans.ClientMeta;
import com.aoligei.remoter.constant.IncompleteParamConstants;
import com.aoligei.remoter.constant.ServerExceptionConstants;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.TerminalTypeEnum;
import com.aoligei.remoter.exception.IncompleteParamException;
import com.aoligei.remoter.exception.ServerException;
import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.beans.ChannelCache;
import com.aoligei.remoter.manage.GroupCacheManage;
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
     * @throws ServerException 异常信息
     */
    @Before("pointcut()")
    public void beforePointcut(JoinPoint joinPoint) throws ServerException {
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
            throw new IncompleteParamException(IncompleteParamConstants.REQUEST_NULL);
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
     * @throws ServerException 异常信息
     */
    private void inspect(InspectEnum[] inspectEnums, BaseRequest baseRequest)throws ServerException {
        for (int i = 0; i < inspectEnums.length; i++) {
            switch (inspectEnums[i]){
                case REGISTER_PARAM:
                    registerParamsIsComplete(baseRequest);
                    break;
                case CONNECT_PARAMS:
                    connectParamsIsComplete(baseRequest);
                    break;
                case CONTROL_PARAMS:
                    controlParamsIsComplete(baseRequest);
                    break;
                case ORDINARY_PARAMS:
                    ordinaryParamsIsComplete(baseRequest);
                    break;
                case DISCONNECT_PARAMS:
                    disconnectParamsIsComplete(baseRequest);
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
                default:
                    break;
            }
        }
    }

    /**
     * 检查注册时请求参数是否齐全
     * @param baseRequest 原始请求
     * @throws IncompleteParamException 异常信息
     */
    private void registerParamsIsComplete(BaseRequest baseRequest)throws IncompleteParamException{
        try{
            /**
             * 获取Data中的客户端信息
             */
            ClientMeta clientMeta = (ClientMeta) baseRequest.getData();
            if(clientMeta == null){
                throw new IncompleteParamException(IncompleteParamConstants.DATA_NULL);
            }else {
                if(clientMeta.getClientName() == null || "".equals(clientMeta.getClientName())){
                    throw new IncompleteParamException(IncompleteParamConstants.CLIENT_NAME_NOT_IN_DATA);
                }
                if(clientMeta.getClientIp() == null || "".equals(clientMeta.getClientIp())){
                    throw new IncompleteParamException(IncompleteParamConstants.CLIENT_IP_NOT_IN_DATA);
                }
                if(clientMeta.getRejectConnection() == null){
                    throw new IncompleteParamException(IncompleteParamConstants.REJECT_CONNECTION_NOT_IN_DATA);
                }
            }
        }catch (Exception e){
            throw new IncompleteParamException(e.getMessage());
        }
    }

    /**
     * 检查连接时请求参数是否齐全
     * @param baseRequest 原始请求
     * @throws IncompleteParamException 异常信息
     */
    private void connectParamsIsComplete(BaseRequest baseRequest)throws IncompleteParamException {
        if(baseRequest.getClientId() == null || "".equals(baseRequest.getClientId())){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        if(baseRequest.getCommandEnum() == null){
            throw new IncompleteParamException(IncompleteParamConstants.COMMAND_NULL);
        }

    }

    /**
     * 检查控制时请求参数是否齐全
     * 当请求为主控端发来时，在Data中带受控端的身份识别码
     * 当请求为受控端发来时，在Data中带连接编码
     * @param baseRequest 原始请求
     * @throws IncompleteParamException 异常信息
     */
    private void controlParamsIsComplete(BaseRequest baseRequest)throws IncompleteParamException {
        if(baseRequest.getTerminalTypeEnum() == null){
            throw new IncompleteParamException(IncompleteParamConstants.TERMINAL_TYPE_NULL);
        }
        if(baseRequest.getClientId() == null || "".equals(baseRequest.getClientId())){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        if(baseRequest.getCommandEnum() == null){
            throw new IncompleteParamException(IncompleteParamConstants.COMMAND_NULL);
        }
        /**
         * 检查Data中是否包含数据，并不负责对数据进行校验，检查数据合法在连接处理器中
         */
        if(baseRequest.getTerminalTypeEnum() == TerminalTypeEnum.MASTER){
            String slaveClientId = (String) baseRequest.getData();
            if(slaveClientId == null || "".equals(slaveClientId)){
                throw new IncompleteParamException(IncompleteParamConstants.NO_SLAVER_SPECIFIED);
            }
        }
    }

    /**
     * 请求实体中合法性检查:包括身份识别码、连接编码、终端类型、命令类型
     * @param baseRequest 原始请求
     * @throws IncompleteParamException 异常信息
     */
    private void ordinaryParamsIsComplete(BaseRequest baseRequest)throws IncompleteParamException {
        if(baseRequest.getClientId() == null || "".equals(baseRequest.getClientId())){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }
        if(baseRequest.getConnectionId() == null || "".equals(baseRequest.getConnectionId())){
            throw new IncompleteParamException(IncompleteParamConstants.CONNECTION_ID_NULL);
        }
        if(baseRequest.getTerminalTypeEnum() == null){
            throw new IncompleteParamException(IncompleteParamConstants.TERMINAL_TYPE_NULL);
        }
        if(baseRequest.getCommandEnum() == null){
            throw new IncompleteParamException(IncompleteParamConstants.COMMAND_NULL);
        }
    }

    /**
     * 断开连接时请求参数是否齐全
     * @param baseRequest 原始请求
     * @throws IncompleteParamException 异常信息
     */
    private void disconnectParamsIsComplete(BaseRequest baseRequest)throws IncompleteParamException {

    }

    /**
     * 没有在缓存中找到连接
     * @param baseRequest 原始请求
     * @throws ServerException 异常信息
     */
    private void connectionNotFind(BaseRequest baseRequest)throws ServerException {
        String connectionId = baseRequest.getConnectionId();
        if(connectionId == null || "".equals(connectionId)){
            throw new IncompleteParamException(IncompleteParamConstants.CONNECTION_ID_NULL);
        }else {
            ChannelCache channelCache = groupChannelManage.getChannelCacheByConnectionId(connectionId);
            if(channelCache == null){
                throw new ServerException(ServerExceptionConstants.CONNECTION_NOT_FIND);
            }
        }
    }

    /**
     * 主控端同时控制多个受控端
     * @param baseRequest 原始请求
     * @throws ServerException 异常信息
     */
    private void masterToSlaves(BaseRequest baseRequest)throws ServerException{

        String masterClientId = baseRequest.getClientId();
        if(masterClientId == null || "".equals(masterClientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }else {
            List<ChannelCache> channelCaches = groupChannelManage.getChannelCachesByMasterClientId(masterClientId);
            if(channelCaches != null) {
                if (channelCaches.size() > 1) {
                    throw new ServerException(ServerExceptionConstants.NOT_SUPPORT_MASTER_CONTROL_MULTIPLE_SLAVE);
                }
            }
        }
    }

    /**
     * 受控端同时被多个主控端控制
     * @param baseRequest 原始请求
     * @throws ServerException 异常信息
     */
    private void slaveToMasters(BaseRequest baseRequest)throws ServerException{

        String slaveClientId = baseRequest.getClientId();
        if(slaveClientId == null || "".equals(slaveClientId)){
            throw new IncompleteParamException(IncompleteParamConstants.CLIENT_ID_NULL);
        }else {
            List<ChannelCache> channelCaches = groupChannelManage.getChannelCachesBySlaveClientId(slaveClientId);
            if(channelCaches != null) {
                if (channelCaches.size() > 1) {
                    throw new ServerException(ServerExceptionConstants.NOT_SUPPORT_SLAVE_CONTROL_BY_MULTIPLE_MASTER);
                }
            }
        }
    }

    /**
     * 主控端不在通道组中，表示未连接
     * @param baseRequest
     * @throws ServerException
     */
    private void masterNotInGroup(BaseRequest baseRequest)throws ServerException{
        masterToSlaves(baseRequest);
        String masterClientId = baseRequest.getClientId();
        List<ChannelCache> channelCaches = groupChannelManage.getChannelCachesByMasterClientId(masterClientId);
        if(channelCaches == null || channelCaches.size() == 0){
            throw new ServerException(ServerExceptionConstants.MASTER_NOT_IN_SLAVE_GROUP);
        }
    }






}
