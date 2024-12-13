package com.coyotwilly.casso.utils;

import com.coyotwilly.casso.consts.GenericQueries;

public class CqlModificationUtils {
    public static <T> String insert(Class<T> clazz) {
        return String.format(GenericQueries.GENERIC_INSERT,
                AnnotationUtils.getTableName(clazz),
                ClassUtils.getAllFieldsAsCqlString(clazz, ", "),
                ClassUtils.getAllFieldsAsCqlParametersString(clazz));
    }

    public static <T> String update(Class<T> clazz) {
        return String.format(GenericQueries.GENERIC_UPDATE,
                AnnotationUtils.getTableName(clazz),
                ClassUtils.getFieldsWithoutIdAsCqlString(clazz, " = ?, "),
                AnnotationUtils.getPrimaryKeyFieldName(clazz));
    }
}
