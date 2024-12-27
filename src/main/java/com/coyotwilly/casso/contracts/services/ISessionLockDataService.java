package com.coyotwilly.casso.contracts.services;

import com.coyotwilly.casso.models.abstracts.SessionLocksData;
import com.datastax.oss.driver.api.core.cql.BoundStatement;

public interface ISessionLockDataService {
    <T extends SessionLocksData> T getSessionLockDataById(String id, Class<T> clazz);
    <T extends SessionLocksData> T createSessionLockData(T lockData, Class<T> clazz);
    <T extends SessionLocksData> BoundStatement createSessionLockDataBatch(T lockData, Class<T> clazz);
    <T extends SessionLocksData> T updateSessionLockData(T lockData, Class<T> clazz);
    <T extends SessionLocksData> void deleteSessionLockDataById(String id, Class<T> clazz);
}
