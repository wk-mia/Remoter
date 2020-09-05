package com.aoligei.remoter.netty.aop;

import java.lang.annotation.*;

/**
 * @author wk-mia
 * 2020-9-4
 * 请求检查注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestInspect {

    /**
     * 默认只检查客户端身份识别码为空的情况
     */
    InspectEnum[] inspectItem() default {InspectEnum.NO_CLEAR_CLIENT_ID};

}
