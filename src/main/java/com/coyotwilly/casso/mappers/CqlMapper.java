package com.coyotwilly.casso.mappers;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class CqlMapper {
    public <T> T map(Row row, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                field.set(instance, row.getObject(field.getName()));
            }

            return instance;
        } catch (Exception e) {
            throw new MapperException(
                    String.format("Failed to map row %s to class %s", row.toString(), clazz.getSimpleName()), e);
        }
    }

    public <T> List<T> map(ResultSet rs, Class<T> clazz) {
        List<T> results = new ArrayList<>();
        for (Row row : rs) {
            results.add(map(row, clazz));
        }

        return results;
    }
}
