package com.coyotwilly.casso.services;

import com.coyotwilly.casso.consts.SessionQueries;
import com.coyotwilly.casso.contracts.services.ICqlMapper;
import com.coyotwilly.casso.contracts.services.ISessionService;
import com.coyotwilly.casso.exceptions.EntityNotFoundException;
import com.coyotwilly.casso.models.entities.Session;
import com.coyotwilly.casso.utils.AnnotationUtils;
import com.coyotwilly.casso.utils.CqlModificationUtils;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SessionService implements ISessionService {
    private final Class<Session> clazz = Session.class;
    private final CqlSession cql;
    private final ICqlMapper cqlMapper;

    @Override
    public List<Session> getSessions() {
        PreparedStatement statement = cql.prepare(SessionQueries.SELECT_ALL_SESSIONS);
        BoundStatement state = statement.bind();

        return cqlMapper.map(cql.execute(state), clazz);
    }

    @Override
    public Session getSessionByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(SessionQueries.SELECT_SESSION_BY_EMAIL,
                AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(email);

        return cqlMapper.mapToSingle(cql.execute(state), clazz);
    }

    @Override
    public Session getSessionById(UUID sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(SessionQueries.SELECT_SESSION_BY_ID,
                AnnotationUtils.getTableName(clazz), AnnotationUtils.getUuidColumnName(clazz)));
        BoundStatement state = statement.bind(sessionId);

        return cqlMapper.mapToSingle(cql.execute(state), clazz);
    }

    @Override
    public Session createSession(Session session) {
        if (session.getEmail() == null || session.getEmail().isEmpty()) {
            session.setEmail(UUID.randomUUID().toString().subSequence(0, 8) + "@cassandra.com");
        }

        PreparedStatement statement = cql.prepare(CqlModificationUtils.insert(clazz));
        BoundStatement state = statement.bind(session.getEmail(), session.getSessionId(), session.getMacAddress(),
                session.getIpAddress(), session.getExpirationTime());
        cql.execute(state);

        return resultSession(session);
    }

    @Override
    public Session updateSession(Session session) {
        PreparedStatement statement = cql.prepare(CqlModificationUtils.update(clazz));
        BoundStatement state = statement.bind(session.getSessionId(), session.getMacAddress(),
                session.getIpAddress(), session.getExpirationTime(), session.getEmail());

        ResultSet rs = cql.execute(state);
        if (cqlMapper.hasFailed(rs)) {
            throw new EntityNotFoundException(clazz.getSimpleName());
        }

        return resultSession(session);
    }

    @Override
    public void deleteSessionByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(SessionQueries.DELETE_SESSION_BY_EMAIL,
                AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(email);

        checkOperationResult(state);
    }

    @Override
    public void deleteSessionById(UUID sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        Session session = getSessionById(sessionId);
        deleteSessionByEmail(session.getEmail());
    }

    private Session resultSession(Session session) {
        if (session.getEmail() != null && !session.getEmail().isEmpty()) {
            return getSessionByEmail(session.getEmail());
        }

        return getSessionById(session.getSessionId());
    }

    private void checkOperationResult(BoundStatement state) {
        ResultSet rs = cql.execute(state);

        if (cqlMapper.hasFailed(rs)) {
            throw new EntityNotFoundException(clazz.getSimpleName());
        }
    }
}
