package com.hstypay.mybatis.pagination.context;

import java.io.Serializable;

/**
 * 分页接口
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:17:40
 */
public interface Page extends Serializable {

	/**
	 * @return 当前页
	 */
	int getCurrentPage();

	/**
	 * @return 是否有下一页
	 */
	boolean isNext();

	/**
	 * @return 是否有上一页
	 */
	boolean isPrevious();

	/**
	 * 设置当当前页超出限制时是否自动调整当前页到最近的限制
	 * <p>
	 * 例如当前有10页，在查询-1页的时候，是否自动查询第1页的效果，查询11页的时候，自动变更查询第10页结果
	 * </p>
	 *
	 * @param reasonable true:自动调整 false:不调整
	 */
	void setReasonable(Boolean reasonable);

	/**
	 * 是否自动生成查询count语句，如果不是，则会根据分页id+countSuffix执行
	 *
	 * @param rowBoundsWithCount true:自动生成count语句 false:不自动生成
	 */
	void setRowBoundsWithCount(Boolean rowBoundsWithCount);

	/**
	 * @return 每页显示数据的终止行数
	 */
	int getPageEndRow();

	/**
	 * @return 页大小
	 */
	int getPageSize();

	/**
	 * @return 每页的起始行数
	 */
	int getPageStartRow();

	/**
	 * @return 总页数
	 */
	int getTotalPages();

	/**
	 * @return 总记录数
	 */
	int getTotalRows();

	/**
	 * @return 设置是否自动校正当前页
	 */
	Boolean getReasonable();

	/**
	 * @return 是否自动生成查询count语句，如果不是，则会根据分页id+countSuffix执行
	 */
	Boolean getRowBoundsWithCount();

	/**
	 * @return 是否需要查询结果总数，如果不需要，不会处理count
	 */
	Boolean getCountFlag();

    /**
     * 是否需要查询结果总数，如果不需要，不会处理count
     *
     * @param countFlag true:不会生成或者查找count语句
     */
    void setCountFlag(Boolean countFlag);

	/**
	 * 设置总页数
	 *
	 * @param i 总页数
	 */
	void setTotalPages(int i);

	/**
	 * 设置当前页
	 *
	 * @param i 当前页
	 */
	void setCurrentPage(int i);

	/**
	 * 设置是否有下一页
	 *
	 * @param b 是否有下一页
	 */
	void setNext(boolean b);

	/**
	 * 设置是否有前一页
	 *
	 * @param b 是否有前一页
	 */
	void setPrevious(boolean b);

	/**
	 * 设置每页显示数据的终止行数
	 *
	 * @param i 每页显示数据的终止行数
	 */
	void setPageEndRow(int i);

	/**
	 * 设置页大小
	 *
	 * @param i 页大小
	 */
	void setPageSize(int i);

	/**
	 * 每页的起始行数
	 *
	 * @param i 每页的起始行数
	 */
	void setPageStartRow(int i);

	/**
	 * 设置总数据数
	 *
	 * @param i 总数据数
	 */
	void setTotalRows(int i);

	/**
	 * 初始化分页参数
	 *
	 * @param rows        int 总数据
	 * @param pageSize    int 页大小
	 * @param currentPage int 当前页
	 */
	void init(int rows, int pageSize, int currentPage);
}
