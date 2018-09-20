package com.hstypay.framework.cache.annotation;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

/**
 * 30s缓存
 *
 * @author leatinfy
 */
@Cacheable(value = "defaultCache")
@CacheAttribute(expire = 30)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable30S {

}
