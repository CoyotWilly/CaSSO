package com.coyotwilly.casso.models.abstracts;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;

@Data
public abstract class SessionCounters {
    @Column("counter")
    @CassandraType(type = CassandraType.Name.COUNTER)
    protected Long counter;
}
