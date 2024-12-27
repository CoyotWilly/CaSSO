package com.coyotwilly.casso.services;

import com.coyotwilly.casso.consts.CounterQueries;
import com.coyotwilly.casso.contracts.mappers.ICqlMapper;
import com.coyotwilly.casso.contracts.services.ICounterService;
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
public class CounterService implements ICounterService {
    private final CqlSession cql;
    private final ICqlMapper cqlMapper;

    @Override
    public <T extends SessionCounters> T getCurrentCounterValue(String id, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(CounterQueries.GET_COUNTER_VALUE,
                        AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldNameOrDefault(clazz)));
        BoundStatement state = statement.bind(id);
        T result = cqlMapper.mapToSingle(cql.execute(state), clazz, false);

        if (result == null) {
            CounterDataDto dto = create(id, 0L, clazz);
            return getCurrentCounterValue(dto.uuid(), clazz);
        }

        return result;
    }

    @Override
    public <T extends SessionCounters> CounterDataDto create(String id, Long value, Class<T> clazz) {
        BoundStatement state = prepareCreate(id, value, clazz);
        cql.execute(state);

        return new CounterDataDto(id, value);
    }

    @Override
    public <T extends SessionCounters> BoundStatement createBatch(String id, Long value, Class<T> clazz) {
        return prepareCreate(id, value, clazz);
    }

    @Override
    public <T extends SessionCounters> void increment(String id, Long amount, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(CounterQueries.INCREMENT_COUNTER_QUERY,
                        AnnotationUtils.getTableName(clazz),
                        AnnotationUtils.getCounterColumnName(clazz),
                        AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(amount, id);
        cql.execute(state);

        new CounterDataDto(id, getCurrentCounterValue(id, clazz).getCounter());
    }

    @Override
    public <T extends SessionCounters> void decrement(String id, Long amount, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(CounterQueries.DECREMENT_COUNTER_QUERY,
                        AnnotationUtils.getTableName(clazz),
                        AnnotationUtils.getCounterColumnName(clazz),
                        AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(amount, id);
        ResultSet rs = cql.execute(state);
        cqlMapper.map(rs, clazz);

        new CounterDataDto(id, getCurrentCounterValue(id, clazz).getCounter());
    }

    private <T extends SessionCounters> BoundStatement prepareCreate(String id, Long value, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(CounterQueries.CREATE_COUNTER_QUERY,
                        AnnotationUtils.getTableName(clazz),
                        AnnotationUtils.getCounterColumnName(clazz),
                        AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        if (value == null) {
            value = 1L;
        }

        return statement.bind(value, id);
    }
}
