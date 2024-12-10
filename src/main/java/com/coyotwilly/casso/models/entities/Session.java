package com.coyotwilly.casso.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Table("users")
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @PrimaryKey
    @CassandraType(type = CassandraType.Name.TEXT)
    private String email;

    @Column("session_id")
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID sessionId;

    @Column("mac_address")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String macAddress;

    @Column("ip_address")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String ipAddress;

    @Column("expiration_time")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private ZonedDateTime ExpirationTime;
}
