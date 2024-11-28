package com.coyotwilly.casso.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@Table("sessions")
@AllArgsConstructor
public class Session {
    @PrimaryKey
    private UUID sessionId;

    private UUID userId;
}
