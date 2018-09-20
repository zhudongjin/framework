package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * SQLServer的方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:52:26
 */
public class SQLServerDialect extends AbstractDialect {

	public boolean supportsLimit() {
		return true;
	}

	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		final int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

	public String getLimitString(String sql, int offset, int limit) {
		return getLimit(sql, offset, limit);
	}

	public String getLimit(String sql, int offset, int limit) {
		if (offset > 0) {
			throw new UnsupportedOperationException("sql server has no offset");
		}
		return new StringBuffer(sql.length() + 8).append(sql).insert(getAfterSelectInsertPoint(sql), " top " + limit).toString();
	}

}
