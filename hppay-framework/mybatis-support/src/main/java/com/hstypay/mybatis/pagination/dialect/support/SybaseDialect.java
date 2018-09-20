package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * Sybase的方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:53:06
 */
public class SybaseDialect extends AbstractDialect {

	@Override
	public boolean supportsLimit() {
		return false;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return null;
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
		throw new UnsupportedOperationException("paged queries not supported");
	}

}
