package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.contracts.controllers.ISessionController;
import com.coyotwilly.casso.contracts.controllers.IUserController;
import com.coyotwilly.casso.contracts.services.ISessionService;
import com.coyotwilly.casso.models.entities.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class SessionController implements ISessionController {
    private final ISessionService sessionService;

    @Override
    public ResponseEntity<List<Session>> getSessions() {
        return ResponseEntity.ok(sessionService.getSessions());
    }

    @Override
    public ResponseEntity<Session> getSession(String id) {
        try {
            return ResponseEntity.ok(sessionService.getSessionById(UUID.fromString(id)));
        } catch (IllegalArgumentException ignored) {
            return ResponseEntity.ok(sessionService.getSessionByEmail(id));
        }
    }

    @Override
    public ResponseEntity<Session> createSession(Session session) {
        Session createSession = sessionService.createSession(session);

        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, IUserController.UserControllerPath.USERS + "/"
                        + createSession.getEmail())
                .body(createSession);
    }

    @Override
    public ResponseEntity<Session> updateSession(Session session) {
        Session update = sessionService.updateSession(session);

        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, IUserController.UserControllerPath.USERS + "/"
                        + update.getEmail())
                .body(update);
    }

    @Override
    public ResponseEntity<Void> deleteSession(String id) {
        try {
            sessionService.deleteSessionById(UUID.fromString(id));
        } catch (IllegalArgumentException ignored) {
            sessionService.deleteSessionByEmail(id);
        }

        return ResponseEntity.noContent().build();
    }
}
