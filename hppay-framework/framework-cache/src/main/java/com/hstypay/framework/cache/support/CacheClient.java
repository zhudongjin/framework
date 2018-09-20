package com.hstypay.framework.cache.support;

import com.hstypay.framework.cache.CacheConfig;
import com.hstypay.framework.cache.type.ValueItem;

public interface CacheClient {

    long NEVER_EXPIRE = 0L;

    /**
     * 初始化，在使用前必须调用
     *
     * @return
     */
    boolean initialize();

    /**
     * 关闭客户端，在退出时必须调用
     */
    void shutdown();

    /**
     * 客户端是否可用
     *
     * @return
     */
    boolean isUsable();

    /**
     * 获取cache的配置信息
     *
     * @return
     */
    CacheConfig getCacheConfig();

    /**
     * 写入数据到cache，不存在新增，存在则更新
     *
     * @param key    数据的key
     * @param value  数据值，需要支持序列化
     * @param expiry 过期时间,单位秒
     * @return
     */
    boolean set(String key, Object value, long expiry);

    /**
     * 写入数据到cache,永不过期
     *
     * @param key   数据的key
     * @param value 数据值，需要支持序列化
     * @return
     */
    boolean set(String key, Object value);

    /**
     * 新增数据
     *
     * @param key    数据的key
     * @param value  数据值，需要支持序列化
     * @param expiry 过期时间,单位秒
     * @return
     */
    boolean add(String key, Object value, long expiry);

    /**
     * 新增数据
     *
     * @param key   数据的key
     * @param value 数据值，需要支持序列化
     * @return
     */
    boolean add(String key, Object value);

    /**
     * 删除缓存key的数据
     *
     * @param key
     * @return
     */
    boolean delete(String key);

    /**
     * 替换key对的缓存数据，不存在则会失败
     *
     * @param key    数据的key
     * @param value  数据值，需要支持序列化
     * @param expiry 过期时间,单位秒
     * @return
     */
    boolean replace(String key, Object value, long expiry);

    /**
     * 替换key对的缓存数据，不存在则会失败
     *
     * @param key   数据的key
     * @param value 数据值，需要支持序列化
     * @return
     */
    boolean replace(String key, Object value);

    /**
     * 追加数据值，一般对字串的值操作，不建议对会破坏格式的数据进行追加，例如
     * 对压缩的数据追加会导致解压失败
     *
     * @param key   数据的key
     * @param value 数据值
     * @return
     */
    boolean append(String key, Object value);

    /**
     * 数据前添加数据值，一般对字串的值操作，不建议对会破坏格式的数据进行追加，例如
     * 对压缩的数据追加会导致解压失败
     *
     * @param key   数据的key
     * @param value 数据值
     * @return
     */
    boolean prepend(String key, Object value);

    /**
     * CAS(Check and Set)原子操作，在写入前需要获得cas标记值，只有传入的cas标识
     * 与缓存服务上的标识相同时才能写入
     *
     * @param key       数据的key
     * @param value     数据值
     * @param casUnique cas的唯一标识
     * @param expiry    过期时间,单位秒
     * @return
     */
    boolean cas(String key, Object value, long casUnique, long expiry);

    /**
     * CAS(Check and Set)原子操作，在写入前需要获得cas标记值，只有传入的cas标识
     * 与缓存服务上的标识相同时才能写入
     *
     * @param key       数据的key
     * @param value     数据值
     * @param casUnique cas的唯一标识
     * @return
     */
    boolean cas(String key, Object value, long casUnique);

    /**
     * 获取缓存数据
     *
     * @param key 数据的key
     * @return
     */
    Object get(String key);

    /**
     * 获取缓存数据，认为缓存的数据是字串值，在非字符串时获得的数据为null
     *
     * @param key
     * @return
     */
    String getByString(String key);

    /**
     * 获取缓存数据，获取后转换为相应的类型，用于将一个对象保存到cache
     * 获取时还原为相应的对象
     *
     * @param key 数据的key
     * @return
     */
    <T> T get(String key, Class<T> type);

    /**
     * 获取数据以及cas标识
     *
     * @param key 数据的key
     * @return 返回数据和cas标识
     */
    ValueItem gets(String key);

    /**
     * 清空所有缓存
     *
     * @return
     */
    boolean flushAll();

    /**
     * 延长key的过期时间，该操作在部分客户端可能不支持，当不支持时返回false，用户需要用set等其
     * 它方式来代替该操作
     *
     * @param key    数据的key
     * @param expiry 过期时间,单位秒
     * @return
     */
    boolean touch(String key, long expiry);

    /**
     * key的value值加1
     *
     * @param key
     * @return
     */
    long incr(String key);

    /**
     * key的value值增加incValue
     *
     * @param key
     * @param incValue
     * @return
     */
    long incr(String key, long incValue);

    /**
     * key的value值减1
     *
     * @param key
     * @return
     */
    long decr(String key);

    /**
     * key的value值减incValue
     *
     * @param key
     * @param decrValue
     * @return
     */
    long decr(String key, long decrValue);
}
