package com.hstypay.mybatis.pagination.helper;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 彭国卿
 * @version 1.0 2016年9月21日 下午1:26:13
 */
public class SqlHelper {

	// 缓存已经修改过的sql
	private static final Map<String, String> COUNT_SQL = new ConcurrentHashMap<String, String>();

	private static final List<SelectItem> COUNT_ITEM = new ArrayList<SelectItem>();
	private static final Alias TABLE_ALIAS = new Alias("table_count");

	static {
		COUNT_ITEM.add(new SelectExpressionItem(new Column("count(0)")));
		TABLE_ALIAS.setUseAs(false);
	}

	private SqlHelper() {

	}

	public static void isSupportedSql(String sql) {
		if (sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
			throw new RuntimeException("paging sql can not contains 'for update'");
		}
	}

	/**
	 * 获取count sql
	 *
	 * @param sql 原始sql
	 * @return 分页sql
	 */
	public static String getSmartCountSql(String sql) {
		isSupportedSql(sql);
		if (COUNT_SQL.get(sql) != null) {
			return COUNT_SQL.get(sql);
		}

		// 解析SQL
		Statement stmt;
		try {
			stmt = CCJSqlParserUtil.parse(sql);
		} catch (Throwable e) {
			// 无法解析的用一般方法返回count语句
			String countSql = getSimpleCountSql(sql);
			COUNT_SQL.put(sql, countSql);
			return countSql;
		}

		Select select = (Select) stmt;
		SelectBody selectBody = select.getSelectBody();
		// 处理body-去order by
		processSelectBody(selectBody);
		// 处理with-去order by
		processWithItemsList(select.getWithItemsList());

		// 处理为count查询
		sqlToCount(select);
		String result = select.toString();
		COUNT_SQL.put(sql, result);
		return result;
	}

	/**
	 * 简单的分页sql，在复杂sql环境中性能比较差
	 *
	 * @param sql 原始sql
	 * @return 分页sql
	 */
	public static String getSimpleCountSql(final String sql) {
		isSupportedSql(sql);
		return "select count(0) from (" + sql + ") " + TABLE_ALIAS.getName();
	}

	/**
	 * 处理selectBody去除Order by
	 *
	 * @param selectBody 查询语句片段
	 */
	public static void processSelectBody(SelectBody selectBody) {
		if (selectBody instanceof PlainSelect) {
			processPlainSelect((PlainSelect) selectBody);
		} else if (selectBody instanceof WithItem) {
			WithItem withItem = (WithItem) selectBody;
			if (withItem.getSelectBody() != null) {
				processSelectBody(withItem.getSelectBody());
			}
		} else {
			SetOperationList operationList = (SetOperationList) selectBody;
			if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
				List<SelectBody> plainSelects = operationList.getSelects();
				for (SelectBody plainSelect : plainSelects) {
					processSelectBody(plainSelect);
				}
			}
			if (!orderByHashParameters(operationList.getOrderByElements())) {
				operationList.setOrderByElements(null);
			}
		}
	}

	/**
	 * 处理PlainSelect类型的selectBody
	 *
	 * @param plainSelect 查询sql片段
	 */
	public static void processPlainSelect(PlainSelect plainSelect) {
		if (!orderByHashParameters(plainSelect.getOrderByElements())) {
			plainSelect.setOrderByElements(null);
		}
		if (plainSelect.getFromItem() != null) {
			processFromItem(plainSelect.getFromItem());
		}
		if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
			List<Join> joins = plainSelect.getJoins();
			for (Join join : joins) {
				if (join.getRightItem() != null) {
					processFromItem(join.getRightItem());
				}
			}
		}
	}

	/**
	 * 判断Orderby是否包含参数，有参数的不能去
	 *
	 * @param orderByElements 排序sql片段
	 * @return true/false
	 */
	public static boolean orderByHashParameters(List<OrderByElement> orderByElements) {
		if (orderByElements == null) {
			return false;
		}
		for (OrderByElement orderByElement : orderByElements) {
			if (orderByElement.toString().contains("?")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 处理子查询
	 *
	 * @param fromItem 子查询片段
	 */
	public static void processFromItem(FromItem fromItem) {
		if (fromItem instanceof SubJoin) {
			SubJoin subJoin = (SubJoin) fromItem;
			if (subJoin.getJoin() != null) {
				if (subJoin.getJoin().getRightItem() != null) {
					processFromItem(subJoin.getJoin().getRightItem());
				}
			}
			if (subJoin.getLeft() != null) {
				processFromItem(subJoin.getLeft());
			}
		} else if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			if (subSelect.getSelectBody() != null) {
				processSelectBody(subSelect.getSelectBody());
			}
		} else if (!(fromItem instanceof ValuesList)) {
			if (fromItem instanceof LateralSubSelect) {
				LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
				if (lateralSubSelect.getSubSelect() != null) {
					SubSelect subSelect = lateralSubSelect.getSubSelect();
					if (subSelect.getSelectBody() != null) {
						processSelectBody(subSelect.getSelectBody());
					}
				}
			}
		}
		// Table时不用处理
	}

	/**
	 * 处理WithItem
	 *
	 * @param withItemsList 子查询片段
	 */
	public static void processWithItemsList(List<WithItem> withItemsList) {
		if (withItemsList != null && withItemsList.size() > 0) {
			for (WithItem item : withItemsList) {
				processSelectBody(item.getSelectBody());
			}
		}
	}

	/**
	 * 将sql转换为count查询
	 *
	 * @param select 查询片段
	 */
	public static void sqlToCount(Select select) {
		SelectBody selectBody = select.getSelectBody();
		// 是否能简化count查询
		if (selectBody instanceof PlainSelect && isSimpleCount((PlainSelect) selectBody)) {
			((PlainSelect) selectBody).setSelectItems(COUNT_ITEM);
		} else {
			PlainSelect plainSelect = new PlainSelect();
			SubSelect subSelect = new SubSelect();
			subSelect.setSelectBody(selectBody);
			subSelect.setAlias(TABLE_ALIAS);
			plainSelect.setFromItem(subSelect);
			plainSelect.setSelectItems(COUNT_ITEM);
			select.setSelectBody(plainSelect);
		}
	}

	/**
	 * 是否可以用简单的count查询方式
	 *
	 * @param select 查询片段
	 * @return true/false
	 */
	public static boolean isSimpleCount(PlainSelect select) {
		// 包含group by的时候不可以
		if (select.getGroupByColumnReferences() != null) {
			return false;
		}
		// 包含distinct的时候不可以
		if (select.getDistinct() != null) {
			return false;
		}
		for (SelectItem item : select.getSelectItems()) {
			// select列中包含参数的时候不可以，否则会引起参数个数错误
			if (item.toString().contains("?")) {
				return false;
			}
			// 如果查询列中包含函数，也不可以，函数可能会聚合列
			if (item instanceof SelectExpressionItem) {
				if (((SelectExpressionItem) item).getExpression() instanceof Function) {
					return false;
				}
			}
		}
		return true;
	}
}
