package com.hstypay.sandbox.support.encrypt;

import java.util.Arrays;
import java.util.Random;

/**
 * AES加解密帮助类
 *
 * @author vincent
 * @version 1.0 2016年10月27日 下午10:12:50
 */
public class AESEncryptionHelper {

    private static final String CHARSET = "UTF-8";

    public static final String DEFAULT_BYTES = "1234567890";

    private AESEncryptionHelper() {

    }

    /**
     * 生成4个字节的网络字节序
     *
     * @param sourceNumber 原始序列
     * @return 网络字节码
     */
    static byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    /**
     * 还原4个字节的网络字节序
     *
     * @param orderBytes 需要还原的字节
     * @return 原始序列
     */
    static int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    /**
     * @return 随机生成的16位字符串
     */
    static String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 获取未加密的字节
     *
     * @param randomStr 随机字符串
     * @param plainText 需要加密的字符串
     * @param saltBytes 扰乱字节
     * @return 加密前字节码
     * @throws Exception 异常
     */
    static byte[] getUnencryptedData(String randomStr, String plainText, byte[] saltBytes) throws Exception {
        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = randomStr.getBytes(CHARSET);
        byte[] textBytes = plainText.getBytes(CHARSET);
        byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);

        // randomStr + networkBytesOrder + text + saltBytes
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(saltBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        return byteCollector.toBytes();
    }

    /**
     * 加密字符串
     *
     * @param plainText       需要加密的字符串
     * @param saltBytes       扰乱字节
     * @param aesKey          加密密钥
     * @param cipherAlgorithm 加密方式
     * @return 加密字符串
     * @throws Exception 异常
     */
    public static String encrypt(String plainText, byte[] saltBytes, String aesKey, String cipherAlgorithm) throws Exception {
        // 加密
        String randomStr = getRandomStr();
        byte[] data = getUnencryptedData(randomStr, plainText, saltBytes);

        byte[] dest = AESEncryption.encryptByAES(data, aesKey.getBytes(CHARSET), cipherAlgorithm);
        return Byte2Hex.byte2Hex(dest);
    }

    /**
     * 加密字符串
     *
     * @param plainText 需要加密的字符串
     * @param salt      扰乱字节
     * @param aesKey    加密密钥
     * @return 加密字符串
     * @throws Exception 异常
     */
    public static String encrypt(String plainText, String salt, String aesKey) throws Exception {
        return encrypt(plainText, salt == null ? DEFAULT_BYTES.getBytes(CHARSET) : salt.getBytes(CHARSET), aesKey, AESEncryption.CIPHER_ALGORITHM_CBC_NoPadding);
    }

    /**
     * 解密信息
     *
     * @param encryptSource   加密后的字节
     * @param aesKey          加密密钥
     * @param cipherAlgorithm 加密方式
     * @return 解密字节，包括扰乱字节
     * @throws Exception 异常
     */
    public static byte[][] decryptBytes(byte[] encryptSource, String aesKey, String cipherAlgorithm) throws Exception {
        byte[] encryptBytes = AESEncryption.decryptByAES(encryptSource, aesKey.getBytes(CHARSET), cipherAlgorithm);
        // 去除补位字符
        byte[] bytes = PKCS7Encoder.decode(encryptBytes);
        // 分离16位随机字符串,网络字节序和附加码salt
        byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

        int xmlLength = recoverNetworkBytesOrder(networkOrder);

        byte[] source = Arrays.copyOfRange(bytes, 20, 20 + xmlLength);
        byte[] saltBytes = Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length);

        return new byte[][]{source, saltBytes};
    }

    /**
     * 解密
     *
     * @param encryptText     加密后的字符串
     * @param aesKey          加密密钥
     * @param cipherAlgorithm 加密方式
     * @return 解密/原始的字符串，包括扰乱字符串
     * @throws Exception 异常
     */
    public static String[] decrypt(String encryptText, String aesKey, String cipherAlgorithm) throws Exception {
        byte[] encryptSource = Byte2Hex.hex2Bytes(encryptText);
        byte[][] sourceBytes = decryptBytes(encryptSource, aesKey, cipherAlgorithm);
        String[] source = new String[2];
        for (int i = 0; i < sourceBytes.length; i++) {
            source[i] = new String(sourceBytes[i], CHARSET);
        }
        return source;
    }

    /**
     * 解密
     *
     * @param encryptText 加密后的字符串
     * @param aesKey      加密密钥
     * @return 解密/原始的字符串，包括扰乱字符串
     * @throws Exception 异常
     */
    public static String[] decrypt(String encryptText, String aesKey) throws Exception {
        return decrypt(encryptText, aesKey, AESEncryption.CIPHER_ALGORITHM_CBC_NoPadding);
    }
}
