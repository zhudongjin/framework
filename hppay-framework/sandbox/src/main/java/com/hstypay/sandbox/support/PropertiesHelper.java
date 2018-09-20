package com.hstypay.sandbox.support;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Vincent
 * @version 1.0 2017-06-27 10:48
 */
public class PropertiesHelper {

    private static final Logger LOG = Logger.getLogger(PropertiesHelper.class.getName());

    public static void main(String[] args) throws IOException {
        Properties p = PropertiesHelper.loadPropertiesFile("messages/ValidationMessages_zh_CN.properties");
        System.out.println(p.get("hello"));

        File file = new File(PropertiesHelper.class.getClassLoader().getResource("messages/ValidationMessages_zh_CN.properties").getFile());
        System.out.println(file.exists());
    }

    /**
     * 处理key为大写并去掉下划线
     *
     * @param key String 需要处理的key
     * @return 去掉下划线之后的key
     */
    public static String processKey(String key) {
        return key.toUpperCase().replaceAll("_", "");
    }

    /**
     * 处理Map中的键值为大写并去掉下划线
     *
     * @param map Map<String, String> 键值对
     * @return 处理后的map
     */
    public static Map<String, String> processMapKey(Map<String, String> map) {
        Map<String, String> result = new HashMap<String, String>(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result.put(processKey(entry.getKey()), entry.getValue());
        }
        return result;
    }

    /**
     * 加载属性文件
     *
     * @param filePath String 属性文件路径
     * @return Properties对象
     * @throws IOException 异常
     */
    public static Properties loadPropertiesFile(String filePath) throws IOException {
        InputStream is = PropertiesHelper.class.getClassLoader().getResourceAsStream(filePath);
        return loadPropertiesFile(is, false);
    }

    /**
     * 加载属性文件
     *
     * @param is    InputStream 文件流
     * @param close boolean 是否需要关闭流
     * @return Properties对象
     * @throws IOException 异常
     */
    public static Properties loadPropertiesFile(InputStream is, boolean close) throws IOException {
        Properties prop;
        try {
            prop = new Properties();
            prop.load(is);
        } finally {
            if (close) {
                IOUtils.closeQuietly(is);
            }
        }

        return prop;
    }

    /**
     * 转换Properties为Map对象
     *
     * @param prop Properties 属性对象
     * @return map对象
     */
    public static Map<String, String> convertToMap(Properties prop) {
        if (prop == null) {
            return null;
        }

        Map<String, String> result = new HashMap<String, String>();
        for (Object eachKey : prop.keySet()) {
            if (eachKey == null || StringUtils.isBlank(eachKey.toString())) {
                continue;
            }

            String key = eachKey.toString();
            String value = (String) prop.get(key);
            result.put(key, value);
            LOG.info("[load property]:[key=" + key + ";value=" + value + "]");
        }
        return result;
    }

    /**
     * 加载属性文件并且转成map
     *
     * @param filePath String 文件路径
     * @return map对象
     */
    public static Map<String, String> loadValue(String filePath) {
        try {
            Properties prop = loadPropertiesFile(filePath);
            return convertToMap(prop);
        } catch (Exception e) {
            LOG.severe("can not load properties file " + filePath);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 加载属性文件流并且转成map
     *
     * @param is    InputStream 文件流
     * @param close boolean 是否需要关闭流
     * @return map对象
     */
    public static Map<String, String> loadValue(InputStream is, boolean close) {
        try {
            Properties prop = loadPropertiesFile(is, close);
            return convertToMap(prop);
        } catch (Exception e) {
            LOG.severe("can not load properties InputStream");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将map转换成javabean
     *
     * @param map   Map<String, String> 键值对
     * @param clazz Class 需要转换的类
     */
    public static <T> T convertToClass(Map<String, String> map, Class<T> clazz) {
        T instance = null;
        try {
            instance = clazz.newInstance();
            if (null == map || map.size() == 0 || null == clazz) {
                return instance;
            }
            Map<String, String> newMap = processMapKey(map);
            Field[] fields = clazz.getDeclaredFields();
            Object value;
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                value = newMap.get(field.getName().toUpperCase());
                if (value == null) {
                    continue;
                }

                field.setAccessible(true);
                field.set(instance, value);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 加载属性文件并且转成javabean
     *
     * @param filePath String 属性文件路径
     * @param clazz    Class 需要转换的类
     * @return map对象
     */
    public static <T> Map<String, String> loadValue(String filePath, Class<T> clazz) {
        Map<String, String> map = loadValue(filePath);
        convertToClass(map, clazz);
        return map;
    }
}
