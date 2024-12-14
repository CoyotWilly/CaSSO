package com.coyotwilly.casso.services;

import com.coyotwilly.casso.consts.CounterQueries;
import com.coyotwilly.casso.contracts.services.ICounterService;
import com.coyotwilly.casso.contracts.services.ICqlMapper;
import com.coyotwilly.casso.dtos.CounterDataDto;
import com.coyotwilly.casso.models.abstracts.SessionCounters;
import com.coyotwilly.casso.utils.AnnotationUtils;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterService<T extends SessionCounters> implements ICounterService<T> {
    private final CqlSession cql;
    private final ICqlMapper cqlMapper;

    @Override
    public CounterDataDto create(String id, Long value, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(CounterQueries.CREATE_COUNTER_QUERY,
                        AnnotationUtils.getTableName(clazz),
                        AnnotationUtils.getCounterColumnName(clazz),
                        AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        if (value == null) {
            value = 1L;
        }

        BoundStatement state = statement.bind(value, id);
        cql.execute(state);

        return new CounterDataDto(id, value);
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
        ResultSet rs = cql.execute(state);
        cqlMapper.map(rs, clazz);

        return new CounterDataDto(id, getCurrentCounterValue(id, clazz).getCounter());
    }

    private T getCurrentCounterValue(String id, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format("SELECT counter FROM %s WHERE %s = ?",
                        AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldNameOrDefault(clazz)));
        BoundStatement state = statement.bind(id);

        return cqlMapper.mapToSingle(cql.execute(state), clazz);
    }
}
