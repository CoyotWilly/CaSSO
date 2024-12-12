package com.coyotwilly.casso.utils;

import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.lang.reflect.Field;

public class AnnotationUtils {
    public static <T> String getTableName(Class<T> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            return clazz.getAnnotation(Table.class).value();
        }

        throw new IllegalStateException("Class " + clazz.getName() + " is not annotated with @Table");
    }

    public static <T> String getPrimaryKeyFieldName(Class<T> clazz) {
        for (Field field : ClassUtils.getAllFields(clazz)) {
            if (field.isAnnotationPresent(PrimaryKey.class) && field.isAnnotationPresent(Column.class)) {
                return field.getAnnotation(Column.class).value();
            }
        }

        throw new IllegalStateException("Class " + clazz.getName() +
                " does not contain field annotated with @PrimaryKey or is not annotation @Column is not present");
    }

    public static <T> String getCounterColumnName(Class<T> clazz) {
        for (Field field : ClassUtils.getAllFields(clazz)) {
            if (field.isAnnotationPresent(Column.class) && field.isAnnotationPresent(CassandraType.class) &&
            field.getAnnotation(CassandraType.class).type() == CassandraType.Name.COUNTER) {
                return field.getName();
            }
        }

        throw new IllegalStateException("Class " + clazz.getName() + " does not contain any counter field");
    }

    public static <T> String getColumnName(Field field) {
        if (field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).value();
        }

        throw new IllegalStateException("Class " + field.getClass().getName() + " does not contain field " +
                field.getName() + " annotated with @Column");
    }
}
