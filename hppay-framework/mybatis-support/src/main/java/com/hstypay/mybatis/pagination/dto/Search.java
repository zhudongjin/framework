package com.hstypay.mybatis.pagination.dto;

import com.hstypay.mybatis.pagination.anno.Paging;
import com.hstypay.mybatis.pagination.context.Page;
import com.hstypay.mybatis.pagination.context.Pagination;
import com.hstypay.sandbox.support.BeanConvertHelper;
import com.hstypay.sandbox.support.BeanToMapHelper;
import com.hstypay.sandbox.support.page.PageData;
import com.hstypay.sandbox.support.page.PageForm;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一般用于分页查询的dto
 *
 * @author 彭国卿
 * @version 1.0 2016年9月19日 下午6:16:03
 */
@Paging
public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(Search.class);

    /**
     * 分页信息
     */
    private Page page;
    /**
     * 排序字段名
     */
    private String sortKey;
    /**
     * 排序方式
     */
    private String sortType;
    /**
     * 查询条件
     */
    private Map<String, Object> search = new HashMap<String, Object>();

    public static <T> Search toSearchObject(T t) {
        if (t instanceof PageForm) {
            return toSearch((PageForm) t);
        }
        Search res = new Search();
        try {
            Map<String, Object> search = BeanToMapHelper.convertBean(t);
            res.setSearch(search);
        } catch (Exception e) {
            logger.error("transeform search error", e);
        }
        return res;
    }

    public static <T extends PageForm> Search toSearch(T t) {
        return toSearch(t.getForm(), t.getCurrentPage(), t.getPageSize(), t.getSortKey(), t.getSortType(), t.getReasonable(), t.getRowBoundsWithCount(), t.getCountFlag());
    }

    public static <T extends PageForm> Search toSearch(T t, Boolean reasonable, Boolean rowBoundsWithCount) {
        return toSearch(t.getForm(), t.getCurrentPage(), t.getPageSize(), t.getSortKey(), t.getSortType(), reasonable, rowBoundsWithCount, null);
    }

    public static <T extends PageForm> Search toSearch(T t, Boolean reasonable, Boolean rowBoundsWithCount, Boolean countFlag) {
        return toSearch(t.getForm(), t.getCurrentPage(), t.getPageSize(), t.getSortKey(), t.getSortType(), reasonable, rowBoundsWithCount, countFlag);
    }

    /**
     * 转换分页信息
     *
     * @param page int 当前页
     * @return 查询对象
     */
    public static Search toSearch(int page) {
        return toSearch(null, page, 15);
    }

    /**
     * 转换分页信息
     *
     * @param page     int 当前页
     * @param pageSize int 页大小
     * @return 查询对象
     */
    public static Search toSearch(int page, int pageSize) {
        return toSearch(null, page, pageSize);
    }

    /**
     * 转换分页信息
     *
     * @param t    dto的javabean对象
     * @param page int 当前页
     * @return 查询对象
     */
    public static <T> Search toSearch(T t, int page) {
        return toSearch(t, page, 15);
    }

    /**
     * 转换分页信息
     *
     * @param t        dto的javabean对象
     * @param page     Integer 当前页
     * @param pageSize Integer 页大小
     * @return 查询对象
     */
    public static <T> Search toSearch(T t, int page, int pageSize) {
        return toSearch(t, page, pageSize, null, null);
    }

    /**
     * 转换分页信息
     *
     * @param t        dto的javabean对象
     * @param page     Integer 当前页
     * @param pageSize Integer 页大小
     * @param sortKey  String 排序key
     * @param sortType String 排序方式
     * @return 查询对象
     */
    public static <T> Search toSearch(T t, int page, int pageSize, String sortKey, String sortType) {
        return toSearch(t, page, pageSize, sortKey, sortType, null, null, null);
    }

    /**
     * 转换分页信息
     *
     * @param t                  dto的javabean对象
     * @param page               int 当前页
     * @param pageSize           int 页大小
     * @param sortKey            String 排序key
     * @param sortType           String 排序方式
     * @param reasonable         Boolean 是否自动调整
     * @param rowBoundsWithCount Boolean 是否自动分页
     * @param countFlag          Boolean 是否查找分页语句
     * @return 查询对象
     */
    public static <T> Search toSearch(T t, int page, int pageSize, String sortKey, String sortType, Boolean reasonable, Boolean rowBoundsWithCount, Boolean countFlag) {
        Search dto = new Search();
        dto.page = new Pagination();
        dto.page.setCurrentPage(page);
        dto.page.setPageSize(pageSize);
        dto.page.setReasonable(reasonable);
        dto.page.setRowBoundsWithCount(rowBoundsWithCount);
        dto.page.setCountFlag(countFlag);

        dto.setSortKey(sortKey);
        dto.setSortType(sortType);
        if (t != null) {
            try {
                Map<String, Object> search = BeanToMapHelper.convertBean(t);
                dto.setSearch(search);
            } catch (Exception e) {
                logger.error("transeform search error", e);
            }
        }
        return dto;
    }

    public String getSortKey() {
        return sortKey;
    }

    public Search setSortKey(String sortKey) {
        this.sortKey = sortKey;
        return this;
    }

    public String getSortType() {
        return sortType;
    }

    public Search setSortType(String sortType) {
        this.sortType = sortType;
        return this;
    }

    public Map<String, Object> getSearch() {
        return search;
    }

    public Search setSearch(Map<String, Object> search) {
        if (search == null) {
            this.search = new HashMap<String, Object>();
        }
        this.search.clear();
        this.search.putAll(search);
        return this;
    }

    public Search addSearch(String key, Object value) {
        if (search == null) {
            this.search = new HashMap<String, Object>();
        }
        this.search.put(key, value);
        return this;
    }

    public Search addSearchs(Map<String, Object> search) {
        if (search == null) {
            this.search = new HashMap<String, Object>();
        }
        this.search.putAll(search);
        return this;
    }

    public Page getPage() {
        return page;
    }

    public Search setPage(Page page) {
        this.page = page;
        return this;
    }

    public <T> PageData<T> toData(List<T> data) {
        return new PageData<T>(this.getPage().getPageSize(), this.getPage().getCurrentPage(), this.getPage().getTotalPages(), this.getPage().getTotalRows(), data);
    }

    public <T1, T2> PageData<T2> toData(List<T1> data, Class<T2> clazz) {
        PageData<T2> page = new PageData<T2>(this.getPage().getPageSize(), this.getPage().getCurrentPage(), this.getPage().getTotalPages(), this.getPage().getTotalRows());
        page.setData(BeanConvertHelper.copyListProperties(data, clazz));
        return page;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
