package com.hstypay.mybatis.pagination.dialect;

import com.hstypay.mybatis.pagination.dialect.support.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 方言自动获取工具
 *
 * @author 彭国卿
 * @version 1.0 2016年9月19日 下午1:47:21
 */
public class DialectHelper {

	private static final Map<String, Class<? extends Dialect>> DIALECT_MAP = new ConcurrentHashMap<String, Class<? extends Dialect>>();

	static {
		publishDialect("db2", DB2Dialect.class);
		publishDialect("derby", DerbyDialect.class);
		publishDialect("h2", H2Dialect.class);
		publishDialect("hsqldb", HSQLDialect.class);
		publishDialect("mysql", MySQLDialect.class);
		publishDialect("oracle", OracleDialect.class);
		publishDialect("postgresql", PostgreSQLDialect.class);
		publishDialect("sqlserver2005", SQLServer2005Dialect.class);
		publishDialect("sqlserver", SQLServerDialect.class);
		publishDialect("sybase", SybaseDialect.class);
	}

	private DialectHelper() {

	}

	/**
	 * 发布方言
	 *
	 * @param name 名称
	 * @param dialectClass 实现类class
	 */
	public static void publishDialect(String name, Class<? extends Dialect> dialectClass) {
		if (DIALECT_MAP.containsKey(name)) {
			throw new IllegalArgumentException("Dialect " + name + " already exist!");
		}
		DIALECT_MAP.put(name, dialectClass);
	}

	/**
	 * @return 方言简写列表
	 */
	public static List<String> getDialectNames() {
		List<String> names = new ArrayList<String>();
		names.addAll(DIALECT_MAP.keySet());
		return names;
	}

	/**
	 * @param name 名称
	 * @return 方言class
	 */
	public static Class<? extends Dialect> getDialect(String name) {
		if (DIALECT_MAP.containsKey(name)) {
			return DIALECT_MAP.get(name);
		}
		return null;
	}

	/**
	 * 根据链接获取方言class
	 *
	 * @param jdbcUrl 数据库连接url
	 * @return 方言class
	 */
	public static Class<? extends Dialect> getAutoDialect(String jdbcUrl) {
		List<String> dialectNames = getDialectNames();
		for (String name : dialectNames) {
			if (jdbcUrl.contains(":" + name + ":")) {
				return getDialect(name);
			}
		}
		return null;
	}
}
