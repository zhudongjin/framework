package com.hstypay.framework.cache.annotation;

import org.springframework.cache.annotation.Cacheable;

import java.lang.annotation.*;

/**
 * 12小时缓存 12*60*60
 *
 * @author leatinfy
 */
@Cacheable(value = "defaultCache")
@CacheAttribute(expire = 43200)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cacheable12H {

}
