package com.hstypay.sandbox.support.page;

import java.io.Serializable;

/**
 * 分页数据接收
 *
 * @author vincent
 * @version 1.0 2016年12月15日 下午2:12:33
 */
public class PageForm<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean reasonable;
    private Boolean rowBoundsWithCount;
    private Boolean countFlag;

    /** 页大小 */
    private int pageSize = 15;
    /** 当前页 */
    private int currentPage = 1;
    /** 排序字段名 */
    private String sortKey;
    /** 排序方式 */
    private String sortType;
    /** 实际参数 */
    private T form;

    public PageForm() {

    }

    public PageForm(int pageSize, int currentPage, String sortKey, String sortType, T form) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.sortKey = sortKey;
        this.sortType = sortType;
        this.form = form;
    }

    public PageForm(int pageSize, int currentPage, String sortKey, String sortType, Boolean reasonable, Boolean rowBoundsWithCount, Boolean countFlag, T form) {
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.sortKey = sortKey;
        this.sortType = sortType;
        this.reasonable = reasonable;
        this.rowBoundsWithCount = rowBoundsWithCount;
        this.countFlag = countFlag;
        this.form = form;
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

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public T getForm() {
        return form;
    }

    public void setForm(T form) {
        this.form = form;
    }

    public Boolean getReasonable() {
        return reasonable;
    }

    public void setReasonable(Boolean reasonable) {
        this.reasonable = reasonable;
    }

    public Boolean getRowBoundsWithCount() {
        return rowBoundsWithCount;
    }

    public void setRowBoundsWithCount(Boolean rowBoundsWithCount) {
        this.rowBoundsWithCount = rowBoundsWithCount;
    }

    public Boolean getCountFlag() {
        return countFlag;
    }

    public void setCountFlag(Boolean countFlag) {
        this.countFlag = countFlag;
    }
}
