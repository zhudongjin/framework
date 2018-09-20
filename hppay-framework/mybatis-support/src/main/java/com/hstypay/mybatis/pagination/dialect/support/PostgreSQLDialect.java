package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * PostgreSQL的方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:49:50
 */
public class PostgreSQLDialect extends AbstractDialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
		StringBuilder pageSql = new StringBuilder().append(sql);
		pageSql = offset <= 0 ? pageSql.append(" limit ").append(limitPlaceholder) : pageSql.append(" limit ").append(limitPlaceholder).append(" offset ").append(offsetPlaceholder);
		return pageSql.toString();
	}

}
