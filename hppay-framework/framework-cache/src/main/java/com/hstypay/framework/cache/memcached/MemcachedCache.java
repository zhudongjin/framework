/**
 *
 */
package com.hstypay.framework.cache.memcached;

import com.hstypay.framework.cache.CustomCacheClient;

/**
 * memecached客户端实现
 *
 * @author Exception
 */
public class MemcachedCache extends CustomCacheClient {

    // 默认的memcached name
    private static final String DEFAULT_MEMCACHED_CACHENAME = "Default-Memcached";
    private static final Integer DEFAULT_MEMCACHED_EXPIRE_TIME = 3600;// 默认的过期时间，1小时

    public MemcachedCache() {
        this(DEFAULT_MEMCACHED_CACHENAME, DEFAULT_MEMCACHED_EXPIRE_TIME);
    }

    public MemcachedCache(String cacheName) {
        this(cacheName, DEFAULT_MEMCACHED_EXPIRE_TIME);
    }

    public MemcachedCache(String cacheName, Integer expire) {
        this.cacheName = cacheName;
        this.expire = expire;
    }
}
