package com.coyotwilly.casso.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Builder
@Table("sessions")
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @PrimaryKey
    @Column("email")
    @CassandraType(type = CassandraType.Name.TEXT)
    private String email;

    @Indexed
    @Column("session_id")
    @CassandraType(type = CassandraType.Name.UUID)
    private UUID sessionId = UUID.randomUUID();

    @Indexed
    @Column("mac_address")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String macAddress;

    @Column("ip_address")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String ipAddress;

    @Column("expiration_time")
    @CassandraType(type = CassandraType.Name.TIMESTAMP)
    private ZonedDateTime expirationTime;

    @Override
    public String toString() {
        return String.format("{ @type = %1$s, id = %2$s, sessionId = %3$s, mac address = %4$s, IP address = %5$s, " +
                        "lock expiration time = %6$s }", getClass().getName(), getEmail(), getSessionId().toString(),
                getMacAddress(), getIpAddress(), getExpirationTime().toOffsetDateTime());
    }
}
