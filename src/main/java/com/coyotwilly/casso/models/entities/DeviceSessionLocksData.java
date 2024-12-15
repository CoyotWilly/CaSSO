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
@Table("device_session_locks_data")
public class DeviceSessionLocksData extends SessionLocksData {

    public DeviceSessionLocksData(String id, ZonedDateTime dateTime) {
        macAddress = id;
        lockExpirationTime = dateTime;
    }

    @PrimaryKey
    @Column("mac_address")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String macAddress;

    @Override
    public String getEntityId() {
        return macAddress;
    }
}
