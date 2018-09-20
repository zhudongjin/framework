/**
 *
 */
package com.hstypay.framework.cache.support;

import java.util.HashMap;
import java.util.Map;

/**
 * client的上下文，用于保存预设key的过期时间，在put时从上下文中获取过期时间
 *
 * @author Exception
 */
public class CacheClientContext {

    private Map<Object, Object> valueMap = new HashMap<Object, Object>();

    public Object removeAttribute(Object key) {
        return this.valueMap.remove(key);
    }

    public Object getAttribute(Object key) {
        return this.valueMap.get(key);
    }

    public Map<Object, Object> getAllAttributes() {
        return this.valueMap;
    }

    public void setAttribute(Object key, Object value) {
        this.valueMap.put(key, value);
    }
}
