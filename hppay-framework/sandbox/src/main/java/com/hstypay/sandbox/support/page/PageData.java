package com.hstypay.sandbox.support.page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页数据输出
 *
 * @author vincent
 * @version 1.0 2016年12月15日 下午2:10:33
 */
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int pageSize;

    private int currentPage;

    private int totalPages;

    private int totalRows;

    private List<T> data;

    public PageData() {

    }

    public PageData(int pageSize, int currentPage, int totalPages, int totalRows) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalRows = totalRows;
    }

    public PageData(int pageSize, int currentPage, int totalPages, int totalRows, List<T> data) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalRows = totalRows;
        this.data = data;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
