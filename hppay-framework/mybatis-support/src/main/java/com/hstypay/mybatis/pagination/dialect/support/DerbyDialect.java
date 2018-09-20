package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * Derby的方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:43:40
 */
public class DerbyDialect extends AbstractDialect {

	@Override
	public boolean supportsLimit() {
		return false;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		throw new UnsupportedOperationException("paged queries not supported");
	}

}

