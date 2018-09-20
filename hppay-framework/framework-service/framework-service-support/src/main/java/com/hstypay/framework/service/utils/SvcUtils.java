package com.hstypay.framework.service.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

/**
 * service的工具类
 *
 * @author Tinnfy Lee
 */
public class SvcUtils {

    /**
     * 获取服务映射的URL
     *
     * @param prefix         前缀
     * @param suffix         后缀
     * @param svcName        服务名
     * @param interfaceClass 服务的接口类
     * @return 映射的URL
     */
    public static String getServiceUrl(String prefix, String suffix, String svcName, Class<?> interfaceClass) {
        StringBuilder sb = new StringBuilder();
        // 如果前缀为空则用/作为前缀
        if (StringUtils.isNotBlank(prefix)) {
            sb.append(prefix);
        } else {
            sb.append("/");
        }
        // 如果service name为空则用接口名
        if (StringUtils.isBlank(svcName)) {
            svcName = ClassUtils.getShortName(interfaceClass.getName());

            // 首字母小写
            svcName = Introspector.decapitalize(svcName);
        }

        sb.append(svcName);
        // 如果设置了后缀则添加后缀
        if (StringUtils.isNotBlank(suffix) && !svcName.endsWith(suffix)) {
            sb.append(suffix);
        }

        return sb.toString();
    }
}