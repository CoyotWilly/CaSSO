package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.contracts.ICounterService;
import com.coyotwilly.casso.dtos.CounterDataDto;
import com.coyotwilly.casso.models.entities.DeviceSessionCounters;
import com.coyotwilly.casso.models.entities.User;
import com.coyotwilly.casso.services.RowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final RowService row;
    private final ICounterService<DeviceSessionCounters> counter;

    @GetMapping(UserControllerPath.USERS)
    public ResponseEntity<List<User>> test() {
        return ResponseEntity.ok().body(row.select());
    }

    @PostMapping(UserControllerPath.USERS)
    public ResponseEntity<User> post() {
        counter.create(10L, DeviceSessionCounters.class);
        return ResponseEntity.ok().build();
    }

    @PutMapping(UserControllerPath.USERS)
    public ResponseEntity<CounterDataDto> put() {
        CounterDataDto c = counter.decrement("mac-address", 10L, DeviceSessionCounters.class);

        return ResponseEntity.ok().body(c);
    }

    @DeleteMapping(UserControllerPath.USERS)
    public ResponseEntity<Void> delete() {
        row.delete();
        return ResponseEntity.noContent().build();
    }

    public static class UserControllerPath {
        public static final String USERS = "/maintenance/users";
    }
}
