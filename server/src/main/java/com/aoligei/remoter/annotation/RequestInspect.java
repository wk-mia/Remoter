package com.aoligei.remoter.annotation;

import com.aoligei.remoter.enums.InspectEnum;
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
     * 默认不检查任何项
     */
    InspectEnum[] inspectItem() default {};

}
