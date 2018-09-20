package com.hstypay.framework.cache.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheAttribute {

    /**
     * 过期时间，单位秒
     *
     * @return
     */
    int expire() default 0;
}
