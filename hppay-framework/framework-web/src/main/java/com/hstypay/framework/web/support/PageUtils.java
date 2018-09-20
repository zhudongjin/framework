/**      
 * PageUtils.java create on 2016年12月15日 下午3:58:39。
 * Copyright (c) 2015-2016 by SwiftPass
 *
 * @author vincent
 */ 
package com.hstypay.framework.web.support;

import com.hstypay.sandbox.support.page.PageForm;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 分页支持
 *
 * @author vincent
 * @version 1.0 2016年12月15日 下午3:58:39
 */
public class PageUtils {
	
	private static final String REQ_PAGE_SIZE = "pageSize";
	private static final String REQ_CURRENT_PAGE = "currentPage";
	private static final String REQ_SORT_KEY = "sortKey";
	private static final String REQ_SORT_TYPE = "sortType";
	private static final String REQ_REASONABLE = "reasonable";
	private static final String REQ_ROWBOUNDSWITHCOUNT = "rowBoundsWithCount";
	private static final String REQ_COUNTFLAG = "countFlag";

	public static <T> PageForm<T> toPage(T t) {
		HttpServletRequest request = WebUtils.getRequest();
		int pageSize = NumberUtils.toInt(request.getParameter(REQ_PAGE_SIZE), 15);
		int currentPage = NumberUtils.toInt(request.getParameter(REQ_CURRENT_PAGE), 1);
		String sortKey = request.getParameter(REQ_SORT_KEY);
		String sortType = request.getParameter(REQ_SORT_TYPE);

        Boolean reasonable = null;
		if ("true".equalsIgnoreCase(request.getParameter(REQ_REASONABLE)) || "false".equalsIgnoreCase(request.getParameter(REQ_REASONABLE))) {
            reasonable = BooleanUtils.toBoolean(request.getParameter(REQ_REASONABLE));
        }

        Boolean rowBoundsWithCount = null;
        if ("true".equalsIgnoreCase(request.getParameter(REQ_ROWBOUNDSWITHCOUNT)) || "false".equalsIgnoreCase(request.getParameter(REQ_ROWBOUNDSWITHCOUNT))) {
            rowBoundsWithCount = BooleanUtils.toBoolean(request.getParameter(REQ_ROWBOUNDSWITHCOUNT));
        }

        Boolean countFlag = null;
        if ("true".equalsIgnoreCase(request.getParameter(REQ_COUNTFLAG)) || "false".equalsIgnoreCase(request.getParameter(REQ_COUNTFLAG))) {
            countFlag = BooleanUtils.toBoolean(request.getParameter(REQ_COUNTFLAG));
        }
		return new PageForm<T>(pageSize, currentPage, sortKey, sortType, reasonable, rowBoundsWithCount, countFlag, t);
	}
}
