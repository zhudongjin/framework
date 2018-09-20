/**
 *
 */
package com.hstypay.framework.session.annotation;

import com.hstypay.framework.session.Session;

import java.lang.annotation.*;

/**
 * session检查的注解
 *
 * @author Exception
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckSession {

    //需要校验session时获取的session key的名称
    String key() default Session.SESSION_KEY;

    //需要校验session时获取session的authid名称
    String authid() default Session.SESSION_AUTHID_KEY;
}
