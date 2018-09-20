package com.hstypay.framework.core.configuration;

import com.hstypay.framework.core.constant.CommonConstant;
import com.hstypay.sandbox.constant.Constant;
import com.hstypay.sandbox.support.type.HostInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 14:11
 */
public class AppConfiguration extends ReloadableConfiguration {

    private static final String DEFAULT_CONFIG_PATH = "classpath:app-config";
    private static int DEFAULT_RELOAD_TIMESPAN = 30;

    private static AppConfiguration appConfiguration = null;

    private static int RELOAD_TIMESPAN = DEFAULT_RELOAD_TIMESPAN;
    private static String CONFIG_PATH = DEFAULT_CONFIG_PATH;

    private AppConfiguration() {
        super(CONFIG_PATH, RELOAD_TIMESPAN, null);
    }

    public static AppConfiguration getInstance() {
        if (appConfiguration == null) {
            appConfiguration = new AppConfiguration();
        }
        return appConfiguration;
    }

    public static void setAppConfigPath(String appConfigPath) {
        CONFIG_PATH = appConfigPath;
        appConfiguration = null;
    }

    public static void setAppConfigPath(int reloadTimespan) {
        RELOAD_TIMESPAN = reloadTimespan;
    }

    @Override
    protected void initResource() {
        super.initResource();
        this.loadSysProperties();
        this.loadNetProperties();
    }

    protected void loadSysProperties() {
        // 设置JVM变量
        String cacheTtl = this.get(CommonConstant.CONF_NETWORKADDRESS_CACHE_TTL);
        if (cacheTtl == null || cacheTtl.trim().length() == 0) {
            cacheTtl = "120";
        }
        java.security.Security.setProperty("networkaddress.cache.ttl", cacheTtl);
        java.security.Security.setProperty("sun.net.inetaddr.ttl", cacheTtl);

        String cacheNegativeTtl = this.get(CommonConstant.CONF_NETWORKADDRESS_CACHE_NEGATIVE_TTL);
        if (cacheNegativeTtl == null || cacheNegativeTtl.trim().length() == 0) {
            cacheNegativeTtl = "120";
        }
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", cacheNegativeTtl);
        java.security.Security.setProperty("sun.net.inetaddr.negative.ttl", cacheNegativeTtl);

        String connectionTimeout = this.get(CommonConstant.CONF_SUN_NET_CLIENT_DEFAULTCONNECTTIMEOUT);
        if (connectionTimeout == null || connectionTimeout.trim().length() == 0) {
            connectionTimeout = "60000";
        }
        String readTimeout = this.get(CommonConstant.CONF_SUN_NET_CLIENT_DEFAULTREADTIMEOUT);
        if (readTimeout == null || readTimeout.trim().length() == 0) {
            readTimeout = "60000";
        }
        System.setProperty("sun.net.client.defaultConnectTimeout", connectionTimeout);
        System.setProperty("sun.net.client.defaultReadTimeout", readTimeout);
    }

    protected void loadNetProperties() {
        // http的代理设置
        String httpProxyHost = this.get(CommonConstant.CONF_JVM_HTTP_PROXYHOST);
        String httpProxyPort = this.get(CommonConstant.CONF_JVM_HTTP_PROXYPORT);
        String nonProxyHosts = this.get(CommonConstant.CONF_JVM_HTTP_NONPROXYHOSTS);

        if (StringUtils.isBlank(httpProxyHost)) {
            HostInfo hostInfo = new HostInfo(System.getenv("http_proxy"));
            httpProxyHost = hostInfo.getHost();

            if (StringUtils.isBlank(httpProxyPort) && hostInfo.getPort() > 0) {
                httpProxyPort = String.valueOf(hostInfo.getPort());
            }
        }

        if (StringUtils.isBlank(nonProxyHosts)) {
            nonProxyHosts = System.getenv("no_proxy");
            if (StringUtils.isNotBlank(nonProxyHosts))
                nonProxyHosts = StringUtils.replaceEachRepeatedly(nonProxyHosts, new String[]{" ", "\t", ","}, new String[]{Constant.EMPTY, Constant.EMPTY, Constant.MARK_VERTICAL});
        }

        LOGGER.debug("jvm:http.proxyHost={},http.proxyPort={},http.nonProxyHosts={}", httpProxyHost, httpProxyPort, nonProxyHosts);

        if (StringUtils.isNotBlank(httpProxyHost)) {
            System.getProperties().setProperty("http.proxyHost", httpProxyHost);
        }

        if (StringUtils.isNotBlank(httpProxyPort)) {
            System.getProperties().setProperty("http.proxyPort", httpProxyPort);
        }

        if (StringUtils.isNotBlank(nonProxyHosts)) {
            System.getProperties().setProperty("http.nonProxyHosts", nonProxyHosts);
        }

        // https的代理设置
        String httpsProxyHost = this.get(CommonConstant.CONF_JVM_HTTPS_PROXYHOST);
        String httpsProxyPort = this.get(CommonConstant.CONF_JVM_HTTPS_PROXYPORT);

        if (StringUtils.isBlank(httpsProxyHost)) {
            HostInfo hostInfo = new HostInfo(System.getenv("https_proxy"));
            httpsProxyHost = hostInfo.getHost();

            if (StringUtils.isBlank(httpsProxyPort) && hostInfo.getPort() > 0) {
                httpsProxyPort = String.valueOf(hostInfo.getPort());
            }
        }

        LOGGER.debug("jvm:https.proxyHost={},https.proxyPort={}", httpsProxyHost, httpsProxyPort);

        if (StringUtils.isNotBlank(httpsProxyHost)) {
            System.getProperties().setProperty("https.proxyHost", httpsProxyHost);
        }

        if (StringUtils.isNotBlank(httpsProxyPort)) {
            System.getProperties().setProperty("https.proxyPort", httpsProxyPort);
        }

        // ftp的代理设置
        String ftpProxyHost = this.get(CommonConstant.CONF_JVM_FTP_NONPROXYHOSTS);
        String ftpProxyPort = this.get(CommonConstant.CONF_JVM_FTP_PROXYPORT);
        String ftpnonProxyHosts = this.get(CommonConstant.CONF_JVM_FTP_NONPROXYHOSTS);

        if (StringUtils.isBlank(ftpProxyHost)) {
            HostInfo hostInfo = new HostInfo(System.getenv("ftp_proxy"));
            ftpProxyHost = hostInfo.getHost();

            if (StringUtils.isBlank(ftpProxyPort) && hostInfo.getPort() > 0) {
                ftpProxyPort = String.valueOf(hostInfo.getPort());
            }
        }

        if (StringUtils.isBlank(ftpnonProxyHosts)) {
            ftpnonProxyHosts = System.getenv("no_proxy");
            if (StringUtils.isNotBlank(ftpnonProxyHosts))
                ftpnonProxyHosts = StringUtils.replaceEachRepeatedly(ftpnonProxyHosts, new String[]{" ", "\t", ","}, new String[]{Constant.EMPTY, Constant.EMPTY, Constant.MARK_VERTICAL});
        }

        LOGGER.debug("jvm:ftp.proxyHost={},ftp.proxyPort={},ftp.nonProxyHosts={}", ftpProxyHost, ftpProxyPort, ftpnonProxyHosts);
        if (StringUtils.isNotBlank(ftpProxyHost)) {
            System.getProperties().setProperty("ftp.proxyHost", ftpProxyHost);
        }

        if (StringUtils.isNotBlank(ftpProxyPort)) {
            System.getProperties().setProperty("ftp.proxyPort", ftpProxyPort);
        }

        if (StringUtils.isNotBlank(ftpnonProxyHosts)) {
            System.getProperties().setProperty("ftp.nonProxyHosts", ftpnonProxyHosts);
        }
    }

}