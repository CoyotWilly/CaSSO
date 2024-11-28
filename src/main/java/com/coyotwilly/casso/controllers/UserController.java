package com.coyotwilly.casso.controllers;

import lombok.RequiredArgsConstructor;
import com.coyotwilly.casso.services.RowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final RowService row;

    @GetMapping
    public ResponseEntity<?> test() {
        return ResponseEntity.ok().build();
    }

    @PostMapping(UserControllerPath.USERS)
    public ResponseEntity<?> post() {
        row.insert();

        return ResponseEntity.ok().build();
    }

    @PutMapping(UserControllerPath.USERS)
    public ResponseEntity<?> put() {
        row.update();

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(UserControllerPath.USERS)
    public ResponseEntity<?> delete() {
        row.delete();
        return ResponseEntity.noContent().build();
    }

    public static class UserControllerPath {
        public static final String USERS = "/users";
    }
}
