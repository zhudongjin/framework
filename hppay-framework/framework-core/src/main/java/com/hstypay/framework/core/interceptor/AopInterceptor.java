package com.hstypay.framework.core.interceptor;

import org.springframework.core.Ordered;

public interface AopInterceptor<T> extends Ordered {

    /**
     * 是否为全局的拦截器
     *
     * @return true-全局的拦截器，false-特定的拦截器
     */
    boolean isGlobal();

    /**
     * aop被拦截的方法调用前处理
     *
     * @param data
     */
    boolean before(T data);

    /**
     * aop被拦截的方法调用后处理
     *
     * @param data
     */
    boolean after(T data);
}
