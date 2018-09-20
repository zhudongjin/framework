/**
 *
 */
package com.hstypay.framework.cache;

import com.hstypay.framework.cache.exception.CacheErrorCodes;
import com.hstypay.framework.cache.exception.CacheException;
import com.hstypay.framework.cache.support.CacheClient;
import com.hstypay.framework.cache.support.CacheClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 * cache client的一个抽象类，实现一些公共操作
 *
 * @author Exception
 */
public class CustomCacheClient implements CustomSpringCache, InitializingBean {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // cache配置的前缀,以支持client实例可以加载不同的配置
    protected String cacheConfPrefix = "";
    // cache名称
    protected String cacheName;
    // cache的信息,前缀+名称,用于打印信息
    protected String cacheInfo;

    //是否加入到spring的cache manager中
    protected boolean attach2SpringCache = false;

    // cache的默认过期时间
    protected Integer expire;
    // Memcached client，访问cache时使用
    protected CacheClient cacheClient;
    // 错误信息打印控制，对cache出现问题不抛异常时只打印一次，该对象用于只打印一次的控制
    protected Map<String, Boolean> errPrintCtrl = new HashMap<String, Boolean>();

    // cache client的上下文，现用于存在Put前事先设定key的过期时间,put(不 带过 期时间的函数)时使用预先设定的过期时间
    protected static ThreadLocal<CacheClientContext> cacheExpiredThreadLocal = new ThreadLocal<CacheClientContext>() {

        @Override
        protected CacheClientContext initialValue() {
            return new CacheClientContext();
        }
    };

    @Override
    public String getName() {
        return this.cacheName;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public String getCacheConfPrefix() {
        return cacheConfPrefix;
    }

    public void setCacheConfPrefix(String cacheConfPrefix) {
        this.cacheConfPrefix = cacheConfPrefix;
    }

    @Override
    public Object getNativeCache() {
        return this.cacheClient;
    }

    /**
     * 初始化方法，根据配置初始化CachedClient
     */
    public void initialize() {
        try {
            cacheInfo = cacheName + "." + cacheConfPrefix;

            cacheClient = CacheClientFactory.createClient(cacheConfPrefix);
        } catch (Throwable t) {
            logger.error("cache client initialize failed. client switch is close", t);
            if (cacheClient != null)
                cacheClient.getCacheConfig().setOpen(false);
        }

        if (cacheClient == null || cacheClient.getCacheConfig() == null)
            throw new CacheException(CacheErrorCodes.INIT_CACHE.toErrorCode());
    }

    /**
     * 写入缓存
     */
    @Override
    public void put(Object key, Object value) {
        long cacheExpiry = -1;
        // 从上下文中获取预先设定的过期时间
        CacheClientContext clientCtx = cacheExpiredThreadLocal.get();
        Long preSetExp = null;
        if (clientCtx != null) {
            preSetExp = (Long) clientCtx.removeAttribute(key);
            if (preSetExp != null) {
                cacheExpiry = preSetExp.longValue();
            }
        }

        // 如果未设置使用默认值
        cacheExpiry = cacheExpiry < 0 ? expire : cacheExpiry;

        if (cacheExpiry < 0)
            cacheExpiry = 0;

        this.put(key, value, (int) cacheExpiry);
    }

    @Override
    public boolean isEnable() {
        if (cacheClient != null && cacheClient.isUsable())
            return true;
        else
            return false;
    }

    /**
     * 检查cache client是否可用
     *
     * @param opName 操作名
     * @return
     */
    public boolean checkClientEnable(String opName) {
        boolean enable = isEnable();
        if (!enable) {
            String errInfo = cacheInfo + " cache client is disabled. can't use " + opName
                    + " operation";
            // 如果设置不可用抛异常则直接抛出异常
            if (cacheClient.getCacheConfig().isFailThrowExeption())
                throw new CacheException(CacheErrorCodes.CACHE_DISABLE.toErrorCode());

            logger.error(errInfo);
        }

        return enable;
    }

    /**
     * 设置key的过期 时间到上下文中
     */
    @Override
    public void putKeyExpired(String key, int expired) {
        if (key == null) {
            logger.error("putKeyExpired key is null");
            return;
        }

        // 如果cache的功能关闭，则不能进行cache操作
        if (!checkClientEnable("putKeyExpired")) {
            return;
        }

        CacheClientContext clientCtx = cacheExpiredThreadLocal.get();
        if (clientCtx != null)
            clientCtx.setAttribute(key, new Long(expired));
    }

    /**
     * 从上下文中删除设置的key过期时间
     */
    @Override
    public void removeKeyExpired(String key) {
        if (key == null) {
            logger.error("removeKeyExpired key is null");
            return;
        }

        // 删除key不检查isEnable,无论上下文中是否有都可以删除

        CacheClientContext clientCtx = cacheExpiredThreadLocal.get();
        if (clientCtx != null)
            clientCtx.removeAttribute(key);
    }

    public void put(Object key, Object value, long expire) {
        try {
            Assert.notNull(key, "key is null");
            cacheClient.set(key.toString(), value, expire);
        } catch (Exception e) {
            catchException(CacheErrorCodes.PUT_FAIL, e);
        }
    }

    /**
     * expire过期时间,秒为单位,为0表示不过期,不将key或value为null的信息写入缓存
     */
    @Override
    public void put(Object key, Object value, int expire) {
        put(key, value, (long) expire);
    }

    /**
     * 从缓存获取值
     */
    @Override
    public ValueWrapper get(Object key) {
        Object value = null;
        try {
            Assert.notNull(key, "key is null");
            value = cacheClient.get(key.toString());
        } catch (Exception e) {
            catchException(CacheErrorCodes.GET_VALUE_FAIL, e);
        }

        if (value != null)
            return new SimpleValueWrapper(value);
        else
            return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        try {
            Assert.notNull(key, "key is null");
            return cacheClient.get(key.toString(), type);
        } catch (Exception e) {
            catchException(CacheErrorCodes.GET_VALUE_FAIL, e);
            return null;
        }
    }

    /**
     * 不存在则写入，存在则查询返回存在的值
     */
    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object existingValue = this.get(key);
        if (existingValue == null) {
            this.put(key, value);
            return null;
        } else {
            return new SimpleValueWrapper(existingValue);
        }
    }

    public boolean touch(Object key, int expire) {
        try {
            Assert.notNull(key, "key is null");
            return cacheClient.touch((String) key, expire);
        } catch (Exception e) {
            catchException(CacheErrorCodes.TOUCH_VALUE_FAIL, e);
            return false;
        }
    }

    /**
     * 清除缓存
     */
    @Override
    public void evict(Object key) {
        try {
            Assert.notNull(key, "key is null");
            cacheClient.delete(key.toString());
        } catch (Exception e) {
            catchException(CacheErrorCodes.DELETE_KEY_FAIL, e);
        }
    }

    /**
     * 清空缓存服务器的内容
     */
    @Override
    public void clear() {
        try {
            cacheClient.flushAll();
        } catch (Exception e) {
            catchException(CacheErrorCodes.FLUSHALL_FAIL, e);
        }
    }

    /**
     * 在bean初始化完所有属性后调用,此处在bean初始时未设置init-method="initialize" 时强制调 用一次来初始化client
     * 只有在属性初始化 完后才能获取到配置信息
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (cacheClient == null) {
            logger.error(cacheName + "." + cacheConfPrefix + " initialize after properties set");
            initialize();
        }
    }

    /**
     * 访问cache时出现异常的异常处理
     *
     * @param errcode
     * @param errinfo
     * @param t
     */
    private void catchException(String errcode, String errinfo, Throwable t) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(cacheInfo);
        sb.append(" ");
        sb.append(errinfo);
        sb.append(":");
        sb.append(errcode);

        String errMsg = sb.toString();

        if (cacheClient.getCacheConfig().isFailThrowExeption()) {
            logger.error(errMsg, t);
            throw new CacheException(errcode, errinfo, t);
        } else {
            if (logger.isDebugEnabled())
                logger.debug(errMsg, t);

            // 如果已打印则不再打印
            if (!errPrintCtrl.containsKey(errcode)) {
                logger.error(errMsg, t);
                errPrintCtrl.put(errcode, Boolean.valueOf(true));
            }
        }
    }

    private void catchException(CacheErrorCodes cacheErr, Throwable t) {
        catchException(cacheErr.getCode(), cacheErr.getMessage(), t);
    }

    /**
     * cache客户端的关闭，在释放bean时处理
     */
    @PreDestroy
    public void shutdown() {
        try {
            if (cacheClient != null)
                cacheClient.shutdown();
        } catch (Throwable e) {
            logger.error("shutdow memcached fail", e);
        }
    }

    @Override
    public boolean isAttach2SpringCache() {
        return attach2SpringCache;
    }

    public void setAttach2SpringCache(boolean attach2SpringCache) {
        this.attach2SpringCache = attach2SpringCache;
    }
}
