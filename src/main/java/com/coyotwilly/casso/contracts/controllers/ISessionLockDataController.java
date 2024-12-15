package com.coyotwilly.casso.contracts.controllers;

import com.coyotwilly.casso.dtos.SessionLockDataDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface ISessionLockDataController {
    @GetMapping(SessionLockDataControllerPath.SESSION_LOCK_DATA + "/{id}")
    ResponseEntity<SessionLockDataDto> getSessionLockDataById(@PathVariable String id, @RequestParam String lockType);

    @PostMapping(SessionLockDataControllerPath.SESSION_LOCK_DATA)
    ResponseEntity<SessionLockDataDto> createSessionLockData(@RequestBody SessionLockDataDto sessionLockDataDto,
                                                             @RequestParam String lockType);

    @PutMapping(SessionLockDataControllerPath.SESSION_LOCK_DATA)
    ResponseEntity<SessionLockDataDto> updateSessionLockData(@RequestBody SessionLockDataDto sessionLockDataDto,
                                                             @RequestParam String lockType);

    @DeleteMapping(SessionLockDataControllerPath.SESSION_LOCK_DATA + "/{id}")
    ResponseEntity<Void> deleteSessionLockData(@PathVariable String id, @RequestParam String lockType);

    class SessionLockDataControllerPath {
        public static final String SESSION_LOCK_DATA = "/maintenance/session-locks-data";
    }
}
