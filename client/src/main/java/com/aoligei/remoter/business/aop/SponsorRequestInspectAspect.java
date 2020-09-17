package com.aoligei.remoter.business.aop;

import com.aoligei.remoter.aop.RequestInspect;
import com.aoligei.remoter.beans.BaseRequest;
import com.aoligei.remoter.enums.InspectEnum;
import com.aoligei.remoter.enums.SponsorInspectEnum;
import com.aoligei.remoter.exception.SponsorException;
import com.aoligei.remoter.manage.ClientManage;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private ClientManage clientManage;

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
    }
}
