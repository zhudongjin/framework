package com.hstypay.framework.cache.annotation;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

/**
 * 30天缓存 30*24*60*60
 *
 * @author leatinfy
 */
@Cacheable(value = "defaultCache")
@CacheAttribute(expire = 2592000)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable30D {

}
