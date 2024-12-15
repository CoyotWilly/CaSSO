package com.coyotwilly.casso.models.entities;

import com.coyotwilly.casso.models.abstracts.SessionCounters;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@NoArgsConstructor
@AllArgsConstructor
@Table("user_session_counter_locks")
public class UserSessionCounters extends SessionCounters {
    @PrimaryKey
    @Column("email")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String email;
}
