package com.hstypay.sandbox.support;

import com.hstypay.sandbox.constant.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.UUID;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 11:13
 */
public class Libs {

    /**
     * @return 获取uuid，去掉横线
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * @return 获取uuid
     */
    public static String getOrgUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取链接或者文件的后缀
     *
     * @param url String 连接或者文件路径
     * @return 后缀
     */
    public static String getSuffix(String url) {
        return getSuffix(url, "\\" + Constant.MARK_POINT);
    }

    /**
     * 获取链接或者文件的后缀
     *
     * @param url   String 连接或者文件路径
     * @param split String 分割符号
     * @return 后缀
     */
    public static String getSuffix(String url, String split) {
        String[] str = url.split(split);
        if (str.length == 1) {
            return "";
        }
        return str[str.length - 1];
    }

    /**
     * 获取流中的数据
     *
     * @param input InputStream 流对象
     * @return 字符串
     * @throws IOException 异常
     */
    public static String getBody(InputStream input) throws IOException {
        StringBuilder out = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = input.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    /**
     * 根据请求url获取域名
     *
     * @param url String 请求url
     * @return 域名
     */
    public static String getHost(String url) {
        String noProtocol = url.indexOf("//") > 0 ? url.substring(url.indexOf("//") + 2) : url;
        String domainWithPort = noProtocol;
        if (noProtocol.indexOf("/") > 0) {
            domainWithPort = noProtocol.substring(0, noProtocol.indexOf("/"));
        }
        return domainWithPort.indexOf(":") > 0 ? domainWithPort.substring(0, domainWithPort.indexOf(":")) : domainWithPort;
    }

    /**
     * 生产长度为length的随机字母数字混合字符串
     *
     * @param length 指定字符串长度
     * @return 随机数字符串
     */
    public static String getCharacterAndNumber(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // 输出字母还是数字
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 字符串
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            }
            // 数字
            else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }
}