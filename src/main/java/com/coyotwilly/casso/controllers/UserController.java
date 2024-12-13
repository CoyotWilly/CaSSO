package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.contracts.IUserService;
import com.coyotwilly.casso.models.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping(UserControllerPath.USERS)
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping(UserControllerPath.USERS + "/{id}")
    public ResponseEntity<User> getUsers(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    @PostMapping(UserControllerPath.USERS)
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, UserControllerPath.USERS + "/" + newUser.getId())
                .body(newUser);
    }

    @PutMapping(UserControllerPath.USERS)
    public ResponseEntity<User> updateUserData(@RequestBody User user) {
        User updateUser = userService.updateUser(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, UserControllerPath.USERS + "/" + updateUser.getId())
                .body(updateUser);
    }

    @DeleteMapping(UserControllerPath.USERS + "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    public static class UserControllerPath {
        public static final String USERS = "/maintenance/users";
    }
}
