package com.hstypay.sandbox.support.encrypt;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * DES加解密
 * <p>
 * <pre>
 * DES是一种对称加密算法，所谓对称加密算法即：加密和解密使用相同密钥的算法。DES加密算法出自IBM的研究，后来被美国政府正式采用，之后开始广泛流传，但是近些年使用越来越少，因为DES使用56位密钥，以现代计算能力，24小时内即可被破解。虽然如此，在某些简单应用中，我们还是可以使用DES加密算法，本文简单讲解DES的JAVA实现。
 * 注意：DES加密和解密过程中，密钥长度都必须是8的倍数
 * </pre>
 *
 * @author vincent
 * @version 1.0 2016年10月20日 上午11:15:24
 */
public class DESEncryption {

    private static final String CHARSET = "UTF-8";

    public static final String ALGORITHM = "DES";

    private DESEncryption() {

    }

    public static String encrypt(String plainText, String secretKey) throws Exception {
        byte[] dest = encryptBytes(plainText.getBytes(CHARSET), secretKey);
        return Byte2Hex.byte2Hex(dest);
    }

    /**
     * 加密
     *
     * @param datasource byte[] 原始字节
     * @param secretKey  String 加密密钥
     * @return byte[] 加密后字节
     */
    public static byte[] encryptBytes(byte[] datasource, String secretKey) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(secretKey.getBytes(CHARSET));
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param src       加密后字符串
     * @param secretKey 加密密钥
     * @return 解密后/原始字符串
     * @throws Exception 异常
     */
    public static String decrypt(String src, String secretKey) throws Exception {
        byte[] dest = decryptBytes(Byte2Hex.hex2Bytes(src), secretKey);
        return new String(dest, CHARSET);
    }

    /**
     * 解密
     *
     * @param src       加密后字节
     * @param secretKey 加密密钥
     * @return 解密后/原始字符串
     * @throws Exception 异常
     */
    public static byte[] decryptBytes(byte[] src, String secretKey) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(secretKey.getBytes(CHARSET));
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }
}
