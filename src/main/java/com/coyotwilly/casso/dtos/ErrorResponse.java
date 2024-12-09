package com.coyotwilly.casso.dtos;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ErrorResponse(
        String exceptionName,
        String exceptionMessage,
        Integer errorCode) {
    public ErrorResponse {
        log.warn("Exception was thrown: {}, {}", exceptionName, exceptionMessage);
    }
}
