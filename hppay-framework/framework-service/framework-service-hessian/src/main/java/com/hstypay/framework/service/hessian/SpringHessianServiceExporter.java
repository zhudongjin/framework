package com.hstypay.framework.service.hessian;

import com.hstypay.framework.service.hessian.support.HessianHeaderContext;
import com.hstypay.framework.service.hessian.support.RequestUtils;
import com.hstypay.sandbox.constant.Constant;
import com.hstypay.sandbox.context.ContextUtil;
import com.hstypay.sandbox.exception.BusinessException;
import com.hstypay.sandbox.exception.CommonErrorCodes;
import com.hstypay.sandbox.support.LogHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 重写hessian服务，发布日志编码信息
 *
 * @author Vincent
 * @version 1.0 2017-06-27 16:45
 */
public class SpringHessianServiceExporter extends HessianServiceExporter {

    private static final Logger LOG = LoggerFactory.getLogger(SpringHessianServiceExporter.class);

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handleHessianHeader(request);
        if (MDC.get(LogHelper.KEY_REQUEST_ID) == null) {
            String requestId = HessianHeaderContext.getContext().getHeader(LogHelper.KEY_REQUEST_ID);
            MDC.put(LogHelper.KEY_REQUEST_ID, StringUtils.isBlank(requestId) ? RandomStringUtils.randomAlphanumeric(8) : requestId);
        }

        MDC.put(LogHelper.KEY_REQUEST_IP, RequestUtils.getRequestIp());

        LOG.debug(LogHelper.getLogStr("[" + request.getRequestURL() + (StringUtils.isBlank(request.getQueryString()) ? "" : Constant.MARK_QUESTION + request.getQueryString()) + "]"));
        try {
            super.handleRequest(request, response);
        } catch (Exception e) {
            if(e instanceof HttpRequestMethodNotSupportedException){
                throw (HttpRequestMethodNotSupportedException) e;
            } else if(e instanceof NestedServletException){
                throw (NestedServletException) e;
            } else {
                throw new BusinessException(CommonErrorCodes.THIRD_SERVER_EXCEPTION.toErrorCode(), e);
            }
        } finally {
            if (ContextUtil.hasContext()) {
                ContextUtil.removeContext();
            }

            if (MDC.get(LogHelper.KEY_REQUEST_ID) != null) {
                MDC.remove(LogHelper.KEY_REQUEST_ID);
            }
        }
    }

    protected void handleHessianHeader(HttpServletRequest request) {
        HessianHeaderContext context = HessianHeaderContext.getContext();
        Enumeration enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            String value = request.getHeader(name);
            context.addHeader(name, value);
        }
    }
}
