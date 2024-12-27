package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.contracts.controllers.IDemoController;
import com.coyotwilly.casso.contracts.controllers.ISessionController;
import com.coyotwilly.casso.contracts.services.ICounterService;
import com.coyotwilly.casso.contracts.services.ISessionLockDataService;
import com.coyotwilly.casso.contracts.services.ISessionService;
import com.coyotwilly.casso.contracts.services.IUserService;
import com.coyotwilly.casso.dtos.CredentialDto;
import com.coyotwilly.casso.dtos.FullLogoutDto;
import com.coyotwilly.casso.dtos.ValidationResult;
import com.coyotwilly.casso.exceptions.CredentialTypeException;
import com.coyotwilly.casso.models.entities.*;
import com.coyotwilly.casso.services.PasswordService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DemoController implements IDemoController {
    private final IUserService userService;
    private final ISessionService sessionService;
    private final ICounterService counterService;
    private final ISessionLockDataService sessionDataService;
    private final PasswordService passwordService;

    @Value("${casso.casssandra.lock-time}")
    private Long lockTime;

    @Value("${casso.casssandra.lock-attempts}")
    private Long permittedAttempts;

    @Value("${casso.cassandra.session-duration}")
    private Long duration;

    @Override
    public ResponseEntity<Session> login(CredentialDto credential) throws Exception {
        User user = userService.getUser(credential.login());
        UserSessionCounters userSessionCounter = counterService
                .getCurrentCounterValue(credential.login(), UserSessionCounters.class);
        DeviceSessionCounters deviceSessionCounter = counterService
                .getCurrentCounterValue(credential.macAddress(), DeviceSessionCounters.class);
        UserSessionLocksData userLock = sessionDataService
                .getSessionLockDataById(credential.login(), UserSessionLocksData.class);
        DeviceSessionLocksData deviceLock = sessionDataService
                .getSessionLockDataById(credential.macAddress(), DeviceSessionLocksData.class);

        if (!passwordService.validatePassword(credential.password(), user.getPassword())) {
            counterService.increment(credential.login(), 1L, UserSessionCounters.class);
            counterService.increment(credential.macAddress(), 1L, DeviceSessionCounters.class);

            if (deviceSessionCounter.getCounter() > permittedAttempts || userSessionCounter.getCounter() > permittedAttempts) {
                deviceLock.setLockExpirationTime(ZonedDateTime.now().plusMinutes(lockTime));
                userLock.setLockExpirationTime(ZonedDateTime.now().plusMinutes(lockTime));
                sessionDataService.updateSessionLockData(deviceLock, DeviceSessionLocksData.class);
                sessionDataService.updateSessionLockData(userLock, UserSessionLocksData.class);
            }

            throw new AuthException();
        }

        ZonedDateTime now = ZonedDateTime.ofInstant(ZonedDateTime.now().toInstant(), ZoneOffset.UTC);

        if (userLock.getLockExpirationTime() != null &&
                userSessionCounter.getCounter() > permittedAttempts && userLock.getLockExpirationTime().isBefore(now)) {
            counterService.decrement(credential.login(), userSessionCounter.getCounter(), UserSessionCounters.class);
        } else if (userLock.getLockExpirationTime() != null &&
                userLock.getLockExpirationTime().isAfter(now) && userSessionCounter.getCounter() > permittedAttempts) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (deviceLock.getLockExpirationTime() != null &&
                deviceSessionCounter.getCounter() > permittedAttempts && deviceLock.getLockExpirationTime().isBefore(now)) {
            counterService.decrement(credential.macAddress(), userSessionCounter.getCounter(), DeviceSessionCounters.class);
        } else if (deviceLock.getLockExpirationTime() != null &&
            deviceLock.getLockExpirationTime().isAfter(now) && deviceSessionCounter.getCounter() > permittedAttempts) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        counterService.decrement(credential.login(), userSessionCounter.getCounter(), UserSessionCounters.class);
        counterService.decrement(credential.macAddress(), userSessionCounter.getCounter(), DeviceSessionCounters.class);
        Session session = sessionService.createSession(Session.builder()
                        .login(credential.login())
                        .sessionId(UUID.randomUUID())
                        .macAddress(credential.macAddress())
                        .expirationTime(now.plusHours(duration))
                        .build());

        return ResponseEntity.ok().body(session);
    }

    @Override
    public ResponseEntity<ValidationResult> validate(String type, String id) throws CredentialTypeException {
        Session session = switch (type) {
            case "login" -> sessionService.getSessionOrDefaultByLogin(id);
            case "device" -> sessionService.getSessionOrDefaultByMacAddress(id);
            case "uuid" -> sessionService.getSessionOrDefaultById(UUID.fromString(id));
            default -> throw new CredentialTypeException(type);
        };

        if (session != null && session.getExpirationTime()
                .isAfter(ZonedDateTime.ofInstant(ZonedDateTime.now().toInstant(), ZoneOffset.UTC))) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.EXPIRES, session.getExpirationTime().toString())
                    .header(HttpHeaders.LAST_MODIFIED,
                            ISessionController.SessionControllerPath.SESSIONS + "/" + session.getLogin())
                    .body(new ValidationResult(true));
        }

        return ResponseEntity.ok().body(new ValidationResult(false));
    }

    @Override
    public ResponseEntity<String> logout(String type, String id) throws CredentialTypeException {
        switch (type) {
            case "login":
                sessionService.deleteSessionByLogin(id);
                break;
            case "device":
                sessionService.deleteSessionByMacAddress(id);
                break;
            case "uuid":
                sessionService.deleteSessionById(UUID.fromString(id));
                break;
            default:
                throw new CredentialTypeException(type);
        }

        return ResponseEntity.ok().body("User with identifier: " + id + " has been logged out.");
    }

    @Override
    public ResponseEntity<String> logout(FullLogoutDto dto) {
        sessionService.deleteSessionByLogin(dto.login());
        if (sessionService.getSessionOrDefaultByMacAddress(dto.macAddress()) != null) {
            sessionService.deleteSessionByMacAddress(dto.macAddress());
        }

        return ResponseEntity.ok().body("User with login: " + dto.login() + "and device MAC address: " +
                dto.macAddress() + "has been logged out.");
    }
}
