/**
 * WebUtils.java <br/>
 * Copyright (c) 2015 by swiftpass.cn<br/>
 *
 * @author 彭国卿     <br/>
 * @version 1.0     <br/>
 * @Time 2016年4月1日 下午2:03:47
 * @Description
 */
package com.hstypay.framework.service.hessian.support;

import com.hstypay.sandbox.constant.Constant;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    /**
     * 获取httprequest对象 需要在web.xml中增加一个监听 <listener>
     * <listener-class>org.springframework
     * .web.context.request.RequestContextListener</listener-class> </listener>
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes reqAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (reqAttr != null) {
            return reqAttr.getRequest();
        }
        return null;
    }

    /**
     * getRequestLog:打印请求url日志
     *
     * @param request
     * @param params
     * @return
     */
    public static String getRequestLog(HttpServletRequest request, String... params) {
        return getRequestLog(Constant.MARK_COMMA, request, params);
    }

    /**
     * getRequestLog:打印请求url日志
     *
     * @param split
     * @param request
     * @param params
     * @return
     */
    public static String getRequestLog(String split, HttpServletRequest request, String... params) {
        String rq = "[" + request.getMethod() + "],[" + request.getRequestURL() + (StringUtils.isBlank(request.getQueryString()) ? "" : Constant.MARK_QUESTION + request.getQueryString()) + "]";
        List<String> _params = Arrays.asList(params);
        if (_params == null || _params.size() == 0) {
            return rq;
        }
        return rq.concat(split).concat(StringUtils.join(_params, split));
    }

    public static boolean isRpcCall(HttpServletRequest request) {
        if (request == null)
            return false;

        /**
         * 如果是hessian请求则认为是rpc调用
         */
        if (isHessianRequest(request))
            return true;

        /**
         * 如果是soap的web service请求则认为是rpc调用
         */
        if (isSoapRequest(request))
            return true;

        return false;
    }

    /**
     * 判断是否为soap调用
     *
     * @param request
     * @return
     */
    private static boolean isSoapRequest(HttpServletRequest request) {
        if (request == null)
            return false;

        String contentType = request.getContentType();
        if (contentType == null)
            return false;

		/*
         * sopa1.2协议中，ContentType为application/soap+xml
		 */
        if (contentType.contains("soap"))
            return true;

        /**
         * sopa协议中有一个请求头SOAPAction的项，该项可能为空串 因此只判断是否为null
         */
        if (request.getHeader("SOAPAction") != null)
            return true;

        return false;
    }

    /**
     * 判断是否为hessian调用
     *
     * @param request
     * @return
     */
    private static boolean isHessianRequest(HttpServletRequest request) {
        if (request == null)
            return false;

        String contentType = request.getContentType();
        if (contentType == null)
            return false;

		/*
		 * Hessian协议的ContentType为x-application/hessian
		 */
        if (contentType.contains("hessian"))
            return true;

        return false;
    }

    private static String getFirstValidIp(String ipList) {
        if (ipList == null || ipList.length() == 0)
            return null;

        String[] ips = ipList.split(",");
        for (String ip : ips) {
            if (StringUtils.isBlank(ip))
                continue;

            if ("unknown".equalsIgnoreCase(ip))
                continue;

            return ip;
        }

        return null;
    }

    public static String getRequestIp() {
        HttpServletRequest request = getRequest();
        if (request == null)
            return null;

        String ip = getFirstValidIp(request.getHeader("X-Forwarded-For"));

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("X-FORWARDED-FOR"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("Proxy-Client-IP"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("WL-Proxy-Client-IP"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("HTTP_CLIENT_IP"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = getFirstValidIp(request.getHeader("HTTP_X_FORWARDED_FOR"));
        } else {
            return ip;
        }

        if (ip == null) {
            ip = request.getRemoteAddr();
        } else {
            return ip;
        }

        if (ip != null)
            return ip.trim();
        else
            return null;
    }

    /**
     * 获取原始的http请求的内容，主要用于获取web接口中请求内容
     *
     * @param request
     * @return
     */
    public static String getRequestString(HttpServletRequest request) {
        if (request == null)
            return null;

        /**
         * 如果是rpc调用，则不获取请求内容，rpc调用请求的内容是特定格式
         */
        if (isRpcCall(request))
            return null;

        /**
         * 是GET方法则从query string中获取
         */
        String method = request.getMethod();
        if (method==null)
            method = Constant.EMPTY;

        if (method.equalsIgnoreCase("GET"))
            return request.getQueryString();

        /**
         * 如果是post方法则从请求的body中获取,但需要区分文件上传的 情况
         */
        if (method.equalsIgnoreCase("POST")) {
            try {
                ServletInputStream inputStream = request.getInputStream();
                int length = request.getContentLength();
                if (length <= 0)
                    return null;

                byte[] bytes = new byte[length];
                int readSize = inputStream.read(bytes);
                if (readSize > 0)
                    return new String(bytes, 0, readSize);
                else
                    return Constant.EMPTY;
            } catch (Throwable t) {
                logger.error("get post data body from request input stream fail", t);
            }
        }

        return null;
    }

    public static boolean isMultipart(HttpServletRequest request) {
        if (!isHttpPost(request)) {
            return false;
        }

        String contentType = request.getContentType();
        return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
    }

    public static boolean isHttpPost(HttpServletRequest request) {
        if (request == null)
            return false;

        String method = request.getMethod();
        if (method == null || !"post".equalsIgnoreCase(method)) {
            return false;
        }

        return true;
    }

    /**
     * 检查http请求是否是请求的传入的二进制数据，对于octet-stream，image，multipart文件 都认为是二进制的
     *
     * @param request
     * @return
     */
    public static boolean isBinayBodyData(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType == null)
            return false;
        contentType = contentType.toLowerCase();

        // 判断Content-Type是否指定为流数据
        if (contentType.contains("stream"))
            return true;

        // 判断Content-Type是否指定为文件上传
        if (contentType.contains("multipart"))
            return true;

        // 判断Content-Type是否指定为图片
        if (contentType.contains("image"))
            return true;

        return false;
    }

    @SuppressWarnings("unchecked")
    public static String getParameterMapString(HttpServletRequest request) {
        if (request == null)
            return Constant.EMPTY;

        Map<String, String[]> map = request.getParameterMap();

        if (map == null || map.size() <= 0)
            return Constant.EMPTY;

        StringBuilder sb = new StringBuilder(100);
        boolean bfirst = true;// 是否首次拼接
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            for (String item : entry.getValue()) {
                if (!bfirst) {
                    sb.append("&");
                } else {
                    bfirst = false;
                }
                sb.append(entry.getKey());
                sb.append("=");
                sb.append(item);
            }
        }

        return sb.toString();
    }

    /**
     * 获取HttpServletResponse对象
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getResponse() {
        ServletRequestAttributes reqAttr = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        if (reqAttr != null) {
            return reqAttr.getResponse();
        }
        return null;
    }

    // 输出 图像
    public static void ajaxFile(File file, String name, boolean download) {
        HttpServletResponse response = getResponse();
        ajaxFile(file, name, download, response);
    }

    public static void ajaxFile(File file, String name, boolean download, HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        if (download) {
            response.setContentType("application/octet-stream");
        } else {
            response.setContentType("image/jpeg");
        }
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Length", String.valueOf(file.length()));

        String _name;
        try {
            _name = URLEncoder.encode(name, Constant.UTF_8);
        } catch (UnsupportedEncodingException e1) {
            _name = name;
        }

        response.setHeader("Content-Disposition", "filename=".concat(_name));
        OutputStream out = null;
        FileInputStream in = null;
        try {
            out = response.getOutputStream();

            int len = 0;
            byte[] buffer = new byte[1024 * 1024];
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    // 输出 图像
    public static String ajaxImage(BufferedImage buffImg) {
        HttpServletResponse response = getResponse();
        return ajaxImage(buffImg, response);
    }

    // 输出 图像
    public static String ajaxImage(BufferedImage buffImg, HttpServletResponse response) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream sos = null;
        try {
            ImageIO.write(buffImg, "jpeg", response.getOutputStream());
        } catch (Exception e) {
            logger.error("[ajaxImage] occurs error", e);
        } finally {
            IOUtils.closeQuietly(sos);
        }
        return null;
    }
}
