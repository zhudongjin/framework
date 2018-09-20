package com.hstypay.mybatis.pagination.dialect.support;

import com.hstypay.mybatis.pagination.dialect.AbstractDialect;

/**
 * DB2的方言实现
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:36:49
 */
public class DB2Dialect extends AbstractDialect {

	@Override
	public boolean supportsLimit() {
		return true;
	}

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return getLimitString(sql, offset, Integer.toString(offset), Integer.toString(limit));
	}

	/**
	 * 将sql变成分页sql语句,提供将offset及limit使用占位符号(placeholder)替换.
	 *
	 * @param sql               String 实际SQL语句
	 * @param offset            int 分页开始纪录条数
	 * @param offsetPlaceholder String 分页开始纪录条数－占位符号
	 * @param limitPlaceholder  String 分页纪录条数占位符号
	 * @return 包含占位符的分页sql
	 */
	public String getLimitString(String sql, int offset, String offsetPlaceholder, String limitPlaceholder) {
		int startOfSelect = sql.toLowerCase().indexOf("select");
		StringBuilder pagingSelect = new StringBuilder(sql.length() + 100)
				.append(sql.substring(0, startOfSelect)) // add the comment
				.append("select * from ( select ") // nest the main query in an outer select
				.append(getRowNumber(sql)); // add the rownnumber bit into the outer query select list
		if (hasDistinct(sql)) {
			pagingSelect.append(" row_.* from ( ") // add another (inner) nested select
					.append(sql.substring(startOfSelect)) // add the main query
					.append(" ) as row_"); // close off the inner nested select
		} else {
			pagingSelect.append(sql.substring(startOfSelect + 6)); // add the main query
		}

		pagingSelect.append(" ) as temp_ where rownumber_ ");
		// add the restriction to the outer select
		if (offset > 0) {
			// int end = offset + limit;
			String endString = offsetPlaceholder + "+" + limitPlaceholder;
			pagingSelect.append("between ").append(offsetPlaceholder).append("+1 and ").append(endString);
		} else {
			pagingSelect.append("<= ").append(limitPlaceholder);
		}

		return pagingSelect.toString();
	}

	/**
	 * 加入rownumber
	 *
	 * @param sql 查询sql
	 * @return 查询count的sql
	 */
	private static String getRowNumber(String sql) {
		StringBuilder rownumber = new StringBuilder(50).append("rownumber() over(");
		int orderByIndex = sql.toLowerCase().indexOf("order by");
		if (orderByIndex > 0 && !hasDistinct(sql)) {
			rownumber.append(sql.substring(orderByIndex));
		}
		rownumber.append(") as rownumber_,");
		return rownumber.toString();
	}

	/**
	 * 是否有distinct查询
	 *
	 * @param sql 查询sql
	 * @return true/false
	 */
	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().contains("select distinct");
	}
}
