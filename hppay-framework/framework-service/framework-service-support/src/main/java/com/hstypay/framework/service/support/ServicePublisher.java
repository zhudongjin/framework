package com.hstypay.framework.service.support;

import com.hstypay.framework.service.Publisher;
import com.hstypay.framework.service.Register;
import com.hstypay.framework.service.ServiceHandler;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.*;

/**
 * 通过注册ContextRefreshedEvent监听，在Context完成所有bean处理后进行服务注册
 *
 * @author Tinnfy Lee
 *
 */
public class ServicePublisher implements Publisher, ApplicationListener<ContextRefreshedEvent> {

    protected ApplicationContext applicationContext;
    // 服务的处理列表
    protected List<ServiceHandler> serviceHandlers = new LinkedList<ServiceHandler>();

    public ServicePublisher() {

    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public List<ServiceHandler> getServiceHandlers() {
        return serviceHandlers;
    }

    public void setServiceHandlers(List<ServiceHandler> serviceHandlers) {
        this.serviceHandlers = serviceHandlers;
    }

    /**
     * 发布所注册的所有服务
     */
    protected void publishServices() {
        // 获取所有服务器注册器
        Collection<Register> svcRegisterList = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), Register.class).values();

        // 对每个register服务接口列表，对接口发布服务
        for (Register register : svcRegisterList) {
            publishRegisterServices(register);
        }
    }

    /**
     * 对每个register服务接口列表，对接口发布服务
     *
     * @param register
     *            服务注册器
     */
    protected void publishRegisterServices(Register register) {
        // 获取注册的接口类
        Set<Class<?>> svcInerfaceClasses = register.getServices();
        if (svcInerfaceClasses == null || svcInerfaceClasses.size() == 0)
            return;

        for (Class<?> clazz : svcInerfaceClasses) {
            publishService(clazz);
        }
    }

    /**
     * 对接口发布服务
     *
     * @param svcInerfaceClasse
     */
    protected void publishService(Class<?> svcInerfaceClasse) {
        Map<String, ? extends Object> svcRegisterList = BeanFactoryUtils.beansOfTypeIncludingAncestors(getApplicationContext(), svcInerfaceClasse);

        if (svcRegisterList == null || svcRegisterList.size() == 0)
            return;

        for (Map.Entry<String, ? extends Object> entry : svcRegisterList.entrySet()) {
            for (ServiceHandler svcHandler : serviceHandlers) {
                svcHandler.registerService(entry.getValue(), entry.getKey(), svcInerfaceClasse);
            }
        }
    }

    /**
     * 在所有bean处理完成后注册service
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.applicationContext = event.getApplicationContext();

        // 注册所有的service
        publishServices();
    }
}