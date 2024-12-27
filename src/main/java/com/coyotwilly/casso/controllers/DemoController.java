package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.contracts.controllers.IDemoController;
import com.coyotwilly.casso.contracts.services.ICounterService;
import com.coyotwilly.casso.contracts.services.ISessionLockDataService;
import com.coyotwilly.casso.contracts.services.ISessionService;
import com.coyotwilly.casso.contracts.services.IUserService;
import com.coyotwilly.casso.dtos.CredentialDto;
import com.coyotwilly.casso.dtos.FullLogoutDto;
import com.coyotwilly.casso.models.entities.*;
import com.coyotwilly.casso.services.PasswordService;
import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

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

    @PostMapping("/login")
    public ResponseEntity<Session> login(@RequestBody CredentialDto credential) throws Exception {
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

        ZonedDateTime now = ZonedDateTime.now();

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
                        .email(credential.login())
                        .macAddress(credential.macAddress())
                        .expirationTime(ZonedDateTime.now().plusHours(duration))
                        .build());

        return ResponseEntity.ok().body(session);
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<Session> logout(@PathVariable Long id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody FullLogoutDto dto) {
        sessionService.deleteSessionByEmail(dto.login());
        sessionService.deleteSessionByMacAddress(dto.macAddress());

        return ResponseEntity.ok().build();
    }
}
