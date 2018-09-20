package com.hstypay.mybatis.pagination.dialect;

import com.hstypay.mybatis.pagination.helper.SqlHelper;

/**
 * 方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月21日 上午11:44:44
 */
public abstract class AbstractDialect implements Dialect {

	public String getCountSql(final String sql) {
		return SqlHelper.getSmartCountSql(sql);
	}
}
