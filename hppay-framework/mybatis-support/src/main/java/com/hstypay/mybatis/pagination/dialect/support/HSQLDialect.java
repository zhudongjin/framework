package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * HSQ方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:47:32
 */
public class HSQLDialect extends AbstractDialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
		boolean hasOffset = offset > 0;
		return new StringBuffer(sql.length() + 10).append(sql).insert(sql.toLowerCase().indexOf("select") + 6, hasOffset ? " limit " + offsetPlaceholder + " " + limitPlaceholder : " top " + limitPlaceholder).toString();
	}
}
