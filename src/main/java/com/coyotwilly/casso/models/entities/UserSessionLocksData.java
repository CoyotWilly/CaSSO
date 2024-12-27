package com.coyotwilly.casso.models.entities;

import com.coyotwilly.casso.models.abstracts.SessionLocksData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Table("user_session_locks_data")
public class UserSessionLocksData extends SessionLocksData {

    public UserSessionLocksData(String id, ZonedDateTime dateTime) {
        login = id;
        lockExpirationTime = dateTime;
    }

    @PrimaryKey
    @Column("login")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String login;

    @Override
    public String getEntityId() {
        return login;
    }
}
