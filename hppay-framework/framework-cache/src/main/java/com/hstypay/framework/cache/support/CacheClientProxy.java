package com.hstypay.framework.cache.support;

import com.hstypay.framework.cache.CacheConfig;
import com.hstypay.framework.cache.exception.CacheErrorCodes;
import com.hstypay.framework.cache.exception.CacheException;
import com.hstypay.framework.cache.type.ValueItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * cache的代理类，用于组装不同的cache客户端，对外提供一致的操作
 *
 * @author Tinnfy Lee
 */
public class CacheClientProxy implements CacheClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CacheClient nativeClient;
    private CacheConfig config;

    public CacheClientProxy(CacheConfig conf, CacheClient nativeClient) {
        Assert.notNull(conf, "CacheConfig is null");

        this.config = conf;
        this.nativeClient = nativeClient;
    }

    /**
     * 检查cache client是否可用
     *
     * @param opName 操作名
     * @return
     */
    public boolean checkClientEnable(String opName) {
        boolean usable = isUsable();
        if (!usable) {
            String errInfo = config.getClientName() + " cache client is disabled. can't use " + opName + " operator";
            logger.error(errInfo);
            // 如果设置不可用抛异常则直接抛出异常
            if (config.isFailThrowExeption())
                throw new CacheException(CacheErrorCodes.CACHE_DISABLE.toErrorCode());
        }

        return usable;
    }

    @Override
    public boolean set(String key, Object value, long expiry) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("set")) {
            boolean res = false;
            try {
                res = nativeClient.set(key, value, expiry);
            } catch (Throwable t) {
                catchException("set", key, "set value to cache  fail", t);
            }

            checkOptResult("set", key, value, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean add(String key, Object value, long expiry) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("add")) {
            boolean res = false;
            try {
                res = nativeClient.add(key, value, expiry);
            } catch (Throwable t) {
                catchException("add", key, "add value to cache  fail", t);
            }

            checkOptResult("add", key, value, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean replace(String key, Object value, long expiry) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("replace")) {
            boolean res = false;
            try {
                res = nativeClient.replace(key, value, expiry);
            } catch (Throwable t) {
                catchException("replace", key, "replace value to cache  fail", t);
            }

            checkOptResult("replace", key, value, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean append(String key, Object value) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("append")) {
            boolean res = false;
            try {
                res = nativeClient.append(key, value);
            } catch (Throwable t) {
                catchException("append", key, "append value to cache  fail", t);
            }

            checkOptResult("append", key, value, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean prepend(String key, Object value) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("prepend")) {
            boolean res = false;
            try {
                res = nativeClient.prepend(key, value);
            } catch (Throwable t) {
                catchException("prepend", key, "prepend value to cache  fail", t);
            }

            checkOptResult("prepend", key, value, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean cas(String key, Object value, long casUnique, long expir) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("cas")) {
            boolean res = false;
            try {
                res = nativeClient.cas(key, value, casUnique, expir);
            } catch (Throwable t) {
                catchException("cas", key, "cas value to cache  fail", t);
            }

            checkOptResult("cas", key, value, res);

            return res;
        }

        return false;
    }

    @Override
    public Object get(String key) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("get")) {
            Object value = null;
            try {
                value = nativeClient.get(key);
            } catch (Throwable t) {
                catchException("get", key, "get value from cache  fail", t);
            }

            // get返回null时，有可能是无数据，或访问cache失败,无法区分，因此对返回null认为操作
            // 结果正常
            checkOptResult("get", key, value, true);

            return value;
        }

        return null;
    }

    @Override
    public String getByString(String key) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("getByString")) {
            String value = null;
            try {
                value = nativeClient.getByString(key);
            } catch (Throwable t) {
                catchException("getByString", key, "get value from cache  fail", t);
            }

            // get返回null时，有可能是无数据，或访问cache失败,无法区分，因此对返回null认为操作
            // 结果正常
            checkOptResult("getByString", key, value, true);

            return value;
        }

        return null;
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("get<T>")) {
            T value = null;
            try {
                value = nativeClient.get(key, type);
            } catch (Throwable t) {
                catchException("get<T>", key, "get value from cache  fail", t);
            }

            // get返回null时，有可能是无数据，或访问cache失败,无法区分，因此对返回null认为操作
            // 结果正常
            checkOptResult("get<T>", key, value, true);

            return value;
        }

        return null;
    }

    @Override
    public ValueItem gets(String key) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("gets")) {
            ValueItem value = null;
            try {
                value = nativeClient.gets(key);
            } catch (Throwable t) {
                catchException("gets", key, "gets value from cache  fail", t);
            }

            // gets返回null时，有可能是无数据，或访问cache失败,无法区分，因此对返回null认为操作
            // 结果正常
            checkOptResult("gets", key, value, true);

            return value;
        }

        return null;
    }

    @Override
    public boolean initialize() {
        if (nativeClient != null && nativeClient.initialize()) {
            logger.info("CachedClient({},{},{}) is created successfully", nativeClient.getClass()
                    .getSimpleName(), config.getDriver(), config.getClientName());

            return true;
        } else {
            logger.error("CachedClient({},{}) initialize fail", config.getDriver(),
                    config.getClientName());

            return false;
        }
    }

    @Override
    public void shutdown() {
        try {
            if (nativeClient != null)
                nativeClient.shutdown();
        } catch (Throwable t) {
            logger.error("shut down client fail," + config.getClientName(), t);
        }
    }

    @Override
    public boolean isUsable() {
        if (nativeClient != null && nativeClient.isUsable())
            return true;

        return false;
    }

    @Override
    public boolean set(String key, Object value) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("set")) {
            boolean res = false;
            try {
                res = nativeClient.set(key, value);
            } catch (Throwable t) {
                catchException("set", key, "set value to cache  fail", t);
            }

            checkOptResult("set", key, value, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean add(String key, Object value) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("add")) {
            boolean res = false;
            try {
                res = nativeClient.add(key, value);
            } catch (Throwable t) {
                catchException("add", key, "add value to cache  fail", t);
            }

            checkOptResult("add", key, value, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean delete(String key) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("delete")) {
            boolean res = false;
            try {
                res = nativeClient.delete(key);
            } catch (Throwable t) {
                catchException("delete", key, "delete value from cache  fail", t);
            }

            checkOptResult("delete", key, null, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean replace(String key, Object value) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("replace")) {
            boolean res = false;
            try {
                res = nativeClient.replace(key, value);
            } catch (Throwable t) {
                catchException("replace", key, "replace value to cache  fail", t);
            }

            checkOptResult("replace", key, null, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean cas(String key, Object value, long casUnique) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("cas")) {
            boolean res = false;
            try {
                res = nativeClient.cas(key, value, casUnique);
            } catch (Throwable t) {
                catchException("cas", key, "cas value to cache  fail", t);
            }

            checkOptResult("cas", key, null, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean flushAll() {
        if (checkClientEnable("flushAll")) {
            boolean res = false;
            try {
                res = nativeClient.flushAll();
            } catch (Throwable t) {
                catchException("flushAll", null, "flushAll value for cache  fail", t);
            }

            checkOptResult("flushAll", null, null, res);

            return res;
        }

        return false;
    }

    @Override
    public boolean touch(String key, long expiry) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("touch")) {
            boolean res = false;
            try {
                res = nativeClient.touch(key, expiry);
            } catch (Throwable t) {
                catchException("touch", key, "touch value for cache  fail", t);
            }

            checkOptResult("touch", key, null, res);

            return res;
        }

        return false;
    }

    @Override
    public CacheConfig getCacheConfig() {
        if (nativeClient != null)
            return nativeClient.getCacheConfig();
        else
            return this.config;
    }

    /**
     * 访问cache时出现异常的异常处理
     *
     * @param operator
     * @param key
     * @param errinfo
     * @param t
     */
    private void catchException(String operator, String key, String errinfo, Throwable t) {
        StringBuilder sb = new StringBuilder(64);
        sb.append("[cache client:");
        sb.append(config.getClientName());
        sb.append("]");
        sb.append(operator);
        sb.append(" ");
        sb.append(key);
        sb.append(" ,error:");
        sb.append(errinfo);

        String errMsg = sb.toString();

        if (config.isPrintCatchInfo())
            logger.error(errMsg + ",excption:" + t.toString());

        if (config.isFailThrowExeption()) {
            throw new CacheException(CacheErrorCodes.CACHE_EXCEPTION.getCode(), errMsg, null);
        }
    }

    /**
     * 检查cache操作结果
     *
     * @param operator
     * @param key
     * @param value
     * @param result
     */
    private void checkOptResult(String operator, String key, Object value, boolean result) {
        if (config.isTraceOperation())
            logger.debug("{} key={},value={} with cache,result={}", operator, key, value, result);

        //操作结果失败的根据是否抛出异常的标来得处理
        if (!result && config.isFailThrowExeption()) {
            throw new CacheException(CacheErrorCodes.CACHE_EXCEPTION.getCode(), operator + " cache fail", null);
        }
    }

    @Override
    public long incr(String key) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("inc")) {
            long res = Long.MIN_VALUE;
            try {
                res = nativeClient.incr(key);
            } catch (Throwable t) {
                catchException("inc", key, "inc value to cache  fail", t);
            }

            checkOptResult("inc", key, null, res >= 0);

            return res;
        }

        return Long.MIN_VALUE;
    }

    @Override
    public long incr(String key, long incValue) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("inc")) {
            long res = Long.MIN_VALUE;
            try {
                res = nativeClient.incr(key, incValue);
            } catch (Throwable t) {
                catchException("inc", key, "inc value to cache  fail", t);
            }

            checkOptResult("inc", key, null, res >= 0);

            return res;
        }

        return Long.MIN_VALUE;
    }

    @Override
    public long decr(String key) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("decr")) {
            long res = Long.MIN_VALUE;
            try {
                res = nativeClient.decr(key);
            } catch (Throwable t) {
                catchException("decr", key, "decr value to cache  fail", t);
            }

            checkOptResult("decr", key, null, res >= 0);

            return res;
        }

        return Long.MIN_VALUE;
    }

    @Override
    public long decr(String key, long decrValue) {
        Assert.notNull(key, "cache key is null");

        if (checkClientEnable("decr")) {
            long res = Long.MIN_VALUE;
            try {
                res = nativeClient.decr(key, decrValue);
            } catch (Throwable t) {
                catchException("decr", key, "decr value to cache  fail", t);
            }

            checkOptResult("decr", key, null, res >= 0);

            return res;
        }

        return Long.MIN_VALUE;
    }
}