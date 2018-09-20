package com.hstypay.framework.cache;

import com.hstypay.framework.cache.provider.EhCachedClient;
import com.hstypay.framework.cache.provider.WhalinMemCachedClient;
import com.hstypay.framework.cache.support.CacheClient;
import com.hstypay.framework.cache.support.CacheClientProxy;

public abstract class CacheClientFactory {
    public static CacheClient createClient(String configFileName, String clientName){
        CacheConfig config = new CacheConfig(configFileName, clientName);
        return createClient(config);
    }

    public static CacheClient createClient(String clientName){
        CacheConfig config = new CacheConfig(clientName);
        return createClient(config);
    }

    public static CacheClient createClient(CacheConfig config) {
        if (config == null)
            return null;

        //如果是close的，则不初始化native client
        if (!config.isOpen()) {
            return new CacheClientProxy(config, null);
        }

        CacheClient nativeClient = null;
        //获取danga memecached 客户端
        if (CacheConfig.WHALIN_MEMCACHED_DRIVER.equalsIgnoreCase(config.getDriver())) {
            nativeClient = new WhalinMemCachedClient(config);
        } else if (CacheConfig.EHCACHE_DRIVER.equalsIgnoreCase(config.getDriver())) {
            nativeClient = new EhCachedClient(config);
        } else {
            throw new UnsupportedOperationException("not support cache client for driver "+config.getDriver());
        }

        CacheClient client = new CacheClientProxy(config, nativeClient);
        client.initialize();

        return client;
    }
}
