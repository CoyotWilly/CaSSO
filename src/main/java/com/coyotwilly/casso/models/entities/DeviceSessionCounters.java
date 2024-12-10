package com.coyotwilly.casso.models.entities;

import com.coyotwilly.casso.models.abstracts.SessionCounters;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("device_session_counter_locks")
@NoArgsConstructor
@AllArgsConstructor
public class DeviceSessionCounters extends SessionCounters {
    @PrimaryKey
    @Column("mac_address")
    @CassandraType(type = CassandraType.Name.VARCHAR)
    private String macAddress;
}
