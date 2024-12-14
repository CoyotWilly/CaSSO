package com.coyotwilly.casso.contracts.controllers;

import com.coyotwilly.casso.models.entities.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface ISessionController {
    @GetMapping(SessionControllerPath.SESSIONS)
     ResponseEntity<List<Session>> getSessions();

    @GetMapping(SessionControllerPath.SESSIONS + "/{id}")
    ResponseEntity<Session> getSession(@PathVariable String id);

    @PostMapping(SessionControllerPath.SESSIONS)
    ResponseEntity<Session> createSession(@RequestBody Session session);

    @PutMapping(SessionControllerPath.SESSIONS)
    ResponseEntity<Session> updateSession(@RequestBody Session session);

    @DeleteMapping(SessionControllerPath.SESSIONS + "/{id}")
    ResponseEntity<Void> deleteSession(@PathVariable String id);

    class SessionControllerPath {
        public static final String SESSIONS = "/maintenance/sessions";
    }
}
