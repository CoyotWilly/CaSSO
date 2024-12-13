package com.coyotwilly.casso.services;

import com.coyotwilly.casso.consts.UserQueries;
import com.coyotwilly.casso.contracts.ICqlMapper;
import com.coyotwilly.casso.contracts.IUserService;
import com.coyotwilly.casso.exceptions.EntityNotFoundException;
import com.coyotwilly.casso.models.entities.User;
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
public class UserService implements IUserService {
    private final Class<User> clazz = User.class;
    private final CqlSession cql;
    private final ICqlMapper cqlMapper;

    @Override
    public List<User> getUsers() {
        PreparedStatement statement = cql.prepare(UserQueries.SELECT_ALL_USERS);
        BoundStatement state = statement.bind();

        return cqlMapper.map(cql.execute(state), User.class);
    }

    @Override
    public User getUser(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(UserQueries.SELECT_USER_BY_ID,
                AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(id);

        return cqlMapper.mapToSingle(cql.execute(state), User.class);
    }

    @Override
    public User createUser(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }

        PreparedStatement statement = cql.prepare(CqlModificationUtils.insert(clazz));
        BoundStatement state = statement.bind(user.getId(), user.getName(), user.getLogin(),
                user.getPassword(), user.getIsLocked());
        cql.execute(state);

        return getUser(user.getId());
    }

    @Override
    public User updateUser(User user) {
        PreparedStatement statement = cql.prepare(CqlModificationUtils.update(clazz));
        BoundStatement state = statement.bind(user.getName(), user.getLogin(),
                user.getPassword(), user.getIsLocked(), user.getId());

        ResultSet rs = cql.execute(state);
        if (cqlMapper.hasFailed(rs)) {
            throw new EntityNotFoundException(clazz.getSimpleName());
        }

        return getUser(user.getId());
    }

    @Override
    public void deleteUser(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("id cannot be null or empty");
        }

        PreparedStatement statement = cql.prepare(String.format(UserQueries.DELETE_USER_BY_ID,
                AnnotationUtils.getTableName(clazz), AnnotationUtils.getPrimaryKeyFieldName(clazz)));
        BoundStatement state = statement.bind(id);
        ResultSet rs = cql.execute(state);

        if (cqlMapper.hasFailed(rs)) {
            throw new EntityNotFoundException(clazz.getSimpleName());
        }
    }
}
