package com.coyotwilly.casso.contracts.services;

import com.coyotwilly.casso.models.entities.User;

import java.util.List;

public interface IUserService {
    List<User> getUsers();
    User getUser(String id);
    User createUser(User user);
    User updateUser(User user);
    void deleteUser(String id);
}
