package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.models.User;
import com.coyotwilly.casso.services.RowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final RowService row;

    @GetMapping(UserControllerPath.USERS)
    public ResponseEntity<List<User>> test() {
        return ResponseEntity.ok().body(row.select());
    }

    @PostMapping(UserControllerPath.USERS)
    public ResponseEntity<User> post() {
        return ResponseEntity.ok().body(row.insert());
    }

    @PutMapping(UserControllerPath.USERS)
    public ResponseEntity<User> put() {
        return ResponseEntity.ok().body(row.update());
    }

    @DeleteMapping(UserControllerPath.USERS)
    public ResponseEntity<Void> delete() {
        row.delete();
        return ResponseEntity.noContent().build();
    }

    public static class UserControllerPath {
        public static final String USERS = "/users";
    }
}
