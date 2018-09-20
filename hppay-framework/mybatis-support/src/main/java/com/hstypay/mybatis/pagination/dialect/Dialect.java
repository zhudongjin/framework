package com.hstypay.mybatis.pagination.dialect;

/**
 * 数据库方言支持
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:34:37
 */
public interface Dialect {

	/**
	 * 数据库本身是否支持分页当前的分页查询方式，如果数据库不支持的话，则不进行数据库分页
	 *
	 * @return true：支持当前的分页查询方式
	 */
	boolean supportsLimit();

	/**
	 * 将sql转换为分页SQL，分别调用分页sql
	 * <p>
	 * <pre>
	 * 如mysql
	 * dialect.getLimitString("select * from user", 12, ":offset",0,":limit") 将返回
	 * select * from user limit :offset,:limit
	 * </pre>
	 *
	 * @param sql    String SQL语句
	 * @param offset int 开始条数
	 * @param limit  int 每页显示多少纪录条数
	 * @return 分页查询的sql
	 */
	String getLimitString(String sql, int offset, int limit);

	/**
	 * 获取分页count sql
	 *
	 * @param sql String 原始sql
	 * @return count的sql
	 */
	String getCountSql(final String sql);
}
