package com.coyotwilly.casso.mappers;

import com.coyotwilly.casso.contracts.mappers.ICqlMapper;
import com.coyotwilly.casso.utils.AnnotationUtils;
import com.coyotwilly.casso.utils.ClassUtils;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.mapper.MapperException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class CqlMapper implements ICqlMapper {

    @Override
    public <T> T map(Row row, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : ClassUtils.getAllFields(clazz)) {
                field.setAccessible(true);
                try {
                    if (row.getType(AnnotationUtils.getColumnName(field)) == DataTypes.TIMESTAMP) {
                        Instant instant = row.getInstant(AnnotationUtils.getColumnName(field));
                        if (instant == null) {
                            continue;
                        }

                        field.set(instance, instant.atZone(ZoneOffset.UTC));
                    } else {
                        field.set(instance, row.getObject(AnnotationUtils.getColumnName(field)));
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }

            return instance;
        } catch (Exception e) {
            throw new MapperException(
                    String.format("Failed to map row %s to class %s", row.toString(), clazz.getSimpleName()), e);
        }
    }

    @Override
    public <T> List<T> map(ResultSet rs, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        for (Row row : rs) {
            results.add(map(row, clazz));
        }

        return results;
    }

    @Override
    public <T> T mapToSingle(ResultSet rs, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        for (Row row : rs) {
            results.add(map(row, clazz));
        }

        if (results.size() != 1) {
            throw new AssertionError("Expected exactly 1 result, got " + results.size());
        }

        return results.getFirst();
    }

    @Override
    public boolean hasFailed(ResultSet rs) {
        for (Row row : rs) {
            return !row.getBoolean(0);
        }

        return true;
    }
}
