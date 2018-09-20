package com.hstypay.mybatis.pagination.helper;

import com.hstypay.mybatis.pagination.anno.Paging;
import com.hstypay.mybatis.pagination.context.Page;
import com.hstypay.mybatis.pagination.dialect.Dialect;
import com.hstypay.mybatis.pagination.dialect.DialectHelper;
import com.hstypay.sandbox.support.ReflectionHelper;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 分页相关工具类
 *
 * @author 彭国卿
 * @version 1.0 2016年9月19日 下午4:21:40
 */
public class PageHelper {

	// 缓存方言实例
	private static final Map<String, Dialect> DIALECT_MAP = new HashMap<String, Dialect>();

	private static ReentrantLock lock = new ReentrantLock();

	private PageHelper() {

	}

	/**
	 * 根据jdbc访问链接获取方言实例
	 *
	 * @param jdbcUrl jdbc链接
	 * @return 方言实现
	 */
	public static Dialect getDialect(String jdbcUrl) {
		if (DIALECT_MAP.containsKey(jdbcUrl)) {
			return DIALECT_MAP.get(jdbcUrl);
		}

		try {
			lock.lock();
			if (DIALECT_MAP.containsKey(jdbcUrl)) {
				return DIALECT_MAP.get(jdbcUrl);
			}

			if (StringUtils.isEmpty(jdbcUrl)) {
				throw new IllegalAccessError("can not get jdbcUrl automatic, please set dialect");
			}

			Class<? extends Dialect> dialectClass = DialectHelper.getAutoDialect(jdbcUrl);
			if (dialectClass == null) {
				throw new IllegalAccessError("can not get dialectClass from jdbcUrl :" + dialectClass);
			}

			Dialect dialect = dialectClass.newInstance();
			DIALECT_MAP.put(jdbcUrl, dialect);
			return dialect;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 分页参数设置
	 *
	 * @param parameterObject 参数对象
	 * @return 分页对象
	 */
	public static Page convertParameter(Object parameterObject) {
		if (parameterObject == null) {
			return null;
		}

		if (parameterObject instanceof Page) {
			return (Page) parameterObject;
		}

		// 参数为某个实体，该实体拥有Page属性
		Paging paging = parameterObject.getClass().getAnnotation(Paging.class);
		if (paging == null) {
			return null;
		}
		String field = paging.field();
		Field pageField = ReflectionHelper.getAccessibleField(parameterObject, field);
		if (pageField == null) {
			return null;
		}

		return (Page) ReflectionHelper.getFieldValue(parameterObject, field);
	}

	/**
	 * 生成分页sql
	 *
	 * @param sql 原始sql
	 * @param page 分页对象
	 * @param dialect 方言实现
	 * @return 分页sql
	 */
	public static String generatePageSql(String sql, Page page, Dialect dialect) {
		if (dialect.supportsLimit()) {
			int pageSize = page.getPageSize();
			if (pageSize <= 0) {
				return sql;
			}
			int index = (page.getCurrentPage() - 1) * pageSize;
			int start = index < 0 ? 0 : index;
			return dialect.getLimitString(sql, start, pageSize);
		} else {
			return sql;
		}
	}

}
