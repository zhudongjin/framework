package com.hstypay.mybatis.pagination.context;

/**
 * 分页数据
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午6:25:51
 */
public class Pagination implements Page {

	private static final long serialVersionUID = 1L;

	/**
	 * 每页默认10条数据
	 */
	protected int pageSize = 15;
	/**
	 * 当前页
	 */
	protected int currentPage = 1;
	/**
	 * 总页数
	 */
	protected int totalPages = 0;
	/**
	 * 总数据数
	 */
	protected int totalRows = 0;
	/**
	 * 每页的起始行数
	 */
	protected int pageStartRow = 0;
	/**
	 * 每页显示数据的终止行数
	 */
	protected int pageEndRow = 0;
	/**
	 * 是否有下一页
	 */
	boolean next = false;
	/**
	 * 是否有前一页
	 */
	boolean previous = false;
	/**
	 * 是否自动调整
	 */
	Boolean reasonable;
	/**
	 * 是否自动生成查询count语句，如果不是，则会根据分页id+countSuffix执行
	 */
	Boolean rowBoundsWithCount;
	/***
	 * 是否需要查询结果总数，如果不需要，不会处理count
	 */
	Boolean countFlag;

	public Pagination() {

	}

	public Pagination(int rows, int pageSize) {
		this.init(rows, pageSize);
	}

	/**
	 * 初始化分页参数
	 *
	 * @param rows     int 总记录数
	 * @param pageSize int 页大小
	 */
	public void init(int rows, int pageSize) {
		this.pageSize = pageSize;
		this.totalRows = rows;

		if ((totalRows % pageSize) == 0) {
			totalPages = totalRows / pageSize;
		} else {
			totalPages = totalRows / pageSize + 1;
		}
	}

	@Override
	public void init(int rows, int pageSize, int currentPage) {
		this.pageSize = pageSize;
		this.totalRows = rows;

		if ((totalRows % pageSize) == 0) {
			totalPages = totalRows / pageSize;
		} else {
			totalPages = totalRows / pageSize + 1;
		}
		if (currentPage != 0)
			gotoPage(currentPage);
	}

	/**
	 * 计算当前页的取值范围：pageStartRow和pageEndRow
	 */
	private void calculatePage() {
		previous = (currentPage - 1) > 0;
		next = currentPage < totalPages;
		if (currentPage * pageSize < totalRows) { // 判断是否为最后一页
			pageEndRow = currentPage * pageSize;
			pageStartRow = pageEndRow - pageSize;
		} else {
			pageEndRow = totalRows;
			pageStartRow = pageSize * (totalPages - 1);
			if (reasonable) {
				currentPage = totalPages;
			}
		}
	}

	/**
	 * 直接跳转到指定页数的页面
	 *
	 * @param page int 指定页
	 */
	public void gotoPage(int page) {
		currentPage = page;
		calculatePage();
	}

	@Override
	public int getCurrentPage() {
		return currentPage;
	}

	@Override
	public boolean isNext() {
		return next;
	}

	@Override
	public boolean isPrevious() {
		return previous;
	}

	@Override
	public int getPageEndRow() {
		return pageEndRow;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public int getPageStartRow() {
		return pageStartRow;
	}

	@Override
	public int getTotalPages() {
		return totalPages;
	}

	@Override
	public int getTotalRows() {
		return totalRows;
	}

	@Override
	public void setTotalPages(int i) {
		totalPages = i;
	}

	@Override
	public void setCurrentPage(int i) {
		currentPage = i;
	}

	@Override
	public void setNext(boolean b) {
		next = b;
	}

	@Override
	public void setPrevious(boolean b) {
		previous = b;
	}

	@Override
	public void setPageEndRow(int i) {
		pageEndRow = i;
	}

	@Override
	public void setPageSize(int i) {
		pageSize = i;
	}

	@Override
	public void setPageStartRow(int i) {
		pageStartRow = i;
	}

	@Override
	public void setTotalRows(int i) {
		// 如果不自行查询总数，在设置完之后进行一次初始化分页
		totalRows = i;
		init(i, pageSize, currentPage);
	}

	@Override
	public void setReasonable(Boolean reasonable) {
		this.reasonable = reasonable;
	}

	@Override
	public Boolean getReasonable() {
		return this.reasonable;
	}

	public String debugString() {
		return "共有数据数:" + totalRows + "共有页数:" + totalPages + "当前页数为:" + currentPage + "是否有前一页:" + previous + "是否有下一页:" + next + "开始行数:" + pageStartRow + "终止行数:" + pageEndRow;
	}

	@Override
	public void setRowBoundsWithCount(Boolean rowBoundsWithCount) {
		this.rowBoundsWithCount = rowBoundsWithCount;
	}

	@Override
	public Boolean getRowBoundsWithCount() {
		return this.rowBoundsWithCount;
	}

	@Override
	public Boolean getCountFlag() {
		return this.countFlag;
	}

    @Override
	public void setCountFlag(Boolean countFlag) {
		this.countFlag = countFlag;
	}
}
