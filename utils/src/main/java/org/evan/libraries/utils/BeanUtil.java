package org.evan.libraries.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.Assert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Evan.Shen
 * @since 2019-09-21
 */
@Slf4j
public class BeanUtil {
    private final static Map<Class<?>, Map<Class<?>, BeanCopier>> beanCopiers = new ConcurrentHashMap<>(128);
    private static Mapper dozerBeanMapper;

    static {
        dozerBeanMapper = DozerBeanMapperBuilder.buildDefault();

        ConvertUtilsBean convertUtils = BeanUtilsBean.getInstance().getConvertUtils();

        DateConverter dateConverter = new DateConverter();
        dateConverter.setPattern("yyyy-MM-dd HH:mm:ss");
        convertUtils.register(dateConverter, String.class);
    }


    /**
     * 对象快速复制
     * <pre>
     * Demo demo = new Demo();
     * demo.setXX();
     * ...
     * DemoQuery query = BeanUtils.quickMap(demo, DemoQuery.class);
     * </pre>
     *
     * @param source
     * @param targetClass <p>
     *                    author: ShenWei<br>
     *                    create at 2015年5月20日 上午11:20:33
     */
    public static <T> T quickMap(Object source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        BeanCopier beanCopier = getBeanCopier(source.getClass(), targetClass);

        T to = null;

        try {
            to = targetClass.newInstance();
        } catch (InstantiationException e) {
            throw new UnsupportedOperationException("Class " + targetClass + "not hava constructor is no params", e);
        } catch (IllegalAccessException e) {
            throw new UnsupportedOperationException("Class " + targetClass + "not hava constructor is no params", e);
        }

        beanCopier.copy(source, to, null);
        return to;
    }

    /**
     * 对象快速复制
     */
    public static <T> List<T> quickMapList(Collection<?> sourceList, Class<T> targetClass) {
        List<T> targetList = new ArrayList<T>();
        if (sourceList.isEmpty()) {
            return targetList;
        }

        Iterator<?> it = sourceList.iterator();
        BeanCopier beanCopier = getBeanCopier(it.next().getClass(), targetClass);
        try {
            for (Object source : sourceList) {
                T target;
                target = targetClass.newInstance();
                beanCopier.copy(source, target, null);
                targetList.add(target);
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Class " + targetClass + "not hava constructor is no params", e);
        }
        return targetList;
    }

    /**
     * 对象快速拷贝
     */
    public static void quickCopy(Object source, Object target) {
        Assert.notNull(source, "source must not null");
        Assert.notNull(target, "target must not null");
        BeanCopier beanCopier = getBeanCopier(source.getClass(), target.getClass());
        beanCopier.copy(source, target, null);
    }

    /**
     * 对象深拷贝
     */
    public static <T> T adviceMap(Object source, Class<T> destinationClass) {
        if (source == null) {
            return null;
        }

        return dozerBeanMapper.map(source, destinationClass);
    }

    /**
     * 列表深拷贝
     */
    public static <T> List<T> adviceMapList(Collection<?> sourceList, Class<T> destinationClass) {
        List<T> destinationList = new ArrayList<T>();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozerBeanMapper.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 对象深拷贝
     */
    public static void adviceCopy(Object source, Object destinationObject) {
        Assert.notNull(source, "source must not null");
        Assert.notNull(destinationObject, "destinationObject must not null");
        dozerBeanMapper.map(source, destinationObject);
    }

    /**
     * @param bean
     * @return
     */
    public static Map<String, String> beanToMap(Object bean) {
        Map<String, String> resultMap = null;

        try {
            resultMap = BeanUtils.describe(bean);
        } catch (IllegalAccessException | InvocationTargetException |
                NoSuchMethodException ex) {
            log.error(ex.getMessage(), ex);
        }

        if (resultMap != null) {
            for (Map.Entry entry : resultMap.entrySet()) {
                if (entry.getValue() == null) {
                    resultMap.remove(entry.getKey());
                }
            }
        }

        return resultMap;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean
     */
    public static Map<String, Object> beanToMap2(Object bean) {
        Class type = bean.getClass();
        Map returnMap = null;
        BeanInfo beanInfo = null;

        try {
            beanInfo = Introspector.getBeanInfo(type);
        } catch (IntrospectionException ex) {
            log.error("bean [" + bean + "] to map failure", ex);
            return returnMap;
        }

        returnMap = new HashMap();

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!StringUtil.equals(propertyName, "class")) {
                Method readMethod = descriptor.getReadMethod();

                Object result = null;
                try {
                    result = readMethod.invoke(bean);
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    log.error("method [" + readMethod + "] invoke failure", ex);
                }

                if (result != null) {
                    returnMap.put(propertyName, result);
                }
//                else {
//                    returnMap.put(propertyName, "");
//                }
            }
        }
        return returnMap;
    }

//    /**
//     * 将一个 Map 对象转化为一个 JavaBean
//     *
//     * @param type 要转化的类型
//     * @param map  包含属性值的 map
//     *             转化出来的 JavaBean 对象
//     * @throws IntrospectionException    如果分析类属性失败
//     * @throws IllegalAccessException    如果实例化 JavaBean 失败
//     * @throws InstantiationException    如果实例化 JavaBean 失败
//     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
//     */
//    public static Object convertBean(Class type, Map map) {
//        BeanInfo beanInfo = null;
//
//        try {
//            BeanInfo beanInfo = Introspector.getBeanInfo(type);
//        } catch (IntrospectionException ex) {
//
//        }
//
//
//        Object obj = type.newInstance(); // 创建 JavaBean 对象
//
//        // 给 JavaBean 对象的属性赋值
//        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        for (int i = 0; i < propertyDescriptors.length; i++) {
//            PropertyDescriptor descriptor = propertyDescriptors[i];
//            String propertyName = descriptor.getName();
//
//            if (map.containsKey(propertyName)) {
//                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
//                Object value = map.get(propertyName);
//
//                Object[] args = new Object[1];
//                args[0] = value;
//
//                descriptor.getWriteMethod().invoke(obj, args);
//            }
//        }
//        return obj;
//    }

    /**
     * 获取一个对象的对象名
     *
     * @param po objectName
     */
    public static String getClassName(Object po) {
        String returnString = StringUtils.capitalize(po.getClass().getSimpleName());
        return returnString;
    }

    /**
     * 获取一个类的对象名
     *
     * @param c objectName
     */
    public static String getObjectName(Class<?> c) {
        String returnString = StringUtils.uncapitalize(c.getSimpleName());
        return returnString;
    }

    private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass) {
        Map<Class<?>, BeanCopier> mapInner = beanCopiers.get(sourceClass);
        BeanCopier beanCopier = null;
        if (mapInner == null) {
            mapInner = new ConcurrentHashMap<Class<?>, BeanCopier>(128);
            beanCopier = BeanCopier.create(sourceClass, targetClass, false);
            mapInner.put(targetClass, beanCopier);
            beanCopiers.put(sourceClass, mapInner);
        } else {
            beanCopier = mapInner.get(targetClass);
            if (beanCopier == null) {
                beanCopier = BeanCopier.create(sourceClass, targetClass, false);
                mapInner.put(targetClass, beanCopier);
            }
        }
        return beanCopier;
    }
//    public static void eachProperties(Object bean, EachPropertiesHandler eachPropertiesHandler) {
//        eachPropertiesInner(bean.getClass(), bean, eachPropertiesHandler);
//    }

//    /**
//     * 将bean转换层map
//     *
//     * @param bean Map<String, Object>
//     */
//    public static Map<String, Object> beanToMap(Object bean) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        beanToMapInner(bean.getClass(), bean, map);
//        return map;
//    }

//    /**
//     * 将bean转换成queryString<br>
//     * <br>
//     * 返回格式：
//     *
//     * <pre>
//     * property1=1&property2=2
//     * </pre>
//     * <p>
//     * shen.wei create at 2013-11-6
//     * 上午11:00:56
//     * </p>
//     *
//     * @param bean
//     * @param dateFormat String
//     */
//    public static String beanToQueryString(Object bean, String dateFormat, String charset) {
//        StringBuffer string = new StringBuffer();
//        beanToQueryStringInner(bean.getClass(), string, bean, dateFormat, charset);
//        if (string.length() > 0) {
//            string.delete(0, 1);
//        }
//        return string.toString();
//    }

//    /**
//     * 将bean转换成queryString<br>
//     * 编码：utf-8
//     *
//     * @param bean
//     * @param dateFormat
//     *  <p>
//     *         author: ShenWei<br>
//     *         create at 2015年5月20日 上午11:18:05
//     */
//    public static String beanToQueryString(Object bean, String dateFormat) {
//        return beanToQueryString(bean, dateFormat, DEFAULT_CHARSET);
//    }

//    /**
//     * 将bean转换成queryString<br>
//     * 编码：utf-8<br>
//     * 日期格式：yyyy-MM-dd
//     *
//     * @param bean
//     *  <p>
//     *         author: ShenWei<br>
//     *         create at 2015年5月20日 上午11:18:13
//     */
//    public static String beanToQueryString(Object bean) {
//        return beanToQueryString(bean, "yyyy-MM-dd", DEFAULT_CHARSET);
//    }

}
