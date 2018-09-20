package com.hstypay.framework.web.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Length {

	public int max();
	public int min() default 1;
	
	/** 错误提示消息
	 * 
	 * @return
	 */
	public String message();
}
