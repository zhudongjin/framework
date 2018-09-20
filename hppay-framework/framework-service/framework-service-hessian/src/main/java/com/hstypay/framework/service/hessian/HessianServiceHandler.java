package com.hstypay.framework.service.hessian;

import com.hstypay.framework.core.configuration.AppConfiguration;
import com.hstypay.framework.core.configuration.ReloadableConfiguration;
import com.hstypay.framework.service.ServiceHandler;
import com.hstypay.framework.service.support.ServiceType;
import com.hstypay.framework.service.utils.SvcUtils;
import com.hstypay.sandbox.annotation.HessianService;
import com.hstypay.sandbox.support.type.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import javax.servlet.ServletConfig;

/**
 * Hessian server的接口映射处理类，用于简化spring配置HessianExplorer和SimpleUrlHandlerMapping
 * 来发布服务。
 *
 * @author Tinnfy Lee
 */
public class HessianServiceHandler extends SimpleUrlHandlerMapping implements ServletConfigAware, BeanFactoryAware, ServiceHandler {

    protected Log logger = LogFactory.getLog(getClass());

    protected BeanFactory beanFactory;
    protected ServletConfig servletConfig;

    // 是否强制发布服务
    protected int forcePublish = ServiceHandler.FORCE_CLOSE;
    // hessian服务发布是否关闭的配置项
    protected static final String HESSIANSERVICE_PUB_CONFIG = "hessianservice.enable";
    // 统一前缀
    protected String urlPrefix = "/";
    // 服务后缀
    protected String serviceSuffix = ".hs";

    protected ServiceType serviceType = ServiceType.HessianService;

    protected ReloadableConfiguration configuration;

    public HessianServiceHandler() {

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    public int getForcePublish() {
        return forcePublish;
    }

    public void setForcePublish(int forcePublish) {
        this.forcePublish = forcePublish;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public String getServiceSuffix() {
        return serviceSuffix;
    }

    public void setServiceSuffix(String serviceSuffix) {
        this.serviceSuffix = serviceSuffix;
    }

    public void setAppConfig(ReloadableConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * 判断是否可发布
     *
     * @return true可发布，false不可发布
     */
    public boolean canPublish() {
        // 设置了强制发布则返回true
        if (forcePublish == ServiceHandler.FORCE_PUBLISH)
            return true;

        // 如果是强制不发布则返回false
        if (forcePublish >= ServiceHandler.FORCE_NOT_PUBLISH)
            return false;

        // 此时forcePushlish==FORCE_CLOSE，根据配置文件中的配置判断

        // 如果没有启动配置文件默认为发布
        if (configuration == null)
            configuration = AppConfiguration.getInstance();

        String strEnablePublish = configuration.get(HESSIANSERVICE_PUB_CONFIG);
        // 如果未配置默认为发布
        if (StringUtils.isBlank(strEnablePublish))
            return true;

        // 配置为false则不发布，否则其他值为发布
        if (strEnablePublish.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    /**
     * 获取服务的注解信息
     *
     * @param beanClazz 服务bean的类
     * @return 返回服务的注解和接口类
     */
    protected Tuple.Tuple2<HessianService, Class<?>> getServiceAnnotation(Class<?> beanClazz, Class<?> interfaceClass) {
        // 查看类上是否有Service注解
        if (!beanClazz.isAnnotationPresent(Service.class))
            return null;

        // 则查看类实现的接口上是否有注解
        Class<?>[] interfaceList = beanClazz.getInterfaces();
        if (interfaceList == null)
            return null;

        for (Class<?> inClass : interfaceList) {
            if (inClass.equals(interfaceClass) && inClass.isAnnotationPresent(HessianService.class))
                return new Tuple.Tuple2<HessianService, Class<?>>(inClass.getAnnotation(HessianService.class), inClass);
        }

        return null;
    }

    @Override
    public void registerService(Object serviceBean, String beanName, Class<?> interfaceClass) {
        // 检查是否可发布
        if (!canPublish())
            return;

        Class<?> clazz = ClassUtils.getUserClass(serviceBean);
        // 获取服务的注解信息
        Tuple.Tuple2<HessianService, Class<?>> webSvcAnnInfo = getServiceAnnotation(clazz, interfaceClass);
        if (webSvcAnnInfo == null)
            return;

        HessianService ws = webSvcAnnInfo._1();
        Class<?> serviceClass = webSvcAnnInfo._2();

        if (ws != null) {
            // 生成服务映射的URL
            String url = SvcUtils.getServiceUrl(urlPrefix, serviceSuffix, ws.serviceName(), serviceClass);
            // 如果URL已被映射，则直接返回
            if (getHandlerMap().containsKey(url)) {
                if (logger.isInfoEnabled())
                    logger.info(String.format("hessian service(%s) has been published,can't publish by %s", url, beanName));

                return;
            }
            // 生成请求处理的Handler
            HessianServiceExporter serviceExporter = new SpringHessianServiceExporter();
            serviceExporter.setService(serviceBean);
            serviceExporter.setServiceInterface(serviceClass);
            serviceExporter.afterPropertiesSet();
            // 注册服务
            registerHandler(url, serviceExporter);

            if (logger.isInfoEnabled())
                logger.info(String.format("hessian service(%s) is published by [%s][%s]", url, beanName, clazz.getName()));
        }
    }
}