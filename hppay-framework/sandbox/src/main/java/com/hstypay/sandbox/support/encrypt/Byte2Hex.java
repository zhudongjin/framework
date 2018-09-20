package com.hstypay.sandbox.support.encrypt;

import java.util.Formatter;

/**
 * 字节与16进制字符串互相转换
 *
 * @author 彭国卿
 * @version 1.0 2016年9月15日 下午2:38:08
 */
public class Byte2Hex {

    private Byte2Hex() {

    }

    /**
     * 字节转换16进制字符串
     *
     * @param b byte 需要转换的字节
     * @return 16进制字符串
     */
    public static String byte2Hex(byte b) {
        String hex = Integer.toHexString(b);
        if (hex.length() > 2) {
            hex = hex.substring(hex.length() - 2);
        }
        while (hex.length() < 2) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 字节数组转换16进制字符串
     *
     * @param bytes 需要转换的字节数组
     * @return 16进制字符串
     */
    public static String byte2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        String hash = formatter.toString();
        formatter.close();
        return hash;
    }

    /**
     * 16进制字符串转换成二进制数组
     *
     * @param hexString 需要转换的16进制字符串
     * @return 转换后的二进制数控
     */
    public static byte[] hex2Bytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
}
