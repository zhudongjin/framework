package com.hstypay.framework.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hstypay.framework.web.support.WebUtils;
import com.hstypay.sandbox.context.ContextUtil;
import com.hstypay.sandbox.support.LogHelper;

public class CoreHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CoreHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (MDC.get(LogHelper.KEY_REQUEST_ID) == null) {
            MDC.put(LogHelper.KEY_REQUEST_ID, RandomStringUtils.randomAlphanumeric(8));
        }
        if (MDC.get(LogHelper.KEY_REQUEST_IP) == null) {
            MDC.put(LogHelper.KEY_REQUEST_IP, WebUtils.getRequestIp());
        }
        logger.debug(WebUtils.getRequestLog(request));

        // 过滤掉静态资源
        if (handler instanceof HandlerMethod) {
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + (request.getServerPort() != 80 ? ":" + request.getServerPort() : "") + path;
            request.setAttribute("basePath", basePath);
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 过滤掉静态资源
        if (handler instanceof HandlerMethod) {
            if (ContextUtil.hasContext()) {
                ContextUtil.removeContext();
            }
        }
        if (MDC.get(LogHelper.KEY_REQUEST_ID) != null) {
            MDC.remove(LogHelper.KEY_REQUEST_ID);
        }
        if (MDC.get(LogHelper.KEY_REQUEST_IP) != null) {
            MDC.remove(LogHelper.KEY_REQUEST_IP);
        }
        super.afterCompletion(request, response, handler, ex);
    }
}