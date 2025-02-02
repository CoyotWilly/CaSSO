package com.coyotwilly.casso.contracts.mappers;

import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.util.List;

public interface ICqlMapper {
    <T> T map(Row row, Class<T> clazz);
    <T> List<T> map(ResultSet rs, Class<T> clazz);
    <T> T mapToSingle(ResultSet rs, Class<T> clazz);
    <T> T mapToSingle(ResultSet rs, Class<T> clazz, Boolean omitCheck);
    boolean hasFailed(ResultSet rs);
}
