package com.hstypay.sandbox.support.encrypt;

import java.util.ArrayList;

/**
 * 字节组处理
 *
 * @author 彭国卿
 * @version 1.0 2016年9月17日 上午1:42:30
 */
public class ByteGroup {

    private ArrayList<Byte> byteContainer = new ArrayList<Byte>();

    public byte[] toBytes() {
        byte[] bytes = new byte[byteContainer.size()];
        for (int i = 0; i < byteContainer.size(); i++) {
            bytes[i] = byteContainer.get(i);
        }
        return bytes;
    }

    public ByteGroup addBytes(byte[] bytes) {
        for (byte b : bytes) {
            byteContainer.add(b);
        }
        return this;
    }

    public int size() {
        return byteContainer.size();
    }
}
