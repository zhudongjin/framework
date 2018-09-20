package com.hstypay.framework.core.configuration;

import com.hstypay.framework.core.constant.CommonConstant;
import com.hstypay.sandbox.configuration.AbstractConfiguration;
import com.hstypay.sandbox.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 14:02
 */
public class ReloadableConfiguration extends AbstractConfiguration {

    /** 重新加载的时间间隔，秒为单位 */
    private int reloadTimespan = 30;

    /** 配置文件名，不带后缀 */
    protected String resourceBaseName;

    protected ReloadableResourceBundleMessageSource resource;

    public ReloadableConfiguration(String resourceBaseName) {
        this(resourceBaseName, new ReloadableResourceBundleMessageSource());
    }

    public ReloadableConfiguration(String resourceBaseName, ReloadableResourceBundleMessageSource resource) {
        this(resourceBaseName, -1, resource);
    }

    public ReloadableConfiguration(String resourceBaseName, int reloadTimespan, ReloadableResourceBundleMessageSource resource) {
        this.resourceBaseName = resourceBaseName;
        this.resource = resource == null ? new ReloadableResourceBundleMessageSource() : resource;
        if (reloadTimespan >= 0) {
            this.reloadTimespan = reloadTimespan;
        }
        this.initResource();
    }

    protected void initResource() {
        try {
            this.resource.setBasename(this.resourceBaseName);
            this.resource.setDefaultEncoding(Constant.UTF_8);
            int time = this.getReloadTimespan();
            this.resource.setCacheSeconds(time);
            this.resource.clearCache();
        } catch (Exception e) {
            LOGGER.warn("初始化加载classpath:" + resource + ".propertie出错", e);
        }
    }

    protected int getReloadTimespan() {
        String strTimeSpan = get(CommonConstant.CONF_SYSTEM_RELOAD_TIMESPAN);
        if (!StringUtils.isBlank(strTimeSpan)) {
            return NumberUtils.toInt(strTimeSpan, reloadTimespan);
        }

        return reloadTimespan;
    }

    public void setResourceBaseName(String resourceBaseName) {
        this.resourceBaseName = resourceBaseName;
    }

    public void setResource(ReloadableResourceBundleMessageSource resource) {
        this.resource = resource;
    }

    public String getResourceBaseName() {
        return resourceBaseName;
    }

    public ReloadableResourceBundleMessageSource getResource() {
        return resource;
    }

    @Override
    public String get(String key) {
        if (this.resource == null) {
            return null;
        }

        try {
            return this.resource.getMessage(key, null, Locale.getDefault());
        } catch (NoSuchMessageException ex) {
            LOGGER.debug("not find key in config resource: " + key);
        } catch (Exception ex) {
            LOGGER.error("get config " + key + " fail", ex);
        }
        return null;
    }

    @Override
    public boolean hasConfig(String key) {
        return this.resource != null && this.get(key) != null;
    }
}
