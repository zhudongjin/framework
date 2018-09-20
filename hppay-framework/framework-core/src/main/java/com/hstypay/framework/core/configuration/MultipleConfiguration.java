package com.hstypay.framework.core.configuration;

import com.hstypay.sandbox.configuration.AbstractConfiguration;
import com.hstypay.sandbox.configuration.PropertiesConfiguration;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 14:10
 */
public class MultipleConfiguration extends AbstractConfiguration {

    private ReloadableConfiguration reloadableConfiguration;

    private PropertiesConfiguration propertiesConfiguration;

    public MultipleConfiguration(ReloadableConfiguration reloadableConfiguration) {
        this.reloadableConfiguration = reloadableConfiguration;
        this.propertiesConfiguration = null;
    }

    public MultipleConfiguration(ReloadableConfiguration reloadableConfiguration, PropertiesConfiguration propertiesConfiguration) {
        this.reloadableConfiguration = reloadableConfiguration;
        this.propertiesConfiguration = propertiesConfiguration;
    }

    public ReloadableConfiguration getReloadableConfiguration() {
        return reloadableConfiguration;
    }

    public void setReloadableConfiguration(ReloadableConfiguration reloadableConfiguration) {
        this.reloadableConfiguration = reloadableConfiguration;
    }

    public PropertiesConfiguration getPropertiesConfiguration() {
        return propertiesConfiguration;
    }

    public void setPropertiesConfiguration(PropertiesConfiguration propertiesConfiguration) {
        this.propertiesConfiguration = propertiesConfiguration;
    }

    @Override
    public String get(String key) {
        String value = reloadableConfiguration.get(key);
        if (value == null && propertiesConfiguration != null) {
            return propertiesConfiguration.get(key);
        }
        return value;
    }

    @Override
    public boolean hasConfig(String key) {
        return this.reloadableConfiguration.hasConfig(key) || (this.propertiesConfiguration != null && this.propertiesConfiguration.hasConfig(key));
    }
}