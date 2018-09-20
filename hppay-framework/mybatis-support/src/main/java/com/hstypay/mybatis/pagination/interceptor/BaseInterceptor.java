package com.hstypay.mybatis.pagination.interceptor;

import org.apache.ibatis.plugin.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 分页拦截器的抽象类
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:54:23
 */
public abstract class BaseInterceptor implements Interceptor, Serializable {

	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * mybatis 版本检测
	 */
	protected void checkVersion() {
		//MyBatis3.2.0版本校验
		try {
			Class.forName("org.apache.ibatis.scripting.xmltags.SqlNode");//SqlNode是3.2.0之后新增的类
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("MyBatis version too low, you must use above MyBatis 3.2.0 version!");
		}
	}
}
