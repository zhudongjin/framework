package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * Oracle的方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:49:08
 */
public class OracleDialect extends AbstractDialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
	}

	public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
		sql = sql.trim();
		boolean isForUpdate = false;
		if (sql.toLowerCase().endsWith(" for update")) {
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		}
		StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
		if (offset > 0) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		} else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (offset > 0) {
			String endString = offsetPlaceholder + "+" + limitPlaceholder;
			pagingSelect.append(" ) row_ ) where rownum_ <= ").append(endString).append(" and rownum_ > ").append(offsetPlaceholder);
		} else {
			pagingSelect.append(" ) where rownum <= ").append(limitPlaceholder);
		}

		if (isForUpdate) {
			pagingSelect.append(" for update");
		}

		return pagingSelect.toString();
	}
}
