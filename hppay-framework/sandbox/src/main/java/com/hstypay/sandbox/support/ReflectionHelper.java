package com.hstypay.sandbox.support;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * 反射工具
 *
 * @author Vincent
 * @version 1.0 2017/03/07 19:57
 */
public class ReflectionHelper {

    private static final Logger log = Logger.getLogger(ReflectionHelper.class.getName());

    public static final String CGLIB_CLASS_SEPARATOR = "$$";

    private ReflectionHelper() {

    }

    /**
     * 获取类实例的属性值
     *
     * @param clazz              Class 类名
     * @param includeParentClass boolean 是否包括父类的属性值
     * @return 类名.属性名=属性类型
     */
    public static List<Field> getClassFields(Class<?> clazz, boolean includeParentClass) {
        List<Field> fieldList = new ArrayList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        Collections.addAll(fieldList, fields);
        if (includeParentClass) {
            if (clazz.getSuperclass() == null) {
                return fieldList;
            }
            List<Field> _fieldList = getClassFields(clazz.getSuperclass(), true);
            if (_fieldList != null && _fieldList.size() > 0) {
                fieldList.addAll(_fieldList);
            }
        }
        return fieldList;
    }

    /**
     * 调用Getter方法.
     *
     * @param obj          Object 目标对象
     * @param propertyName String 属性名称
     * @return 执行结果
     */
    public static Object invokeGetter(Object obj, String propertyName) {
        String getterMethodName = "get" + StringUtils.capitalize(propertyName);
        return invokeMethod(obj, getterMethodName, new Class[]{}, new Object[]{});
    }

    /**
     * 调用Setter方法.使用value的Class来查找Setter方法.
     *
     * @param obj          Object 目标对象
     * @param propertyName String 属性名称
     * @param value        Object 赋值
     */
    public static void invokeSetter(Object obj, String propertyName, Object value) {
        invokeSetter(obj, propertyName, value, null);
    }

    /**
     * 调用Setter方法.
     *
     * @param obj          Object 目标对象
     * @param propertyName String 属性名称
     * @param value        Object 赋值
     * @param propertyType Class 用于查找Setter方法,为空时使用value的Class替代.
     */
    public static void invokeSetter(Object obj, String propertyName, Object value, Class<?> propertyType) {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = "set" + StringUtils.capitalize(propertyName);
        invokeMethod(obj, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    /**
     * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
     *
     * @param obj       Object 目标对象
     * @param fieldName String 属性名称
     * @return 属性值
     */
    public static Object getFieldValue(final Object obj, final String fieldName) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        Object result = null;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            log.severe("Could not get value for field [" + fieldName + "] on target [" + obj + "]");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
     *
     * @param obj       Object 目标对象
     * @param fieldName String 属性名称
     * @param value     Object 赋值
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
        Field field = getAccessibleField(obj, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }

        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            log.severe("Could not set value for field [" + fieldName + "] on target [" + obj + "]");
            e.printStackTrace();
        }
    }

    /**
     * 对于被cglib AOP过的对象, 取得真实的Class类型.
     *
     * @param clazz Class 代理对象的class
     * @return 真实class
     */
    public static Class<?> getUserClass(Class<?> clazz) {
        if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 检查是否包含属性
     *
     * @param obj       Object 对象
     * @param fieldName String 属性名
     * @return 属性值
     */
    @SuppressWarnings("rawtypes")
    public static Object isExist(Object obj, String fieldName) {
        if (obj instanceof java.util.Map) {
            java.util.Map map = (java.util.Map) obj;
            return map.get(fieldName);
        } else {
            for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
                try {
                    return superClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException ignored) {
                }
            }
            return null;
        }
    }

    /**
     * 直接调用对象方法, 无视private/protected修饰符. 用于一次性调用的情况.
     *
     * @param obj            Object 目标对象
     * @param methodName     String 方法名
     * @param parameterTypes Class<?>[] 参数类型列表
     * @param args           Object[] 参数
     * @return 执行结果
     */
    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
        }

        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.如向上转型到Object仍无法找到, 返回null.
     *
     * @param obj       Object 目标对象
     * @param fieldName String 属性名
     * @return 属性类
     */
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        Validate.notNull(obj, "object can't be null");
        Validate.notBlank(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            } catch (NoSuchFieldException e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj,
     * Object...args)
     * </p>
     *
     * @param obj            Object 目标对象
     * @param methodName     String 方法名
     * @param parameterTypes 参数类型
     * @return 方法体
     */
    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        Validate.notNull(obj, "object can't be null");

        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Method method = superClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException e) {// NOSONAR
                // Method不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class. eg. public UserDao
     * extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be
     * determined
     */
    public static Class<?> getSuperClassGenricType(final Class<?> clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     * </p>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     * @return the index generic declaration, or Object.class if cannot be
     * determined
     */
    public static Class<?> getSuperClassGenricType(final Class<?> clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType.getClass().isAssignableFrom(ParameterizedType.class))) {
            log.warning(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            log.warning("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index].getClass().isAssignableFrom(Class.class))) {
            log.warning(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class<?>) params[index];
    }

    /**
     * 反射初始化，无参
     *
     * @param className String 类的全路径
     * @return 类对象
     */
    public static Object instance(String className) {
        try {
            Class<?> dialectCls = Class.forName(className);
            return dialectCls.newInstance();
        } catch (ClassNotFoundException e) {
            log.severe("can not find class " + className);
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            log.severe("instance class " + className + " error");
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            log.severe("instance class " + className + " error");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将反射时的checked exception转换为unchecked exception.
     *
     * @param e Exception 异常对象
     * @return 运行时异常
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }
}
