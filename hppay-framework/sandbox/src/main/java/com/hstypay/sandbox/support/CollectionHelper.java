package com.hstypay.sandbox.support;

import com.hstypay.sandbox.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author fu.wen
 *         2016-04-10
 */
public class CollectionHelper {

    public static boolean isNullOrEmpty(Collection<?> c) {
        return null == c || c.isEmpty();

    }

    /**
     * 判断集合是否为null或空
     * @param map map对象
     * @return true/false
     */
    public static boolean isNullOrEmpty(Map<?,?> map) {
        return null == map || map.isEmpty();

    }

    /**
     * 分割字符串转换成整型集合
     *
     * @param str 需要分割的字符串
     * @return 整型集合
     */
    public static List<Integer> splitInt(String str) {
        String[] ids = StringUtils.split(str, Constant.MARK_COMMA);
        List<Integer> list = new ArrayList<Integer>(ids.length);
        for (String id : ids) {
            try {
                list.add(Integer.valueOf(id));
            } catch (Exception ignored) {

            }

        }
        return list;
    }

    /**
     * 分割字符串转换成长整型集合
     *
     * @param str 需要分割的字符串
     * @return 长整型集合
     */
    public static List<Long> splitLong(String str) {
        String[] ids = StringUtils.split(str, Constant.MARK_COMMA);
        List<Long> list = new ArrayList<Long>(ids.length);
        for (String id : ids) {
            try {
                list.add(Long.valueOf(id));
            } catch (Exception ignored) {

            }

        }
        return list;
    }

    public static <A> boolean inArray(A[] array, A a) {
        for (A anArray : array) {
            if (anArray.equals(a)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 分割列表
     *
     * @param list      分割目标
     * @param spilsSize 分割大小
     * @return 如果list为null或空，或spiltSize为小于等于0，则返回空的List<List<T>>对象
     */
    public static <T> List<List<T>> spiltList(List<T> list, int spilsSize) {
        List<List<T>> result = new ArrayList<List<T>>();

        if (isNullOrEmpty(list) || spilsSize < 1) {
            return result;
        }

        int listSize = list.size();
        int count = (int) Math.ceil(listSize / (double) spilsSize);//不能整除，就进一位

        int from;
        int to;
        for (int i = 0; i < count; i++) {
            from = i * spilsSize;
            to = (((i + 1) * spilsSize) > listSize) ? listSize : ((i + 1) * spilsSize);
            result.add(list.subList(from, to));
        }

        return result;
    }

    /**
     * String数组转int列表
     *
     * @param array 数组
     * @return 列表
     */
    public static List<Integer> toIntList(String[] array) {
        if (null == array) {
            throw new IllegalArgumentException();
        }

        List<Integer> list = new ArrayList<Integer>();
        for (String str : array) list.add(Integer.parseInt(str));

        return list;
    }
}
