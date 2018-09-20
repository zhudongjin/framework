package com.hstypay.sandbox.configuration;

import java.io.Serializable;

/**
 * 配置
 *
 * @author Vincent
 * @version 1.0 2017-06-27 10:34
 */
public interface Configuration extends Serializable {

    String get(String key);

    String get(String key, String defaultValue);

    boolean getBoolean(String key);

    boolean getBoolean(String key, boolean defaultValue);

    int getInt(String key);

    int getInt(String key, int defaultValue);

    long getLong(String key);

    long getLong(String key, long defaultValue);

    double getDouble(String key);

    double getDouble(String key, double defaultValue);

    float getFloat(String key);

    float getFloat(String key, float defaultValue);

    boolean hasConfig(String key);
}
