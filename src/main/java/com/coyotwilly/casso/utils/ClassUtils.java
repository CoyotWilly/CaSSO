package com.coyotwilly.casso.utils;

import java.lang.reflect.Field;

public class ClassUtils {
    public static <T> Field[] getAllFields(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        if (clazz.getSuperclass() != null) {
            Field[] parentFields = getAllFields(clazz.getSuperclass());
            Field[] allFields = new Field[parentFields.length + fields.length];
            System.arraycopy(fields, 0, allFields, 0, fields.length);
            System.arraycopy(parentFields, 0, allFields, fields.length, parentFields.length);

            return allFields;
        } else {
            return fields;
        }
    }
}
