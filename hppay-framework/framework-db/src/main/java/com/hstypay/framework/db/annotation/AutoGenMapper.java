package com.hstypay.framework.db.annotation;

import java.lang.annotation.*;

/**
 * 定义Mybatis自动生成DAO的扫描注解
 *
 * @author Tinnfy Lee
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AutoGenMapper {

}
