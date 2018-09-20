package com.hstypay.sandbox.context;

import java.io.Serializable;
import java.util.Map;

/**
 * 上下文信息存储
 *
 * @author Vincent
 * @version 1.0 2017-06-26 22:01
 */
public interface Context extends Serializable {

    Object getCurrentAttribute(String key);

    void removeCurrentAttribute(String key);

    void setCurrentAttribute(String key, Object value);

    Map<String, Object> getCurrentAttributesMap();

    Map<String, String[]> getRequestParamsMap();

    String getRequestParameter(String name);

    String[] getRequestParameterValues(String name);

    Object getSessionAttribute(String key);

    void removeSessionAttribute(String key);

    void setSessionAttribute(String key, Object value);

    Object getApplicationAttribute(String key);

    void setApplicationAttribute(String key, Object value);

    void removeApplicationAttribute(String key);

    void release();
}
