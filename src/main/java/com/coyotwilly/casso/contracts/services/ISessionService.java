package com.coyotwilly.casso.contracts.services;

import com.coyotwilly.casso.models.entities.Session;

import java.util.List;
import java.util.UUID;

public interface ISessionService {
    List<Session> getSessions();
    Session getSessionByEmail(String email, Boolean withCheck);
    Session getSessionOrDefaultByEmail(String email);
    Session getSessionById(UUID sessionId, Boolean withCheck);
    Session getSessionOrDefaultById(UUID sessionId);
    Session getSessionByMacAddress(String macAddress, Boolean withCheck);
    Session getSessionOrDefaultByMacAddress(String macAddress);
    Session createSession(Session session);
    Session updateSession(Session session);
    void deleteSessionByEmail(String email);
    void deleteSessionById(UUID sessionId);
    void deleteSessionByMacAddress(String macAddress);
}
