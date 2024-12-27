package com.coyotwilly.casso.services;

import com.coyotwilly.casso.consts.GenericQueries;
import com.coyotwilly.casso.consts.Resources;
import com.coyotwilly.casso.consts.SessionQueries;
import com.coyotwilly.casso.contracts.mappers.ICqlMapper;
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
    public Session getSessionOrDefaultByLogin(String login) {
        return getSessionByLogin(login, false);
    }

    @Override
    public Session getSessionByLogin(String login, Boolean withCheck) {
        if (login == null || login.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(SessionQueries.SELECT_SESSION,
                AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(login);

        return cqlMapper.mapToSingle(cql.execute(state), clazz, withCheck);
    }

    @Override
    public Session getSessionOrDefaultById(UUID sessionId) {
        return getSessionById(sessionId, false);
    }

    @Override
    public Session getSessionById(UUID sessionId, Boolean withCheck) {
        if (sessionId == null) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(SessionQueries.SELECT_SESSION_BY_ID,
                AnnotationUtils.getTableName(clazz), AnnotationUtils.getUuidColumnName(clazz)));
        BoundStatement state = statement.bind(sessionId);

        return cqlMapper.mapToSingle(cql.execute(state), clazz, withCheck);
    }

    @Override
    public Session getSessionOrDefaultByMacAddress(String macAddress) {
        return getSessionByMacAddress(macAddress, false);
    }

    @Override
    public Session getSessionByMacAddress(String macAddress, Boolean withCheck) {
        if (macAddress == null || macAddress.isEmpty()) {
            throw new IllegalArgumentException("mac address cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(SessionQueries.SELECT_SESSION,
                AnnotationUtils.getTableName(clazz), Resources.MAC_ADDRESS));
        BoundStatement state = statement.bind(macAddress);

        return cqlMapper.mapToSingle(cql.execute(state), clazz);
    }

    @Override
    public Session createSession(Session session) {
        if (session.getLogin() == null || session.getLogin().isEmpty()) {
            session.setLogin(UUID.randomUUID().toString().subSequence(0, 8) + "@cassandra.com");
        }

        PreparedStatement statement = cql.prepare(CqlModificationUtils.insert(clazz));
        BoundStatement state = statement.bind(session.getLogin(), session.getSessionId(), session.getMacAddress(),
                session.getIpAddress(), session.getExpirationTime().toInstant());
        cql.execute(state);

        return resultSession(session);
    }

    @Override
    public Session updateSession(Session session) {
        PreparedStatement statement = cql.prepare(CqlModificationUtils.update(clazz));
        BoundStatement state = statement.bind(session.getSessionId(), session.getMacAddress(),
                session.getIpAddress(), session.getExpirationTime(), session.getLogin());
        executeWithOperationResultCheck(state);

        return resultSession(session);
    }

    @Override
    public void deleteSessionByLogin(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("email cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(GenericQueries.GENERIC_DELETE,
                AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(email);

        executeWithOperationResultCheck(state);
    }

    @Override
    public void deleteSessionById(UUID sessionId) {
        if (sessionId == null) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        Session session = getSessionById(sessionId, true);
        deleteSessionByLogin(session.getLogin());
    }

    @Override
    public void deleteSessionByMacAddress(String macAddress) {
        if (macAddress == null) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }
        Session session = getSessionByMacAddress(macAddress, true);
        deleteSessionByLogin(session.getLogin());
    }

    private Session resultSession(Session session) {
        if (session.getLogin() != null && !session.getLogin().isEmpty()) {
            return getSessionByLogin(session.getLogin(), true);
        }

        return getSessionById(session.getSessionId(), true);
    }

    private void executeWithOperationResultCheck(BoundStatement state) {
        ResultSet rs = cql.execute(state);

        if (cqlMapper.hasFailed(rs)) {
            throw new EntityNotFoundException(clazz.getSimpleName());
        }
    }
}
