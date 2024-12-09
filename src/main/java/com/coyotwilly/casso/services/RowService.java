package com.coyotwilly.casso.services;

import com.coyotwilly.casso.consts.UserQueries;
import com.coyotwilly.casso.mappers.CqlMapper;
import com.coyotwilly.casso.models.User;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.Statement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RowService {
    private final CqlSession cql;
    private final CqlMapper cqlMapper;

    public List<User> select() {
        PreparedStatement statement = cql.prepare(UserQueries.SELECT_ALL_USERS);
        Statement<BoundStatement> state = statement.bind();

        return cqlMapper.map(cql.execute(state), User.class);
    }

    public User insert() {
        PreparedStatement statement = cql.prepare("insert into users(id, age, name)\n" +
                "values (?, 43, 'Batman')");
        Statement<BoundStatement> state = statement.bind(UUID.randomUUID());
        cql.execute(state);

//        return cqlMapper.map(, User.class).getFirst();
        return null;
    }

    public User update() {
        PreparedStatement statement = cql.prepare("UPDATE users SET name = 'Joker' WHERE name = 'Batman'");
        Statement<BoundStatement> state = statement.bind();

        return cqlMapper.map(cql.execute(state), User.class).getFirst();
    }

    public void delete() {
        PreparedStatement statement = cql.prepare("DELETE FROM users WHERE name = 'Batman'");
        Statement<BoundStatement> state = statement.bind();
        cql.execute(state);
    }
}
