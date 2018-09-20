package com.hstypay.sandbox.configuration;

import com.hstypay.sandbox.support.PropertiesHelper;
import org.apache.commons.lang3.Validate;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 10:35
 */
public class PropertiesConfiguration extends AbstractConfiguration {

    private static final long serialVersionUID = 1L;

    private Properties properties;

    public PropertiesConfiguration(String filePath) {
        // 加载配置文件
        Validate.notNull(filePath, "properties filePath can not be null");
        try {
            this.properties = PropertiesHelper.loadPropertiesFile(filePath);
        } catch (IOException e) {
            LOGGER.error("can not load properties file :" + filePath, e);
        }
    }

    public PropertiesConfiguration(Properties properties) {
        Validate.notNull(properties, "properties can not be null");
        this.properties = properties;
    }

    @Override
    public String get(String key) {
        if (this.properties == null) {
            return null;
        }
        return properties.getProperty(key);
    }

    @Override
    public boolean hasConfig(String key) {
        return this.properties != null && this.properties.containsKey(key);
    }
}
