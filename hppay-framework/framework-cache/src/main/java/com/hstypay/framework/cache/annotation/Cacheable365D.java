package com.hstypay.framework.cache.annotation;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

/**
 * 365天缓存,365*24*60*60
 *
 * @author leatinfy
 */
@Cacheable(value = "defaultCache")
@CacheAttribute(expire = 31536000)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable365D {

}
