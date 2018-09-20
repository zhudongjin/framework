package com.hstypay.framework.cache.provider;

import com.hstypay.framework.cache.CacheConfig;
import com.hstypay.framework.cache.support.AbstractCacheClient;
import com.hstypay.framework.cache.type.ValueItem;
import com.schooner.MemCached.MemcachedItem;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class WhalinMemCachedClient extends AbstractCacheClient {
    private SockIOPool pool;
    private MemCachedClient client;

    public WhalinMemCachedClient(CacheConfig config) {
        super(config);
    }

    @Override
    protected boolean init() {
        if (!CacheConfig.WHALIN_MEMCACHED_DRIVER.equalsIgnoreCase(config.getDriver())) {
            logger.error("init WhalinMemCachedClient, but config driver is not DANGA_MEMCACHED");
            return false;
        }

        if (StringUtils.isBlank(config.getClientName())) {
            logger.error("init WhalinMemCachedClient, cache client name is blank");
            return false;
        }

        this.pool = SockIOPool.getInstance(config.getClientName(), config.isUseTcp());
        if (pool == null) {
            logger.error("init WhalinMemCachedClient, get pool  instance fail");
            return false;
        }

        if (config.isWithNoReply()) {
            logger.error("WhalinMemCachedClient not support noreply flag, noreply will be ignore");
        }

        if (config.getServers() == null && config.getServers().length == 0) {
            logger.error("init WhalinMemCachedClient, server list is empty");
            return false;
        }

        pool.setServers(config.getServers());

        if (config.getWeigths() != null && config.getWeigths().length == config.getServers().length)
            pool.setWeights(ArrayUtils.toObject(config.getWeigths()));

        pool.setFailover(config.isUseFailover());

        if (config.getInitConnSize() > 0)
            pool.setInitConn(config.getInitConnSize());

        if (config.getMinConnSize() > 0)
            pool.setMinConn(config.getMinConnSize());

        if (config.getConnectionPoolSize() > 0)
            pool.setMaxConn(config.getConnectionPoolSize());

        if (config.getMaxIdleTime() > 0)
            pool.setMaxIdle((int) config.getMaxIdleTime());

        if (config.getMaintSleepTime() > 0)
            pool.setMaintSleep(config.getMaintSleepTime());

        pool.setNagle(config.isUseNagleAlg());

        if (config.getConnectTimeout() > 0)
            pool.setSocketConnectTO(config.getConnectTimeout());

        if (config.getOperateTimeout() > 0)
            pool.setSocketTO(config.getOperateTimeout());

        pool.setAliveCheck(config.isAliveCheck());

        pool.initialize();

        boolean binaryProtocal = "binary".equalsIgnoreCase(config.getProtocol());
        client = new MemCachedClient(config.getClientName(), config.isUseTcp(), binaryProtocal);
        client.setPrimitiveAsString(config.isDataAsString());

        // 压缩参数无效，不设置
		/*
		 * if (config.getCompressionThreshold() > 0) {
		 * client.setCompressEnable(true);
		 * client.setCompressThreshold(config.getCompressionThreshold()); }
		 */

        if (StringUtils.isNotBlank(config.getDataCharset()))
            client.setDefaultEncoding(config.getDataCharset());

        return true;
    }

    @Override
    public boolean set(String key, Object value, long expired) {
        if (client == null)
            return false;

        // 如果过期时间小于0，则置为0
        if (expired < 0)
            expired = 0;

        return client.set(key, value, new Date(expired * 1000));
    }

    @Override
    public boolean add(String key, Object value, long expired) {
        if (client == null)
            return false;

        // 如果过期时间小于0，则置为0
        if (expired < 0)
            expired = 0;

        return client.add(key, value, new Date(expired * 1000));

    }

    @Override
    public boolean replace(String key, Object value, long expired) {
        if (client == null)
            return false;

        // 如果过期时间小于0，则置为0
        if (expired < 0)
            expired = 0;

        return client.replace(key, value, new Date(expired * 1000));
    }

    @Override
    public boolean append(String key, Object value) {
        if (client == null)
            return false;

        return client.append(key, value);
    }

    @Override
    public boolean prepend(String key, Object value) {
        if (client == null)
            return false;

        return client.prepend(key, value);
    }

    @Override
    public boolean cas(String key, Object value, long casUnique, long expired) {
        if (client == null)
            return false;

        // 如果过期时间小于0，则置为0
        if (expired < 0)
            expired = 0;

        return client.cas(key, value, new Date(expired * 1000), casUnique);
    }

    public Object get(String key) {
        if (client == null)
            return null;

        return client.get(key);
    }

    public String getByString(String key) {
        if (client == null)
            return null;

        Object value = client.get(key, null, true);
        if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

    @Override
    public ValueItem gets(String key) {
        if (client == null)
            return null;

        MemcachedItem value = client.gets(key);
        if (value != null && value.getValue() != null) {
            return new ValueItem(value.getValue(), value.getCasUnique());
        }

        return null;
    }

    @Override
    public boolean delete(String key) {
        if (client == null)
            return false;

        return client.delete(key);
    }

    @Override
    public void shutdown() {
        if (pool != null && pool.isInitialized()) {
            this.pool.shutDown();
            this.pool = null;
            this.client = null;
            config.setOpen(false);
        }
    }

    @Override
    public boolean isUsable() {
        if (pool != null && client != null && config != null && config.isOpen()
                && pool.isInitialized())
            return true;

        return false;
    }

    @Override
    public boolean flushAll() {
        if (client == null)
            return false;

        return client.flushAll();
    }

    @Override
    public boolean touch(String key, long expiry) {
        logger.debug("WhalinMemCachedClient cache client not support touch operator");
        return false;
    }

    @Override
    public long incr(String key) {
        if (client == null)
            return Long.MIN_VALUE;

        return client.incr(key);
    }

    @Override
    public long incr(String key, long incValue) {
        if (client == null)
            return Long.MIN_VALUE;

        return client.incr(key, incValue);
    }

    @Override
    public long decr(String key) {
        if (client == null)
            return Long.MIN_VALUE;

        return client.decr(key);
    }

    @Override
    public long decr(String key, long decrValue) {
        if (client == null)
            return Long.MIN_VALUE;

        return client.decr(key, decrValue);
    }
}