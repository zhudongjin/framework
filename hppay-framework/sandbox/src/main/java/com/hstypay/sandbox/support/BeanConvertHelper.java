package com.hstypay.sandbox.support;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vincent
 * @version 1.0 2017-06-28 14:23
 */
public class BeanConvertHelper {

    private static final Map<String, BeanCopier> beanCopierCache = new ConcurrentHashMap<String, BeanCopier>();

    /**
     * 拷贝属性, 默认转换处理
     *
     * @param source      源对象
     * @param targetClass Class 目标类class
     * @return 目标对象
     */
    public static <T1, T2> T2 copyProperties(T1 source, Class<T2> targetClass) {
        return copyProperties(source, targetClass, null);
    }

    /**
     * 拷贝属性
     *
     * @param source      源对象
     * @param targetClass Class 目标类class
     * @param converter   转换处理类
     * @return 目标对象
     */
    public static <T1, T2> T2 copyProperties(T1 source, Class<T2> targetClass, Converter converter) {
        if (source == null) return null;

        T2 t;
        try {
            t = targetClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        copyProperties(source, t, converter);
        return t;
    }

    /**
     * 拷贝属性, 默认转换处理
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static <T1, T2> void copyProperties(T1 source, T2 target) {
        copyProperties(source, target, null);
    }

    /**
     * 拷贝属性
     *
     * @param source    源对象
     * @param target    目标对象
     * @param converter 转换处理类
     */
    public static <T1, T2> void copyProperties(T1 source, T2 target, Converter converter) {
        if (source == null || target == null) {
            return;
        }
        BeanCopier copier = getBeanCopier(source.getClass(), target.getClass(), converter != null);
        copier.copy(source, target, converter);
    }

    /**
     * 拷贝列表数据的属性, 无转换器
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标对象class
     * @return 目标对象列表
     */
    public static <T1, T2> List<T2> copyListProperties(List<T1> sourceList, Class<T2> targetClass) {
        return copyListProperties(sourceList, targetClass, null);
    }

    /**
     * 拷贝列表数据的属性
     *
     * @param sourceList  源对象列表
     * @param targetClass 目标对象class
     * @param converter   转换对象
     * @return 目标对象列表
     */
    public static <T1, T2> List<T2> copyListProperties(List<T1> sourceList, Class<T2> targetClass, Converter converter) {
        if (sourceList == null || sourceList.size() == 0) {
            return Collections.emptyList();
        }

        List<T2> resultList = new ArrayList<T2>(sourceList.size());
        for (T1 t1 : sourceList) {
            T2 t2;
            try {
                t2 = targetClass.newInstance();
                copyProperties(t1, t2, converter);
                resultList.add(t2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return resultList;
    }

    /**
     * 获取缓存的key
     *
     * @param class1      Class 源对象所属class
     * @param class2      Class 目标对象所属class
     * @param convertFlag boolean 是否转换
     * @return 缓存key
     */
    private static <T1, T2> String generateKey(Class<T1> class1, Class<T2> class2, boolean convertFlag) {
        return class1.toString() + class2.toString() + String.valueOf(convertFlag);
    }

    /**
     * 获取BeanCopier
     *
     * @param sourceClass Class 源对象所属class
     * @param targetClass Class 目标对象所属class
     * @param convertFlag boolean 是否转换
     * @return 转换实现对象
     */
    private static <T1, T2> BeanCopier getBeanCopier(Class<T1> sourceClass, Class<T2> targetClass, boolean convertFlag) {
        String beanKey = generateKey(sourceClass, targetClass, convertFlag);
        BeanCopier copier;
        if (!beanCopierCache.containsKey(beanKey)) {
            copier = BeanCopier.create(sourceClass, targetClass, convertFlag);
            beanCopierCache.put(beanKey, copier);
        } else {
            copier = beanCopierCache.get(beanKey);
        }
        return copier;
    }
}
