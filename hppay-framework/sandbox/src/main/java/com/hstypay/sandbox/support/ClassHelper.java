package com.hstypay.sandbox.support;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Vincent
 * @version 1.0 2017-06-30 21:27
 */
@SuppressWarnings("unchecked")
public class ClassHelper {

    /**
     * 给一个接口，返回这个接口的所有实现类
     *
     * @param c 接口类
     * @return 列表
     */
    public static List<Class<Object>> getAllClassByInterface(Class c) {
        List<Class<Object>> returnClassList = new ArrayList<Class<Object>>();

        String packageName = c.getPackage().getName(); // 获得当前的包名
        try {
            List<Class<Object>> allClass = getClasses(packageName); // 获得当前包下以及子包下的所有类
            // 判断是否是同一个接口
            for (Class<Object> allClas : allClass) {
                if (c.isAssignableFrom(allClas)) { // 判断是不是一个接口
                    if (!c.equals(allClas)) { // 本身不加进去
                        returnClassList.add(allClas);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return returnClassList;
    }

    /**
     * 从一个包中查找出所有的类，在jar包中不能查找
     *
     * @param packageName 包路径
     * @return 列表
     * @throws ClassNotFoundException 异常
     * @throws IOException            异常
     */
    private static List<Class<Object>> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<Object>> classes = new ArrayList<Class<Object>>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<Object>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<Object>> classes = new ArrayList<Class<Object>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add((Class<Object>) Class.forName(packageName + '.'
                        + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
