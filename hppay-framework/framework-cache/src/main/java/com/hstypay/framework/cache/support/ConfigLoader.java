package com.hstypay.framework.cache.support;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String keyPrefix = "";

    // 配置信息
    private Properties confProperties;

    public ConfigLoader() {
    }

    public ConfigLoader(String keyPrefix) {
        setKeyPrefix(keyPrefix);
    }

    public ConfigLoader(String configFileName, String keyPrefix) {
        setKeyPrefix(keyPrefix);
    }

    public ConfigLoader(Properties confProperties, String keyPrefix) {
        setKeyPrefix(keyPrefix);
        setConfProperties(confProperties);
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public Properties getConfProperties() {
        return confProperties;
    }

    public void setConfProperties(Properties confProperties) {
        this.confProperties = confProperties;
    }

    public static ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ConfigLoader.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    public boolean loadConfig(String configFileName) {
        if (confProperties != null)
            return true;

        confProperties = new Properties();
        InputStream is = null;
        try {
            if (StringUtils.isBlank(configFileName))
                configFileName = "classpath:cache.properties";

            String confFile = configFileName;

            if (configFileName.startsWith(CLASSPATH_URL_PREFIX))
                confFile = configFileName.substring(CLASSPATH_URL_PREFIX.length());

            // 载入配置信息
            ClassLoader loader = getClassLoader();
            is = loader.getResourceAsStream(confFile);
            if (is == null){
                logger.info("load {} failed. cache feature is disable. if need cache feature, "
                                + "please check {} is exist in conf directory and readable", configFileName,
                        configFileName);
                return false;
            }

            confProperties.load(is);

            logger.debug("load default cache config from {}", configFileName);

            return true;
        } catch (Throwable e) {
            logger.info("load {} failed. cache feature is disable. if need cache feature, "
                            + "please check {} is exist in conf directory and readable", configFileName,
                    configFileName);
            logger.error("load cache config excption", e);
            return false;
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {

            }
        }
    }

    public boolean getBoolConfItem(String key, boolean defaultValue) {
        String strValue = getConf(key);
        try {
            if (StringUtils.isNotBlank(strValue))
                return Boolean.valueOf(strValue);
        } catch (Exception e) {
            logger.info("get key {} fail. use default value {}", key, defaultValue);
        }

        return defaultValue;
    }

    public int getIntConfItem(String key, int defaultValue) {
        String strValue = getConf(key);
        try {
            if (StringUtils.isNotBlank(strValue))
                return Integer.parseInt(strValue);
        } catch (Exception e) {
            logger.info("get key {} fail. use default value {}", key, defaultValue);
        }

        return defaultValue;
    }

    public long getLongConfItem(String key, long defaultValue) {
        String strValue = getConf(key);
        try {
            if (StringUtils.isNotBlank(strValue))
                return Long.parseLong(strValue);
        } catch (Exception e) {
            logger.info("get key {} fail. use default value {}", key, defaultValue);
        }

        return defaultValue;
    }

    public String getStringConfItem(String key, String defaultValue) {
        String strValue = getConf(key);
        try {
            if (StringUtils.isNotBlank(strValue))
                return strValue;
        } catch (Exception e) {
            logger.info("get key {} fail. use default value {}", key, defaultValue);
        }

        return defaultValue;
    }

    protected String getConf(String key) {
        if (this.confProperties == null)
            return null;

        String confKey = getConfKey(key);
        return this.confProperties.getProperty(confKey);
    }

    /**
     * 如果前缀为空则直接返回key,否则将拼接上前缀
     *
     * @param key
     * @return
     */
    protected String getConfKey(String key) {
        if (StringUtils.isBlank(this.getKeyPrefix()))
            return key;

        return this.getKeyPrefix() + "." + key;
    }

}
