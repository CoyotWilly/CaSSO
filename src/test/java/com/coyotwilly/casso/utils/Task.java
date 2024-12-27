package com.coyotwilly.casso.utils;

import com.coyotwilly.casso.contracts.services.IUserService;
import com.coyotwilly.casso.models.entities.User;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

@AllArgsConstructor
public class Task implements Callable<Void> {
    private final int startMarker;
    private final int count;
    private final IUserService userService;
    private final List<User> users;

    private final Random rand = new Random();

    @Override
    public Void call() {
        // TODO here goes implementation of dummy data insert to Cassandra
        for (int i = startMarker; i < startMarker + count; i++) {
            userService.createUser(users.get(i));
        }

        return null;
    }
}
