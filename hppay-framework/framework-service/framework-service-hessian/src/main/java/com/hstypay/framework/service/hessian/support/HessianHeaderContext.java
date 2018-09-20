package com.hstypay.framework.service.hessian.support;

import java.util.HashMap;
import java.util.Map;

/**
 * hessian协议请求头信息上下文
 * <p>
 * 1.该类使用ThreadLocal将客户端请求头信息传递给服务端。
 * 2.请求头在HessianProxy发送请求之前，将该类中得请求头附加到请求中。
 * 3.服务端使用HessianServiceExporter中获取请求头，并放入HessianHeaderContext中，提供服务端使用。
 * 4.使用完记得调用#close方法，防止ThreadLocal内存泄露。
 *
 * @author Vincent
 * @version 1.0 2017-06-27 17:11
 */
public class HessianHeaderContext {

    private static final ThreadLocal<HessianHeaderContext> THREAD_LOCAL = new ThreadLocal<HessianHeaderContext>();

    private Map<String, String> headers = new HashMap<String, String>();

    private HessianHeaderContext() {

    }

    public static HessianHeaderContext getContext() {
        HessianHeaderContext context = THREAD_LOCAL.get();
        if (context == null) {
            context = new HessianHeaderContext();
            THREAD_LOCAL.set(context);
        }
        return context;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public Map<String, String> getHeaders() {
        Map<String, String> clones = new HashMap<String, String>();
        clones.putAll(this.headers);
        return clones;
    }

    public static void close() {
        HessianHeaderContext context = THREAD_LOCAL.get();
        if (context != null) {
            context.headers.clear();
            THREAD_LOCAL.set(null);
        }
    }
}
