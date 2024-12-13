package com.coyotwilly.casso.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

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

    public static <T> String getAllFieldsAsCqlParametersString(Class<T> clazz) {
        return getAllFieldsAsCqlParametersString(clazz, ", ");
    }

    public static <T> String getAllFieldsAsCqlString(Class<T> clazz, String separator) {
        return Arrays.stream(getAllFields(clazz))
                .map(AnnotationUtils::getColumnNameOrDefault)
                .collect(Collectors.joining(separator));
    }

    public static <T> String getFieldsWithoutIdAsCqlString(Class<T> clazz, String separator) {
        return Arrays.stream(getAllFields(clazz))
                .filter(f -> !AnnotationUtils.isPrimaryKey(f))
                .map(AnnotationUtils::getColumnNameOrDefault)
                .collect(Collectors.joining(separator));
    }

    public static <T> String getAllFieldsAsCqlParametersString(Class<T> clazz, String separator) {
        return Arrays.stream(getAllFields(clazz)).map(m -> "?").collect(Collectors.joining(separator));
    }
}
