package com.coyotwilly.casso.services;

import com.coyotwilly.casso.consts.CounterQueries;
import com.coyotwilly.casso.contracts.ICounterService;
import com.coyotwilly.casso.dtos.CounterDataDto;
import com.coyotwilly.casso.mappers.CqlMapper;
import com.coyotwilly.casso.models.abstracts.SessionCounters;
import com.coyotwilly.casso.utils.AnnotationUtils;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CounterService<T extends SessionCounters> implements ICounterService<T> {
    private final CqlSession cql;
    private final CqlMapper cqlMapper;

    @Override
    public CounterDataDto create(Long value, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(CounterQueries.CREATE_COUNTER_QUERY,
                        AnnotationUtils.getTableName(clazz),
                        AnnotationUtils.getCounterColumnName(clazz),
                        AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        if (value == null) {
            value = 1L;
        }

        String uuid = UUID.randomUUID().toString();
        BoundStatement state = statement.bind(value, uuid); // TODO REPLACE TO TAKE ACTUAL ID
        cql.execute(state);

        return new CounterDataDto(uuid, value);
    }

    @Override
    public CounterDataDto increment(String id, Long amount, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(CounterQueries.INCREMENT_COUNTER_QUERY,
                        AnnotationUtils.getTableName(clazz),
                        AnnotationUtils.getCounterColumnName(clazz),
                        AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(amount, id);
        cql.execute(state);

        return new CounterDataDto(id, getCurrentCounterValue(id, clazz).getCounter());
    }

    @Override
    public CounterDataDto decrement(String id, Long amount, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(CounterQueries.DECREMENT_COUNTER_QUERY,
                        AnnotationUtils.getTableName(clazz),
                        AnnotationUtils.getCounterColumnName(clazz),
                        AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(amount, id);
        cql.execute(state);

        return new CounterDataDto(id, getCurrentCounterValue(id, clazz).getCounter());
    }

    private T getCurrentCounterValue(String id, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format("SELECT counter FROM %s WHERE %s = ?",
                        AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(id);

        return cqlMapper.mapToSingle(cql.execute(state), clazz);
    }
}
