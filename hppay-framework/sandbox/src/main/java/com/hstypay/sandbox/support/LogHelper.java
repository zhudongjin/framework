package com.hstypay.sandbox.support;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 16:44
 */
public class LogHelper {

    public static final String LOG_SPLIT = ",";
    public static final String KEY_REQUEST_ID = "requestid";
    public static final String KEY_REQUEST_IP = "requestip";

    public static String getLogStr(Object... params) {
        return getSplitLogStr(null, params);
    }

    public static String getSplitLogStr(String split, Object... params) {
        return StringUtils.join(params, split == null ? LOG_SPLIT : split);
    }
}
