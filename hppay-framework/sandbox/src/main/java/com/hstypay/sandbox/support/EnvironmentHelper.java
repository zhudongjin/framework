package com.hstypay.sandbox.support;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 系统环境工具
 *
 * @author Vincent
 * @version 1.0 2017/03/07 19:26
 */
public class EnvironmentHelper {

    private static Boolean OS_LINUX = null;

    /**
     * 判断当前系统是否为 linux
     *
     * @return true linux, false windows
     */
    public static boolean isLinux() {
        if (OS_LINUX == null) {
            String OS = System.getProperty("os.name").toLowerCase();
            OS_LINUX = !(OS != null && OS.contains("windows"));
        }
        return OS_LINUX;
    }

    /**
     * 返回当前系统变量的函数 结果放至 Properties
     *
     * @return Properties对象
     */
    public static Properties getEnv() {
        Properties prop = new Properties();
        try {
            Process p;
            if (isLinux()) {
                p = Runtime.getRuntime().exec("sh -c set");
            } else {
                // windows
                p = Runtime.getRuntime().exec("cmd /c set");
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                int i = line.indexOf("=");
                if (i > -1) {
                    String key = line.substring(0, i);
                    String value = line.substring(i + 1);
                    prop.setProperty(key, value);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}
