package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * H2Dialect
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:45:17
 */
public class H2Dialect extends AbstractDialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
	}

	private String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		return sql + ((offset > 0) ? " limit " + limitPlaceholder + " offset " + offsetPlaceholder : " limit " + limitPlaceholder);
	}
}
