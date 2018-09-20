package com.hstypay.mybatis.pagination.interceptor;

import com.hstypay.mybatis.pagination.config.PageConfiguration;
import com.hstypay.mybatis.pagination.context.Page;
import com.hstypay.mybatis.pagination.context.Pagination;
import com.hstypay.mybatis.pagination.dialect.Dialect;
import com.hstypay.mybatis.pagination.helper.MSHelper;
import com.hstypay.mybatis.pagination.helper.PageHelper;
import com.hstypay.sandbox.support.PropertiesHelper;
import com.hstypay.sandbox.support.ReflectionHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库分页插件，只拦截查询语句.
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午8:02:47
 */
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PaginationInterceptor extends BaseInterceptor {

    private static final long serialVersionUID = 1L;

    // 缓存count查询的ms
    private static final Map<String, MappedStatement> msCountMap = new ConcurrentHashMap<String, MappedStatement>();

    /**
     * 初始化配置，默认配置
     */
    private PageConfiguration configuration;

    private Dialect dialect = null;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        // true为存在不分页配置，flase为不存在
        boolean isNoPage = StringUtils.isNotEmpty(this.configuration.getNoPagingPattern());
        // 拦截需要分页的SQL
        Page page = null;
        // 是否需要分页
        boolean pageFlag = false;
        // 是否需要查询结果：如果获取count为0 则不会查询结果
        boolean needQuery = true;
        // 如果是需要分页的sql和不需要分页的sql
        if (mappedStatement.getId().matches(this.configuration.getSqlPattern()) || (isNoPage && mappedStatement.getId().matches(this.configuration.getNoPagingPattern()))) {
            Object[] args = invocation.getArgs();
            Object parameter = args[1];
            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            String originalSql = boundSql.getSql().trim();
            Object parameterObject = boundSql.getParameterObject();
            if (StringUtils.isBlank(boundSql.getSql()))
                return null;

            // 1 获取方言实例
            Dialect dialect = this.getDialect(invocation);
            if (null == dialect) {
                throw new RuntimeException("can not find dialect instance !");
            }

            // 2 获取分页参数
            page = this.getPage(parameterObject);
            if (page == null) {
                throw new RuntimeException("can not find page params !");
            }

            // 1、存在不分页配置时、不匹配不分页规则且匹配分页规则时
            // 2、不存在分页配置时、匹配分页规则时
            if ((isNoPage && !mappedStatement.getId().matches(this.configuration.getNoPagingPattern()) && mappedStatement.getId().matches(this.configuration.getSqlPattern())) || (!isNoPage && mappedStatement.getId().matches(this.configuration.getSqlPattern()))) {
                // 是否需要查询count
                boolean countFlag = page.getCountFlag() == null ? this.configuration.isCountFlag() : page.getCountFlag();
                int pageSize = page.getPageSize() > 0 ? page.getPageSize() : (this.configuration.isPageSizeZero() ? 0 : 15);
                if (pageSize > 0) {
                    int totalRows = page.getTotalRows(); // 得到总记录数
                    if (totalRows <= 0 && countFlag) {
                        totalRows = this.getCount(invocation, page, args, dialect);
                    }

                    // 分页计算
                    page.setTotalRows(totalRows);
                    if (totalRows <= 0 && countFlag) {
                        needQuery = false;
                    }
                } else {
                    pageFlag = true;
                }
            }

            // 分页查询 本地化对象 修改数据库注意修改实现
            String pageSql = PageHelper.generatePageSql(originalSql, page, dialect);
            invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
            BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
            MappedStatement newMs = MSHelper.copyFromMappedStatement(mappedStatement, new MSHelper.BoundSqlSqlSource(newBoundSql));
            invocation.getArgs()[0] = newMs;
        }

        Object result = Collections.emptyList();
        if (needQuery) {
            result = invocation.proceed();
        }
        // 是否查询了所有
        if (pageFlag && result != null && page != null && page.getPageSize() <= 0 && this.configuration.isPageSizeZero() && result instanceof Collection) {
            Collection<?> collection = (Collection<?>) result;
            page.setTotalRows(collection.size());
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 校验版本
        checkVersion();
        if (properties == null) {
            properties = new Properties();
        }

        // 设置属性
        Map<String, String> map = PropertiesHelper.convertToMap(properties);
        if (map != null && map.size() > 0) {
            if (map.containsKey("dialectClass")) {
                String dialectClassName = map.get("dialectClass");
                map.remove("dialectClass");
                if (StringUtils.isNotBlank(dialectClassName)) {
                    try {
                        Class<?> dialectClass = Class.forName(dialectClassName);
                        if (Dialect.class.isAssignableFrom(dialectClass)) {
                            Dialect dialect = (Dialect) ReflectionHelper.instance(dialectClassName);
                            this.configuration.setDialect(dialect);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            configuration = PropertiesHelper.convertToClass(map, PageConfiguration.class);
            configuration.setProperties(properties);
        } else {
            configuration = new PageConfiguration();
        }

        if (!this.configuration.isAutoDialect() && !this.configuration.isAutoRuntimeDialect() && this.configuration.getDialect() == null) {
            throw new IllegalArgumentException("can not find dialect class!");
        }
    }

    /**
     * 获取分页参数
     *
     * @param parameterObject 参数对象
     * @return 分页对象
     */
    private Page getPage(Object parameterObject) {
        Page page = PageHelper.convertParameter(parameterObject);
        if (null == page) {
            page = new Pagination();
        }
        if (page.getReasonable() == null) {
            page.setReasonable(this.configuration.isReasonable());
        }
        if (page.getRowBoundsWithCount() == null) {
            page.setRowBoundsWithCount(this.configuration.isRowBoundsWithCount());
        }
        return page;
    }

    /**
     * 获取方言实现
     *
     * @param invocation 原始对象
     * @return 方言实现
     */
    private Dialect getDialect(Invocation invocation) {
        if (this.configuration.isAutoRuntimeDialect()) {
            MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
            DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
            String jdbcUrl = this.getJdbcUrl(dataSource);
            return PageHelper.getDialect(jdbcUrl);
        } else if (this.configuration.isAutoDialect()) {
            if (this.dialect == null) {
                MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
                DataSource dataSource = ms.getConfiguration().getEnvironment().getDataSource();
                String jdbcUrl = this.getJdbcUrl(dataSource);
                this.dialect = PageHelper.getDialect(jdbcUrl);
            }
            return dialect;
        }
        return this.configuration.getDialect();
    }

    /**
     * 获取数据库访问链接
     *
     * @param dataSource 数据源
     * @return 访问链接
     */
    private String getJdbcUrl(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return conn.getMetaData().getURL();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    if (this.configuration.isCloseConn()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    // ignore
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    private int getCount(Invocation invocation, Page page, Object[] args, Dialect dialect) throws Throwable {
        // 获取原始的ms
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];
        // 是否自动生成count
        boolean autoCountFlag = page.getRowBoundsWithCount() == null ? this.configuration.isRowBoundsWithCount() : page.getRowBoundsWithCount();
        BoundSql boundSql;
        String countSql;
        MappedStatement countMappedStatement;
        if (autoCountFlag) {
            countMappedStatement = mappedStatement;
            boundSql = countMappedStatement.getBoundSql(parameter);
            String originalSql = boundSql.getSql().trim();
            if (StringUtils.isBlank(boundSql.getSql()))
                return 0;
            countSql = dialect.getCountSql(originalSql);
        } else {
            countMappedStatement = msCountMap.get(mappedStatement.getId());
            if (countMappedStatement == null) {
                countMappedStatement = MSHelper.newCountMappedStatement(mappedStatement, this.configuration.getCountSuffix());
                msCountMap.put(mappedStatement.getId(), countMappedStatement);
            }
            boundSql = countMappedStatement.getBoundSql(parameter);
            countSql = boundSql.getSql().trim();
        }

        if (StringUtils.isBlank(countSql)) {
            throw new RuntimeException("can not get count sql");
        }

        Object parameterObject = boundSql.getParameterObject();
        Connection connection = countMappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(countMappedStatement.getConfiguration(), countSql, boundSql.getParameterMappings(), parameterObject);
            logger.debug("count Preparing:" + countBS.getSql());
            MSHelper.setParameters(countStmt, countMappedStatement, countBS, parameterObject);
            rs = countStmt.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            return count;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (countStmt != null) {
                countStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
