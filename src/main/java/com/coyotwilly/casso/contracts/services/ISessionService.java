package com.coyotwilly.casso.contracts.services;

import com.coyotwilly.casso.models.entities.Session;

import java.util.List;
import java.util.UUID;

public interface ISessionService {
    List<Session> getSessions();
    Session getSessionByLogin(String login, Boolean withCheck);
    Session getSessionOrDefaultByLogin(String login);
    Session getSessionById(UUID sessionId, Boolean withCheck);
    Session getSessionOrDefaultById(UUID sessionId);
    Session getSessionByMacAddress(String macAddress, Boolean withCheck);
    Session getSessionOrDefaultByMacAddress(String macAddress);
    Session createSession(Session session);
    Session updateSession(Session session);
    void deleteSessionByLogin(String email);
    void deleteSessionById(UUID sessionId);
    void deleteSessionByMacAddress(String macAddress);
}
