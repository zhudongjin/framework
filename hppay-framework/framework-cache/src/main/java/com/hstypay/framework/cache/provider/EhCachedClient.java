package com.hstypay.framework.cache.provider;

import com.hstypay.framework.cache.CacheConfig;
import com.hstypay.framework.cache.support.AbstractCacheClient;
import com.hstypay.framework.cache.type.ValueItem;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import org.apache.commons.lang3.StringUtils;

/**
 * 增加ehcache的客户端
 * @author Tinnfy Lee
 *
 */
public class EhCachedClient extends AbstractCacheClient {

    private Cache client;
    private CacheManager cMgr;

    public EhCachedClient(CacheConfig config) {
        super(config);
        // TODO Auto-generated constructor stub
    }

    @SuppressWarnings("deprecation")
    @Override
    protected boolean init() {
        if (!CacheConfig.EHCACHE_DRIVER.equalsIgnoreCase(config.getDriver())) {
            logger.error("init EhCachedClient, but config driver is not EHCACHE");
            return false;
        }

        if (StringUtils.isBlank(config.getClientName())) {
            logger.error("init EhCachedClient, cache client name is blank");
            return false;
        }

		/*
		 * MemoryStoreEvictionPolicy policy =
		 * MemoryStoreEvictionPolicy.fromString(config
		 * .getMemoryStoreEvictionPolicy()); String diskStorePath =
		 * config.getDiskStorePath(); if (StringUtils.isBlank(diskStorePath))
		 * diskStorePath = null;
		 *
		 * client = new Cache(config.getClientName(),
		 * config.getMaxElementsInMemory(), policy, config.isOverflowToDisk(),
		 * diskStorePath, config.isEternal(), config.getTimeToLiveSeconds(),
		 * config.getTimeToIdleSeconds(), config.isDiskPersistent(),
		 * config.getDiskExpiryThreadIntervalSeconds(), null);
		 */

        CacheConfiguration ehCfg = new CacheConfiguration();
        ehCfg.setName(config.getClientName());
        ehCfg.setMaxElementsInMemory(config.getMaxElementsInMemory());
        ehCfg.setMemoryStoreEvictionPolicy(config.getMemoryStoreEvictionPolicy());
        ehCfg.setOverflowToDisk(config.isOverflowToDisk());
        ehCfg.setEternal(config.isEternal());
        ehCfg.setTimeToLiveSeconds(config.getTimeToLiveSeconds());
        ehCfg.setTimeToIdleSeconds(config.getTimeToIdleSeconds());
        ehCfg.setDiskPersistent(config.isDiskPersistent());
        ehCfg.setDiskExpiryThreadIntervalSeconds(config.getDiskExpiryThreadIntervalSeconds());

        Configuration cfg = new Configuration();
        cfg.addCache(ehCfg);
        if (StringUtils.isNotBlank(config.getDiskStorePath())) {
            DiskStoreConfiguration diskCfg = new DiskStoreConfiguration();
            diskCfg.setPath(config.getDiskStorePath());

            cfg.diskStore(diskCfg);
        }
        //CacheManager使用单例模式
        cMgr = CacheManager.create(cfg);
        if (cMgr == null)
            return false;

        client = cMgr.getCache(config.getClientName());
        if (client == null)
            return false;

        return true;
    }

    @Override
    public void shutdown() {
        if (client != null && client.getStatus().equals(Status.STATUS_ALIVE)) {
            // client.dispose();
            //关闭时直接关闭整个cache manager
            if (cMgr != null)
                cMgr.shutdown();
        }
    }

    @Override
    public boolean isUsable() {
        if (config != null && config.isOpen() && client != null
                && client.getStatus().equals(Status.STATUS_ALIVE))
            return true;
        else
            return false;
    }

    @Override
    public boolean set(String key, Object value, long expiry) {
        if (!isUsable())
            return false;

        Element element = new Element(key, value);
        element.setTimeToLive((int) expiry);
        client.put(element);

        return true;
    }

    @Override
    public boolean add(String key, Object value, long expiry) {
        if (!isUsable())
            return false;

        Element element = new Element(key, value);
        element.setTimeToLive((int) expiry);
        client.put(element);

        return true;
    }

    @Override
    public boolean delete(String key) {
        if (!isUsable())
            return false;

        return client.remove(key);
    }

    @Override
    public boolean replace(String key, Object value, long expiry) {
        if (!isUsable())
            return false;

        if (client.isKeyInCache(key)) {
            Element element = new Element(key, value);
            element.setTimeToLive((int) expiry);
            client.put(element);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean append(String key, Object value) {
        throw new UnsupportedOperationException("ehcache not support append operator");
    }

    @Override
    public boolean prepend(String key, Object value) {
        throw new UnsupportedOperationException("ehcache not support prepend operator");
    }

    @Override
    public boolean cas(String key, Object value, long casUnique, long expiry) {
        throw new UnsupportedOperationException("ehcache not support cas operator");
    }

    @Override
    public Object get(String key) {
        if (!isUsable())
            return null;

        Element value = client.get(key);

        if (value != null)
            return value.getObjectValue();

        return null;
    }

    @Override
    public String getByString(String key) {
        Object value = get(key);
        if (value != null && value instanceof String)
            return (String) value;
        else
            return null;
    }

    @Override
    public ValueItem gets(String key) {
        throw new UnsupportedOperationException("ehcache not support gets operator");
    }

    @Override
    public boolean flushAll() {
        if (!isUsable())
            return false;

        client.removeAll();

        return true;
    }

    @Override
    public boolean touch(String key, long expiry) {
        throw new UnsupportedOperationException("ehcache not support touch operator");
    }

    @Override
    public long incr(String key) {
        throw new UnsupportedOperationException("ehcache not support cas operator");
    }

    @Override
    public long incr(String key, long incValue) {
        throw new UnsupportedOperationException("ehcache not support cas operator");
    }

    @Override
    public long decr(String key) {
        throw new UnsupportedOperationException("ehcache not support cas operator");
    }

    @Override
    public long decr(String key, long decrValue) {
        throw new UnsupportedOperationException("ehcache not support cas operator");
    }

}
