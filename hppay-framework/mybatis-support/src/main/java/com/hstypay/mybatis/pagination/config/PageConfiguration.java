package com.hstypay.mybatis.pagination.config;

import com.hstypay.mybatis.pagination.dialect.Dialect;
import lombok.Data;

import java.util.Properties;

/**
 * mybatis分页插件相关配置
 *
 * @author 彭国卿
 * @version 1.0 2016年9月19日 下午12:22:55
 */
@Data
public class PageConfiguration {

	/** 需要分页拦截的ID，在mapper中的id，可以匹配正则 */
	protected static final String _SQL_PATTERN = ".*Page*.*";
	/** 不需要分页拦截的ID，在mapper中的id，可以匹配正则 */
	protected static final String _NO_PAGING_PATTERN = ".*NoPage*.*";
	/** 分页counter的id后缀 */
	protected static final String _PAGEING_COUNTER_SUFFIX = "Count";

	/** 原始properties */
	private Properties properties;

	/** 分页方言实现 */
	private Dialect dialect;
	/** 自动获取dialect, 如果为true,dialectClass设置无效，适合在单数据库源单种数据库的时候使用 */
	private boolean autoDialect = true;
	/** 运行时自动获取dialect,适合在多数据源多中数据库时使用 */
	private boolean autoRuntimeDialect = false;
	/** 默认是否自动调整页码 */
	private boolean reasonable = false;
	/** 分页id正则 */
	private String sqlPattern = _SQL_PATTERN;
	/** 不分页id正则 */
	private String noPagingPattern = _NO_PAGING_PATTERN;
	/** count后缀 */
	private String countSuffix = _PAGEING_COUNTER_SUFFIX;
	/** 根据jdbcurl自动获取方言实现后是否关闭数据源 */
	private boolean closeConn = true;
	/** 是否自动生成查询count语句，如果不是，则会根据分页id+countSuffix执行 */
	private boolean rowBoundsWithCount = true;
	/** 是否需要去查询总数 */
	private boolean countFlag = false;
	/** 当设置为true的时候，如果pagesize设置为0（或RowBounds的limit=0），就不执行分页 */
	private boolean pageSizeZero = true;
}
