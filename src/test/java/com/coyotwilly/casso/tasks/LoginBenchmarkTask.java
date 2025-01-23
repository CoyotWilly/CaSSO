package com.coyotwilly.casso.tasks;

import com.coyotwilly.casso.contracts.controllers.IDemoController;
import com.coyotwilly.casso.dtos.CredentialDto;
import com.coyotwilly.casso.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
@AllArgsConstructor
public class LoginBenchmarkTask implements Callable<Void> {
    private final int startMarker;
    private final int count;
    private final IDemoController controller;
    private final List<User> users;

    @Override
    public Void call() throws Exception {
        for (int i = startMarker; i < count; i++) {
            User user = users.get(i);
            controller.login(new CredentialDto(user.getLogin(), user.getPassword(), "72:f3:80:02:4f:b9"));
        }

        return null;
    }
}
