package com.hstypay.sandbox.configuration;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 10:44
 */
public abstract class AbstractConfiguration implements Configuration {

    protected Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public String get(String key, String defaultValue) {
        String value = this.get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public boolean getBoolean(String key) {
        return BooleanUtils.toBoolean(this.get(key));
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        if (!this.hasConfig(key)) {
            return defaultValue;
        }
        return this.getBoolean(key);
    }

    @Override
    public int getInt(String key) {
        return NumberUtils.toInt(key);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return NumberUtils.toInt(this.get(key), defaultValue);
    }

    @Override
    public long getLong(String key) {
        return NumberUtils.toLong(this.get(key));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return NumberUtils.toLong(this.get(key), defaultValue);
    }

    @Override
    public double getDouble(String key) {
        return NumberUtils.toDouble(this.get(key));
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return NumberUtils.toDouble(this.get(key), defaultValue);
    }

    @Override
    public float getFloat(String key) {
        return NumberUtils.toFloat(this.get(key));
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return NumberUtils.toFloat(this.get(key), defaultValue);
    }
}