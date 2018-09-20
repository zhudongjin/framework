package com.hstypay.sandbox.annotation;

import java.lang.annotation.*;

/**
 * @author Vincent
 * @version 1.0 2017-06-26 22:13
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HessianService {

    String serviceName() default "";

}

