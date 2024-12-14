package com.coyotwilly.casso.contracts.services;

import com.coyotwilly.casso.dtos.CounterDataDto;
import com.coyotwilly.casso.models.abstracts.SessionCounters;

public interface ICounterService<T extends SessionCounters> {
    CounterDataDto create(String id, Long value, Class<T> clazz);
    CounterDataDto increment(String id, Long amount, Class<T> clazz);
    CounterDataDto decrement(String id, Long amount, Class<T> clazz);
}
