package com.coyotwilly.casso.contracts.services;

import com.coyotwilly.casso.dtos.CounterDataDto;
import com.coyotwilly.casso.models.abstracts.SessionCounters;

public interface ICounterService {
    <T extends SessionCounters> T getCurrentCounterValue(String id, Class<T> clazz);
    <T extends SessionCounters> CounterDataDto create(String id, Long value, Class<T> clazz);
    <T extends SessionCounters> CounterDataDto increment(String id, Long amount, Class<T> clazz);
    <T extends SessionCounters> CounterDataDto decrement(String id, Long amount, Class<T> clazz);
}
