package com.hstypay.sandbox.support.encrypt;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * SHA 安全哈希算法
 * <p>
 * 消息摘要是一种算法：无论原始数据多长，消息摘要的结果都是固定长度的；是一种不可逆的算法<br/>
 * 原始数据任意bit位的变化，都会导致消息摘要的结果有很大的不同，且根据结果推算出原始数据的概率极低。<br/>
 * 消息摘要可以看作原始数据的指纹，指纹不同则原始数据不同。
 * </p>
 *
 * @author 彭国卿
 * @version 1.0 2016年9月15日 下午4:11:50
 */
public class SHAEncryption {

    private static final String CHARSET = "UTF-8";

    public static final String ALGORITHM = "SHA1";

    public static final String SIGNATURE_ALGORITHM = "PBEWithMD5AndDES";

    public enum MACHmac {
        HmacSHA1, HmacSHA256, HmacSHA384, HmacSHA512
    }

    private SHAEncryption() {

    }

    /**
     * 使用MAC算法的消息摘要
     *
     * @param data      String 需要加密的字符串
     * @param secretKey String 密钥
     * @param hmac      String mac算法
     * @return 签名字符串
     * @throws Exception 异常
     */
    public static String encodeByMAC(String data, String secretKey, String hmac) throws Exception {
        if (secretKey == null) {
            return null;
        }

        // KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA1");
        // SecretKey key = keyGen.generateKey(); // 这个每次生成的key不一样, 此处不能使用

        PBEKeySpec keySpec = new PBEKeySpec(secretKey.toCharArray());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(SIGNATURE_ALGORITHM);
        SecretKey key = keyFactory.generateSecret(keySpec);

		/*
		 * 此类提供“消息验证码”（Message Authentication Code，MAC）算法的功能。 MAC
		 * 基于秘密密钥提供一种方式来检查在不可靠介质上进行传输或存储的信息的完整性。
		 * 通常，消息验证码在共享秘密密钥的两个参与者之间使用，以验证这两者之间传输的信息。 基于加密哈希函数的 MAC 机制也叫作
		 * HMAC。结合秘密共享密钥， HMAC 可以用于任何加密哈希函数（如 MD5 或 SHA-1）
		 */
        Mac mac = Mac.getInstance(hmac);
        mac.init(key);
        byte[] dest = mac.doFinal(data.getBytes(CHARSET));
        return Byte2Hex.byte2Hex(dest);
    }

    /**
     * 文件流（生成文件摘要）
     *
     * @param input InputStream 文件输入流
     * @return 签名字符串
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
     * 用SHA1算法生成安全签名
     * <p>
     * 安全哈希算法（Secure Hash Algorithm）主要适用于数字签名标准 （Digital Signature Standard
     * DSS）里面定义的数字签名算法（Digital Signature Algorithm
     * DSA）。对于长度小于2^64位的消息，SHA1会产生一个160位的消息摘要。当接收到消息的时候，这个消息摘要可以用来验证数据的完整性。
     * 在传输的过程中， 数据很可能会发生变化，那么这时候就会产生不同的消息摘要。
     * SHA1有如下特性：不可以从消息摘要中复原信息；两个不同的消息不会产生同样的消息摘要,(但会有1x10 ^
     * 48分之一的机率出现相同的消息摘要,一般使用时忽略)。
     * </p>
     *
     * @param plainText 原始字符串
     * @return 签名字符串
     * @throws Exception 异常
     */
    public static String encryptSHA1(String plainText) throws Exception {
        if (plainText == null) {
            return null;
        }

        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
        digest.update(plainText.getBytes(CHARSET));
        byte messageDigest[] = digest.digest();
        // Create Hex String
        StringBuilder hexString = new StringBuilder();
        // 字节数组转换为 十六进制 数
        for (byte aMessageDigest : messageDigest) {
            String shaHex = Integer.toHexString(aMessageDigest & 0xFF);
            if (shaHex.length() < 2) {
                hexString.append(0);
            }
            hexString.append(shaHex);
        }
        return hexString.toString();
    }

}
