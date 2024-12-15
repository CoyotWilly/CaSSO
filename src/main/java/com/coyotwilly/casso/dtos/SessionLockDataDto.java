package com.coyotwilly.casso.dtos;

import java.time.ZonedDateTime;

public record SessionLockDataDto(
        String id,
        ZonedDateTime expirationTime
) {
}
