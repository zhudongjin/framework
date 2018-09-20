package com.hstypay.sandbox.support.encrypt;

import com.hstypay.sandbox.support.encrypt.base64.Base64;
import com.hstypay.sandbox.support.encrypt.base64.UrlBase64;

/**
 * BASE64编码解码工具包
 *
 * @author 彭国卿
 * @version 1.0 2016年9月15日 下午3:15:07
 */
public class Base64Encrytion {

    private static final String CHARSET = "UTF-8";

    public static void main(String[] args) throws Exception {
        String base64 = "2222222222222222222222222222222222222222222";
//		String base64 = "1111111111111111111111111111111111111111111";
        byte[] c = decode(base64 + "=");
        System.out.println(c.length);
    }

    /**
     * BASE64字符串解码为二进制数据
     *
     * @param base64 String 需要解码字符串
     * @return 解码后二进制数据
     * @throws Exception 异常
     */
    public static byte[] decode(String base64) throws Exception {
        return Base64.decode(base64.getBytes(CHARSET));
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param bytes byte[] 编码字节数组
     * @return 编码后字符串
     * @throws Exception 异常
     */
    public static String encode(byte[] bytes) throws Exception {
        return new String(Base64.encode(bytes), CHARSET);
    }

    /**
     * 加密，使用UFT-8编码转换成字符串
     *
     * @param key byte[] 编码字节数组
     * @return 加密后字符串
     * @throws Exception 异常
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return encryptBASE64(key, CHARSET);
    }

    /**
     * 加密
     *
     * @param key      byte[] 编码字节数组
     * @param encoding String 字符串转换编码
     * @return 加密后字符串
     * @throws Exception 异常
     */
    public static String encryptBASE64(byte[] key, String encoding) throws Exception {
        byte[] b = UrlBase64.encode(key);
        return new String(b, encoding);
    }

    /**
     * 解密，使用UFT-8编码转换成字符串
     *
     * @param key String 需要解密字符串
     * @return 解密后二进制数据
     * @throws Exception 异常
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return decryptBASE64(key, CHARSET);
    }

    /**
     * 解密
     *
     * @param key      String 需要解密字符串
     * @param encoding String 字符串转换编码
     * @return 解密后二进制数据
     * @throws Exception 异常
     */
    public static byte[] decryptBASE64(String key, String encoding) throws Exception {
        return UrlBase64.decode(key.getBytes(encoding));
    }
}
