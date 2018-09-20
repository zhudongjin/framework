package com.hstypay.framework.cache.type;

/**
 * 用于保存缓存数据和cas标识
 *
 * @author Tinnfy Lee
 */
public class ValueItem {

    public static final long INVALID_CAS_UNIQUE = Long.MIN_VALUE;

    protected Object value;
    protected long casUnique = INVALID_CAS_UNIQUE;

    public ValueItem() {
    }

    public ValueItem(Object value) {
        this.value = value;
    }

    public ValueItem(Object value, long casUnique) {
        this.value = value;
        this.casUnique = casUnique;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public long getCasUnique() {
        return casUnique;
    }

    public void setCasUnique(long casUnique) {
        this.casUnique = casUnique;
    }
}
