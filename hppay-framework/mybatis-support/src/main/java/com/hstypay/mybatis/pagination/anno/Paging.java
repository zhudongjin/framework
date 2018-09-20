package com.hstypay.mybatis.pagination.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页注解，当需要对实体进行分页处理时，对其进行设置. 只支持在类声明上进行设置
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午7:58:05
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Paging {

	/**
	 * 分页对象的属性名称， 也就是在当参数传递对象时，需要进行设置的属性名称
	 *
	 * @return 分页对象的属性名称，默认page
	 */
	String field() default "page";
}
