/**
 *
 */
package com.hstypay.framework.cache;

import org.springframework.cache.Cache;

/**
 * @author Exception
 */
public interface CustomSpringCache extends Cache {

    /**
     * 根据缓存时间设置缓存
     *
     * @param key    缓存Key
     * @param value  缓存Value
     * @param expire 缓存过期时间 ，秒
     */
    void put(Object key, Object value, int expire);

    /**
     * 对已存在的key设置过期时间
     *
     * @param key
     * @param value
     * @param expire
     */
    boolean touch(Object key, int expire);

    /**
     * 缓存是否可用
     *
     * @return
     */
    boolean isEnable();

    /**
     * 保存缓存key的过期时间到当前上下文中，以支持spring cache写入缓存时从上下文中获取这个过期时间
     *
     * @param key     缓存的key
     * @param expired 缓存过期时间
     */
    void putKeyExpired(String key, int expired);

    /**
     * 移除当前上下文key的过 期时间信息
     *
     * @param key 缓存的key
     */
    void removeKeyExpired(String key);

    /**
     * cache client的bean是否加入到spring的cache manager中
     *
     * @return
     */
    boolean isAttach2SpringCache();
}
