package com.hstypay.sandbox.support.encrypt;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * MD5 消息摘要算法工具类
 * <p>
 * 消息摘要是一种算法：无论原始数据多长，消息摘要的结果都是固定长度的；是一种不可逆的算法<br/>
 * 原始数据任意bit位的变化，都会导致消息摘要的结果有很大的不同，且根据结果推算出原始数据的概率极低。<br/>
 * 消息摘要可以看作原始数据的指纹，指纹不同则原始数据不同。
 * </p>
 * <p>
 * MD5即Message-Digest Algorithm
 * 5（信息-摘要算法5），用于确保信息传输完整一致。是计算机广泛使用的杂凑算法之一（又译摘要算法、哈希算法），主流编程语言普遍已有MD5实现。将数据（如汉字
 * ）运算为另一固定长度值，是杂凑算法的基础原理，MD5的前身有MD2、MD3和MD4。
 * </p>
 *
 * @author 彭国卿
 * @version 1.0 2016年9月15日 下午2:49:51
 */
public class MD5Encryption {

    private static final String CHARSET = "UTF-8";

    public static final String ALGORITHM = "MD5";

    public static final String SIGNATURE_ALGORITHM = "PBEWithMD5AndDES";

    private static final Logger logger = Logger.getLogger(MD5Encryption.class.getName());

    private MD5Encryption() {

    }

    /**
     * 使用MAC算法的消息摘要
     *
     * @param data      String 需要加密的字符串
     * @param secretKey String 加密密钥
     * @return 签名串
     * @throws Exception 异常
     */
    public static String encryptByMAC(String data, String secretKey) throws Exception {
        if (secretKey == null) {
            return null;
        }
        PBEKeySpec keySpec = new PBEKeySpec(secretKey.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SIGNATURE_ALGORITHM);
        SecretKey key = keyFactory.generateSecret(keySpec);

		/*
		 * 此类提供“消息验证码”（Message Authentication Code，MAC）算法的功能。 MAC
		 * 基于秘密密钥提供一种方式来检查在不可靠介质上进行传输或存储的信息的完整性。
		 * 通常，消息验证码在共享秘密密钥的两个参与者之间使用，以验证这两者之间传输的信息。 基于加密哈希函数的 MAC 机制也叫作
		 * HMAC。结合秘密共享密钥， HMAC 可以用于任何加密哈希函数（如 MD5 或 SHA-1）
		 */
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(key);
        byte[] dest = mac.doFinal(data.getBytes(CHARSET));
        return Byte2Hex.byte2Hex(dest);
    }

    /**
     * md5文件流（生成文件摘要）
     *
     * @param input InputStream 文件输入流
     * @return 签名串
     * @throws Exception 异常
     */
    public static String encryptStream(InputStream input) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(ALGORITHM);
        byte[] buffer = new byte[2048];
        int length;
        while ((length = input.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, length);
        }
        byte[] b = messageDigest.digest();
        return Byte2Hex.byte2Hex(b);
    }

    /**
     * 字符串加密为MD5 中文加密一致通用,必须转码处理： plainText.getBytes("UTF-8")
     *
     * @param plainText String 需要签名的字符串
     * @return 签名字符串
     */
    public static String encrypt(String plainText) {
        return encrypt(plainText, CHARSET);
    }

    /**
     * 字符串加密为MD5 中文加密一致通用,必须转码处理： plainText.getBytes("UTF-8")
     *
     * @param plainText String 需要签名的字符串
     * @param encoding  String 字符串编码
     * @return 签名字符串
     */
    public static String encrypt(String plainText, String encoding) {
        if (plainText == null) {
            return null;
        }

        StringBuilder rlt = new StringBuilder();
        try {
            rlt.append(encryptBytes(plainText.getBytes(encoding)));
        } catch (UnsupportedEncodingException e) {
            logger.severe(" CipherHelper md5 encrypt exception.");
            e.printStackTrace();
        }
        return rlt.toString();
    }

    /**
     * MD5 字节签名
     *
     * @param data byte[] 签名字节数组
     * @return 签名后字符串
     */
    public static String encryptBytes(byte[] data) {
        String md5Str = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
            byte[] buf = md5.digest(data);
            for (byte aBuf : buf) {
                md5Str += Byte2Hex.byte2Hex(aBuf);
            }
        } catch (Exception e) {
            md5Str = null;
            logger.severe("md5 encryptBytes error.");
            e.printStackTrace();
        }
        return md5Str;
    }

    /**
     * MD5 字节签名
     *
     * @param data byte[] 签名字节数组
     * @return 签名后字节数组
     */
    public static byte[] encryptRaw(byte[] data) {
        byte[] md5buf;
        try {
            MessageDigest md5 = MessageDigest.getInstance(ALGORITHM);
            md5buf = md5.digest(data);
        } catch (Exception e) {
            md5buf = null;
            logger.severe("md5 encryptRaw error.");
            e.printStackTrace();
        }
        return md5buf;
    }

    /**
     * MD5 参数签名生成算法
     *
     * @param params HashMap<String,String> 请求参数集，所有参数必须已转换为字符串类型
     * @param secret String 签名密钥
     * @return 签名串
     */
    public static String getSignature(HashMap<String, String> params, String secret) {
        Map<String, String> sortedParams = new TreeMap<String, String>(params);
        Set<Entry<String, String>> entrys = sortedParams.entrySet();
        StringBuilder basestring = new StringBuilder();
        for (Entry<String, String> param : entrys) {
            basestring.append(param.getKey()).append("=").append(param.getValue());
        }
        return getSignature(basestring.toString(), secret);
    }

    /**
     * MD5 参数签名生成算法
     *
     * @param sigstr String 签名字符串
     * @param secret String 签名密钥
     * @return 签名
     */
    public static String getSignature(String sigstr, String secret) {
        String basestring = sigstr + "#" + encrypt(secret);
        return encrypt(basestring);
    }
}
