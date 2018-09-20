package com.hstypay.framework.service;

import java.util.Set;

/**
 * 服务注册
 *
 * @author Tinnfy Lee
 */
public interface Register {

    /**
     * 注册单个服务
     *
     * @param serviceInterfaceClass
     */
    void registerService(Class<?> serviceInterfaceClass);

    /**
     * 获取所有注册的服务
     *
     * @return
     */
    Set<Class<?>> getServices();
}