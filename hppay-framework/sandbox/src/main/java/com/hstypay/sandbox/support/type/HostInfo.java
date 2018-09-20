package com.hstypay.sandbox.support.type;

import com.hstypay.sandbox.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * 主机信息
 * @author Tinnfy Lee
 *
 */
public class HostInfo {
    private static final Logger logger = LoggerFactory.getLogger(HostInfo.class);

    private String host;
    private String protocol;
    private int port = -1;

    public HostInfo(String host, String protocol, int port) {
        this.host = host;
        this.protocol = protocol;
        this.port = port;
    }

    /**
     * 用host字串配置信息初始化，内部再分割
     * @param hostInfo
     */
    public HostInfo(String hostInfo) {
        parseHostInfo(hostInfo);
    }

    /**
     * 解析字串中的host信息
     * @param strHostInfo
     */
    protected void parseHostInfo(String strHostInfo){
        try {
            if (StringUtils.isBlank(strHostInfo))
                return;
            //去除空格
            strHostInfo = StringUtils.replaceEachRepeatedly(strHostInfo,
                    new String[] { " ", "\t" }, new String[] { Constant.EMPTY, Constant.EMPTY });
            //如果是以://标记的则按URI解析
            if (strHostInfo.contains("://")) {
                URI uri = new URI(strHostInfo);
                this.host = uri.getHost();
                this.port = uri.getPort();
                this.protocol = uri.getScheme();
                return;
            } else {
                String[] infos = strHostInfo.split(":");
                this.host = infos[0];
                if (infos.length >= 2) {
                    this.port = Integer.parseInt(infos[1]);
                }
            }
        } catch (Throwable t) {
            logger.error("fail to parse host info:"+strHostInfo, t);
        }
    }

    public HostInfo() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
