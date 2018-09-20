package com.hstypay.framework.web.support;

import com.hstypay.sandbox.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * cookie操作相关
 *
 * @author vincent
 * @version 1.0 2016年9月21日 下午8:22:04
 */
public class CookieUtil {

    /**
     * 浏览器关闭时自动删除
     */
    public final static int CLEAR_BROWSER_IS_CLOSED = -1;
    /**
     * 立即删除
     */
    public final static int CLEAR_IMMEDIATELY_REMOVE = 0;

    /**
     * 根据 cookieName 清空 Cookie [默认域下]
     *
     * @param response   请求
     * @param cookieName 名称
     */
    public static void clearCookieByName(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
        response.addCookie(cookie);
    }

    /**
     * 清除指定doamin的所有Cookie
     *
     * @param request  请求
     * @param response 响应
     * @param domain   域
     * @param path     cookie path
     */
    public static void clearAllCookie(HttpServletRequest request, HttpServletResponse response, String domain, String path) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cooky : cookies) {
            clearCookie(response, cooky.getName(), domain, path);
        }
    }

    /**
     * <p>
     * 根据cookieName清除指定Cookie
     * </p>
     *
     * @param request    请求
     * @param response   响应
     * @param cookieName cookie name
     * @param domain     Cookie所在的域
     * @param path       Cookie 路径
     * @return boolean
     */
    public static boolean clearCookieByName(HttpServletRequest request, HttpServletResponse response, String cookieName, String domain, String path) {
        boolean result = false;
        Cookie ck = findCookieByName(request, cookieName);
        if (ck != null) {
            result = clearCookie(response, cookieName, domain, path);
        }
        return result;
    }

    /**
     * 清除指定Cookie 等同于 clearCookieByName(...)
     * <p>
     * <p>
     * 该方法不判断Cookie是否存在,因此不对外暴露防止Cookie不存在异常.
     * </p>
     *
     * @param response   响应
     * @param cookieName 名称
     * @param domain     域
     * @param path       Cookie 路径
     * @return true/false
     */
    private static boolean clearCookie(HttpServletResponse response, String cookieName, String domain, String path) {
        boolean result = false;
        try {
            Cookie cookie = new Cookie(cookieName, "");
            cookie.setMaxAge(CLEAR_IMMEDIATELY_REMOVE);
            cookie.setDomain(domain);
            cookie.setPath(path);
            response.addCookie(cookie);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Cookie findCookieByName(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return null;
        for (Cookie cooky : cookies) {
            if (cooky.getName().equals(cookieName)) {
                return cooky;
            }
        }
        return null;
    }

    /**
     * 获取cookies的对应值
     *
     * @param request 请求
     * @param name    名称
     * @return 值
     */
    public static String getCookiesValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        String value = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    value = cookie.getValue();
                }
            }
        }

        if (StringUtils.isNotBlank(value)) {
            try {
                return URLDecoder.decode(value, Constant.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return value;
            }
        }
        return null;
    }

    /**
     * 设置cookie
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     */
    public static void setCookiesValue(HttpServletResponse response, String name, String value) {
        setCookiesValue(response, name, value, null, null);
    }

    /**
     * 设置cookie
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     * @param expiry   过期时间
     * @param path     cookie path
     */
    public static void setCookiesValue(HttpServletResponse response, String name, String value, Integer expiry, String path) {
        setCookiesValue(response, name, value, expiry, path, null);
    }

    /**
     * 设置cookie
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     * @param expiry   过期时间
     * @param path     cookie path
     * @param domain   域
     */
    public static void setCookiesValue(HttpServletResponse response, String name, String value, Integer expiry, String path, String domain) {
        setCookiesValue(response, name, value, expiry, path, domain, true, false);
    }

    /**
     * 设置cookie
     *
     * @param response 响应
     * @param name     名称
     * @param value    值
     * @param expiry   过期时间
     * @param path     cookie path
     * @param domain   域
     * @param httpOnly true/false
     * @param secure   true/false
     */
    public static void setCookiesValue(HttpServletResponse response, String name, String value, Integer expiry, String path, String domain, boolean httpOnly, boolean secure) {
        if (StringUtils.isBlank(name)) {
            return;
        }

        String code = null;
        if (StringUtils.isNotBlank(value)) {
            try {
                code = URLEncoder.encode(value, Constant.UTF_8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                code = value;
            }
        }
        Cookie cookie = new Cookie(name, code);
        if (null != expiry) {
            cookie.setMaxAge(expiry);
        }
        if (StringUtils.isNotBlank(path)) {
            cookie.setPath(path);
        }
        if (StringUtils.isNotBlank(domain)) {
            cookie.setDomain(domain);
        }

        if (httpOnly) {
            cookie.setHttpOnly(true);
        }
        if (secure) {
            cookie.setSecure(true);
        }
        response.addCookie(cookie);
    }
}
