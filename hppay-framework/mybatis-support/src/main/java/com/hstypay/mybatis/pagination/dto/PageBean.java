package com.hstypay.mybatis.pagination.dto;

import com.hstypay.mybatis.pagination.context.Page;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页包装
 *
 * @author 彭国卿
 * @version 1.0 2016年9月21日 下午3:22:08
 */
@Getter
@Setter
public class PageBean<T> {

	private int pageSize;

	private int currentPage;

	private int totalPages;

	private int totalRows;

	private List<T> data;
	
	public static <T> PageBean<T> toPage(Page page, List<T> data) {
		return new PageBean<T>(page, data);
	}
	
	public PageBean(Page page, List<T> data) {
		this(page.getPageSize(), page.getCurrentPage(), page.getTotalPages(), page.getTotalRows(), data);
	}
	
	public PageBean(int pageSize, int currentPage, int totalPages, int totalRows, List<T> data) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.totalRows = totalRows;
		this.data = data;
	}
}
