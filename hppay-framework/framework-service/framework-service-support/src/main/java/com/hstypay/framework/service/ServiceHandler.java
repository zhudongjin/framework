package com.hstypay.framework.service;

import com.hstypay.framework.service.support.ServiceType;

/**
 * 服务处理接口
 *
 * @author Tinnfy Lee
 */
public interface ServiceHandler {

    //强制发布功能关闭
    int FORCE_CLOSE = 0;
    //强制发布服务
    int FORCE_PUBLISH = 1;
    //不强制发布服务
    int FORCE_NOT_PUBLISH = 2;

    /**
     * 根据bean和接口类型注册服务
     *
     * @param serviceBean
     * @param beanName
     * @param interfaceClass
     */
    void registerService(Object serviceBean, String beanName, Class<?> interfaceClass);

    /**
     * 获取服务处理器的类型
     *
     * @return
     */
    ServiceType getServiceType();
}
