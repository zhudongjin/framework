package com.hstypay.sandbox.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 简单上下文实现
 *
 * @author vincent
 * @version 1.0 2016年3月30日 下午9:25:57
 */
public class SimpleContext extends AbstractContext {

    private static final long serialVersionUID = 1L;

    private static final Map<String, Object> applicationAttributes = new HashMap<String, Object>();
    protected final Map<String, Object> currentAttributes = new HashMap<String, Object>();
    protected final Map<String, Object> sessionAttributes = new HashMap<String, Object>();
    protected Map<String, String[]> requestParams = new HashMap<String, String[]>();

    @Override
    public Object getCurrentAttribute(String key) {
        return currentAttributes.get(key);
    }

    @Override
    public void setCurrentAttribute(String key, Object value) {
        currentAttributes.put(key, value);
    }

    @Override
    public void removeCurrentAttribute(String key) {
        currentAttributes.remove(key);
    }

    public Set<String> getCurrentrAttributeNames() {
        return currentAttributes.keySet();
    }

    public Set<String> getSessionAttributeNames() {
        return sessionAttributes.keySet();
    }

    @Override
    public void setSessionAttribute(String key, Object value) {
        sessionAttributes.put(key, value);
    }

    @Override
    public Object getSessionAttribute(String key) {
        return sessionAttributes.get(key);
    }

    @Override
    public Object getApplicationAttribute(String key) {
        return applicationAttributes.get(key);
    }

    public Set<String> getContextAttributeNames() {
        return applicationAttributes.keySet();
    }

    @Override
    public void setApplicationAttribute(String key, Object value) {
        applicationAttributes.put(key, value);
    }

    @Override
    public void removeApplicationAttribute(String key) {
        applicationAttributes.remove(key);
    }

    @Override
    public void removeSessionAttribute(String key) {
        sessionAttributes.remove(key);
    }

    @Override
    public Map<String, Object> getCurrentAttributesMap() {
        return this.currentAttributes;
    }

    @Override
    public String getRequestParameter(String name) {
        String[] arr = this.requestParams.get(name);
        if (arr != null && arr.length > 0) {
            return arr[0];
        }
        return null;
    }

    @Override
    public String[] getRequestParameterValues(String name) {
        return this.requestParams.get(name);
    }

    @Override
    public Map<String, String[]> getRequestParamsMap() {
        return this.requestParams;
    }

    public void setRequestParamsMap(Map<String, String[]> params) {
        this.requestParams = params;
    }

    @Override
    public void release() {

    }
}
