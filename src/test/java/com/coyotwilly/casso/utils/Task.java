package com.coyotwilly.casso.utils;

import com.coyotwilly.casso.contracts.services.IUserService;
import com.coyotwilly.casso.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
@AllArgsConstructor
public class Task implements Callable<Void> {
    private final int startMarker;
    private final int count;
    private final IUserService userService;
    private final List<User> users;

    @Override
    public Void call() {
        for (int i = startMarker; i < count; i++) {
            userService.createUser(users.get(i));
        }

        return null;
    }
}
