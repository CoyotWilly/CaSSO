package com.coyotwilly.casso.contracts.services;

import com.coyotwilly.casso.dtos.CounterDataDto;
import com.coyotwilly.casso.models.abstracts.SessionCounters;
import com.datastax.oss.driver.api.core.cql.BoundStatement;

public interface ICounterService {
    <T extends SessionCounters> T getCurrentCounterValue(String id, Class<T> clazz);
    <T extends SessionCounters> CounterDataDto create(String id, Long value, Class<T> clazz);
    <T extends SessionCounters> BoundStatement createBatch(String id, Long value, Class<T> clazz);
    <T extends SessionCounters> void increment(String id, Long amount, Class<T> clazz);
    <T extends SessionCounters> void decrement(String id, Long amount, Class<T> clazz);
}
