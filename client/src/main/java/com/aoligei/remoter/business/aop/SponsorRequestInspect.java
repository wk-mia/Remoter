package com.aoligei.remoter.business.aop;

import com.aoligei.remoter.enums.SponsorInspectEnum;
import java.lang.annotation.*;

/**
 * @author wk-mia
 * 2020-9-17
 * 发起请求检查注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SponsorRequestInspect {

    /**
     * 默认不检查任何项
     */
    SponsorInspectEnum[] inspectItem() default {};

}
