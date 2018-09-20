package com.hstypay.sandbox.support;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * ClassName:IpUtil
 * Function: IP帮助类
 * Date:     2017年7月1日 下午5:07:58
 * @author   hansong
 * @since    JDK 1.6
 */
public class IpUtil {

	public static String localIp = null;
	static{
		localIp = initIp();
	}
	
	/**
	 * initIp:初始化本地局域网IP
	 *
	 * @author hansong
	 * @return
	 */
    private static String initIp() { 
        try {
			Enumeration<?> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<?> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && (ip.getHostAddress().startsWith("192") || ip.getHostAddress().startsWith("10") || ip.getHostAddress().startsWith("172"))) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e){}
        return "127.0.0.1";
    }
    
    /**
     * getClientIp:获取客户端ip
     * @author huang
     * @param clientIp
     * @return
     */
    public static String getClientIp(String clientIp){
    	String ip = "127.0.0.1";
    	try {
			if(StringUtils.isNotBlank(clientIp)){
				ip = StringUtils.trim(StringUtils.split(clientIp, "\\,")[0]);
			}
		} catch (Exception e) {}
    	return ip;
    }
}

