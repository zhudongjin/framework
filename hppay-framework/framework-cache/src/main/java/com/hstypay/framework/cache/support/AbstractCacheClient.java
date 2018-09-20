package com.hstypay.framework.cache.support;

import com.hstypay.framework.cache.CacheConfig;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCacheClient implements CacheClient {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected CacheConfig config;

    public AbstractCacheClient(CacheConfig config) {
        this.config = config;
    }

    @Override
    public CacheConfig getCacheConfig() {
        return config;
    }

    public CacheConfig getConfig() {
        return config;
    }

    public void setConfig(CacheConfig config) {
        this.config = config;
    }

    protected abstract boolean init();

    @Override
    public boolean initialize() {
        boolean res = init();
        if(res) {
            logger.info("cache {}.{} initialize success, config:",
                    config.getClientName(),
                    config.getDriver(),
                    ToStringBuilder.reflectionToString(config, ToStringStyle.SHORT_PREFIX_STYLE));
        } else {
            config.setOpen(false);
            logger.info("cache {}.{} initialize failed, client switch be set close",
                    config.getClientName(),
                    config.getDriver());
        }

        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> type) {
        Object value = this.get(key);
        if (value != null && type != null && type.isInstance(value))
            return (T) value;
        else
            return null;
    }

    @Override
    public boolean set(String key, Object value) {
        return set(key, value, CacheClient.NEVER_EXPIRE);
    }

    @Override
    public boolean add(String key, Object value) {
        return add(key, value, CacheClient.NEVER_EXPIRE);
    }

    @Override
    public boolean replace(String key, Object value) {
        return replace(key, value, CacheClient.NEVER_EXPIRE);
    }

    @Override
    public boolean cas(String key, Object value, long casUnique) {
        return cas(key, value, casUnique);
    }
}