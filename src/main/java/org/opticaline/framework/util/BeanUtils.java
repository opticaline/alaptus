package org.opticaline.framework.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by Nathan on 2014/9/16.
 */
public class BeanUtils {
    /*public static void set(Object object, String name, Object value) {
        try {
            object.getClass()
                    .getMethod("set" + name.toUpperCase().substring(0, 1) + name.substring(1, name.length()),
                            value.getClass()
                    ).invoke(object, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Object get(Object bean, String name) {
        try {
            return bean.getClass()
                    .getMethod("get" + name.toUpperCase().substring(0, 1) + name.substring(1, name.length())
                    ).invoke(bean);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static <T> T get(Object bean, String name, Class<T> clazz) {
        return (T) get(bean, name);
    }*/

    public static void request2Bean(Map<String, String[]> map, Object bean) {
        PropertyDescriptor[] properties = PropertyUtils.getPropertyDescriptors(bean);
        for (int i = 0; i < properties.length; i++) {
            String name = properties[i].getName();
            Class clazz = properties[i].getPropertyType();
            try {
                if (clazz.isArray()) {
                    org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, map.get(name));
                } else {
                    String[] value = map.get(name);
                    if (value != null && value.length > 0) {
                        org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, map.get(name)[0]);
                    } else {
                        org.apache.commons.beanutils.BeanUtils.setProperty(bean, name, null);
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAge(Integer age) {
        System.out.println(age);
    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        BeanUtils user = new BeanUtils();
        org.apache.commons.beanutils.BeanUtils.setProperty(user, "name", "13");
    }
}
