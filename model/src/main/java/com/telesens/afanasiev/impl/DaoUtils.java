package com.telesens.afanasiev.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oleg on 1/17/16.
 */
public class DaoUtils {
    /**
     *
     * @param entity any object
     * @param fieldName String with name of private field
     * @param value value with will be set to field
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * This method collects all private fields from entity  class, his ancestors till Object class,
     * takes field with defined name and sets defined value.
     */
    public static void setPrivateField(Object entity, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        List<Field> fields = new ArrayList<Field>();
        Class clazz = entity.getClass();
        while (!clazz.equals(Object.class)) {
            fields = Arrays.asList(clazz.getDeclaredFields());
            for (Field f : fields) {
                if (f.getName().equals(fieldName)) {
                    f.setAccessible(true);
                    f.set(entity, value);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }
}
