package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * mysql的方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:48:25
 */
public class MySQLDialect extends AbstractDialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}
	
	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
		StringBuilder stringBuilder = new StringBuilder(sql);
		stringBuilder.append(" limit ");
		if (offset > 0) {
			stringBuilder.append(offsetPlaceholder).append(",").append(limitPlaceholder);
		} else {
			stringBuilder.append(limitPlaceholder);
		}
		return stringBuilder.toString();
	}

}
