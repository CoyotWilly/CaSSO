package com.coyotwilly.casso.models.abstracts;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;

import java.time.ZonedDateTime;

@Data
public abstract class SessionLocksData {
    @Column("lock_expiration_time")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    protected ZonedDateTime lockExpirationTime;
}
