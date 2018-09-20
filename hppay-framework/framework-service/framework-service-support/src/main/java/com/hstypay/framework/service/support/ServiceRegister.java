package com.hstypay.framework.service.support;

import com.hstypay.framework.service.Register;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 服务注册器，用于业务service通过该类指定发布服务的接口
 *
 * @author Tinnfy Lee
 */
public class ServiceRegister implements Register {
    // 服务接口类
    protected Set<Class<?>> services;

    public ServiceRegister() {

    }

    @Override
    public Set<Class<?>> getServices() {
        return services;
    }

    public void setServices(Set<Class<?>> services) {
        this.services = services;
    }

    @Override
    public void registerService(Class<?> serviceInterfaceClass) {
        Set<Class<?>> svcSet = getInnerServices();

        svcSet.add(serviceInterfaceClass);
    }

    protected Set<Class<?>> getInnerServices() {
        if (services == null) {
            services = new LinkedHashSet<Class<?>>();
        }

        return services;
    }
}