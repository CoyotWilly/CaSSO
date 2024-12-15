package com.coyotwilly.casso.services;

import com.coyotwilly.casso.consts.GenericQueries;
import com.coyotwilly.casso.consts.SessionLockDataQueries;
import com.coyotwilly.casso.contracts.services.ISessionLockDataService;
import com.coyotwilly.casso.exceptions.EntityNotFoundException;
import com.coyotwilly.casso.mappers.CqlMapper;
import com.coyotwilly.casso.models.abstracts.SessionLocksData;
import com.coyotwilly.casso.utils.AnnotationUtils;
import com.coyotwilly.casso.utils.CqlModificationUtils;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionLockDataService implements ISessionLockDataService {
    private final CqlSession cql;
    private final CqlMapper cqlMapper;

    @Override
    public <T extends SessionLocksData> T getSessionLockDataById(String id, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(SessionLockDataQueries.GET_SESSION_LOCK_DATA_BY_ID,
                        AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldNameOrDefault(clazz)));
        BoundStatement state = statement.bind(id);

        return cqlMapper.mapToSingle(cql.execute(state), clazz);
    }

    @Override
    public <T extends SessionLocksData> T createSessionLockData(T lockData, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(CqlModificationUtils.insert(clazz));
        BoundStatement state = statement.bind(lockData.getEntityId(), lockData.getLockExpirationTime().toInstant());
        cql.execute(state);

        return getSessionLockDataById(lockData.getEntityId(), clazz);
    }

    @Override
    public <T extends SessionLocksData> T updateSessionLockData(T lockData, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(CqlModificationUtils.update(clazz));
        BoundStatement state = statement.bind(lockData.getLockExpirationTime().toInstant(), lockData.getEntityId());
        executeWithOperationResultCheck(state, clazz);

        return getSessionLockDataById(lockData.getEntityId(), clazz);
    }

    @Override
    public <T extends SessionLocksData> void deleteSessionLockDataById(String id, Class<T> clazz) {
        PreparedStatement statement = cql.prepare(
                String.format(GenericQueries.GENERIC_DELETE,
                        AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(id);
        executeWithOperationResultCheck(state, clazz);
    }

    private <T extends SessionLocksData> void executeWithOperationResultCheck(BoundStatement state, Class<T> clazz) {
        ResultSet rs = cql.execute(state);

        if (cqlMapper.hasFailed(rs)) {
            throw new EntityNotFoundException(clazz.getSimpleName());
        }
    }
}
