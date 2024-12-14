package com.coyotwilly.casso.contracts.controllers;

import com.coyotwilly.casso.models.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IUserController {
    @GetMapping(UserControllerPath.USERS)
    ResponseEntity<List<User>> getUsers();

    @GetMapping(UserControllerPath.USERS + "/{id}")
    ResponseEntity<User> getUser(@PathVariable String id);

    @PostMapping(UserControllerPath.USERS)
    ResponseEntity<User> createUser(@RequestBody User user);

    @PutMapping(UserControllerPath.USERS)
    ResponseEntity<User> updateUser(@RequestBody User user);

    @DeleteMapping(UserControllerPath.USERS + "/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable String id);

    class UserControllerPath {
        public static final String USERS = "/maintenance/users";
    }
}
