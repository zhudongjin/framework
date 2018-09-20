package com.hstypay.framework.core.constant;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 14:05
 */
public interface CommonConstant {

    /***************************************** 配置文件key *********************************************/
    /** JVM参数 */
    String CONF_NETWORKADDRESS_CACHE_TTL = "networkaddress.cache.ttl";
    /** JVM参数 */
    String CONF_NETWORKADDRESS_CACHE_NEGATIVE_TTL = "networkaddress.cache.negative.ttl";
    /** JVM参数 */
    String CONF_SUN_NET_CLIENT_DEFAULTCONNECTTIMEOUT = "sun.net.client.defaultConnectTimeout";
    /** JVM参数 */
    String CONF_SUN_NET_CLIENT_DEFAULTREADTIMEOUT = "sun.net.client.defaultReadTimeout";
    /** JVM参数:http代理 */
    String CONF_JVM_HTTP_PROXYHOST = "jvm.http.proxyHost";
    String CONF_JVM_HTTP_PROXYPORT = "jvm.http.proxyPort";
    String CONF_JVM_HTTP_NONPROXYHOSTS = "jvm.http.nonProxyHosts";
    /** JVM参数:https代理 */
    String CONF_JVM_HTTPS_PROXYHOST = "jvm.https.proxyHost";
    String CONF_JVM_HTTPS_PROXYPORT = "jvm.https.proxyPort";
    /** JVM参数:ftp代理 */
    String CONF_JVM_FTP_PROXYHOST = "jvm.ftp.proxyHost";
    /** JVM参数 */
    String CONF_JVM_FTP_PROXYPORT = "jvm.ftp.proxyPort";
    /** JVM参数 */
    String CONF_JVM_FTP_NONPROXYHOSTS = "jvm.ftp.nonProxyHosts";

    /** 配置文件刷新时间 */
    String CONF_SYSTEM_RELOAD_TIMESPAN = "system.reload.timespan";
}
