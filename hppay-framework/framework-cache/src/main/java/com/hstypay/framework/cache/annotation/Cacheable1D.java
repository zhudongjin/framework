package com.hstypay.framework.cache.annotation;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

/**
 * 1天缓存 24*60*60
 *
 * @author leatinfy
 */

@Cacheable(value = "defaultCache")
@CacheAttribute(expire = 86400)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable1D {

}
