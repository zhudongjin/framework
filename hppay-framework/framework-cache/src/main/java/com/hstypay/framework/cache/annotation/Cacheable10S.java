package com.hstypay.framework.cache.annotation;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

/**
 * 10缓存
 *
 * @author leatinfy
 */

@Cacheable(value = "defaultCache")
@CacheAttribute(expire = 10)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable10S {

}
