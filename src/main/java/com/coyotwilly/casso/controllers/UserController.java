package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.contracts.controllers.IUserController;
import com.coyotwilly.casso.contracts.services.IUserService;
import com.coyotwilly.casso.models.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController implements IUserController {
    private final IUserService userService;

    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    public ResponseEntity<User> getUser(String id) {
        return ResponseEntity.ok().body(userService.getUser(id));
    }

    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, UserControllerPath.USERS + "/" + newUser.getLogin())
                .body(newUser);
    }

    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);

        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, UserControllerPath.USERS + "/" + updateUser.getLogin())
                .body(updateUser);
    }

    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
