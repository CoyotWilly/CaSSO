package com.coyotwilly.casso.dtos;

public record CredentialDto(
        String login,
        String password,
        String macAddress
) {
}
