package com.coyotwilly.casso.controllers;

import com.coyotwilly.casso.contracts.controllers.ISessionLockDataController;
import com.coyotwilly.casso.contracts.mappers.ISessionLocksDataMapper;
import com.coyotwilly.casso.contracts.services.ISessionLockDataService;
import com.coyotwilly.casso.dtos.SessionLockDataDto;
import com.coyotwilly.casso.enums.LockType;
import com.coyotwilly.casso.models.abstracts.SessionLocksData;
import com.coyotwilly.casso.models.entities.DeviceSessionLocksData;
import com.coyotwilly.casso.models.entities.UserSessionLocksData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SessionLockDataController implements ISessionLockDataController {
    private final ISessionLockDataService sessionLocksDataService;
    private final ISessionLocksDataMapper mapper;

    @Override
    public ResponseEntity<SessionLockDataDto> getSessionLockDataById(String id, String lockType) {
        SessionLocksData response = switch (LockType.fromString(lockType)) {
            case DEVICE -> sessionLocksDataService.getSessionLockDataById(id, DeviceSessionLocksData.class);
            case USER -> sessionLocksDataService.getSessionLockDataById(id, UserSessionLocksData.class);
        };

        return ResponseEntity.ok(mapper.toSessionLockDataDto(response));
    }

    @Override
    public ResponseEntity<SessionLockDataDto> createSessionLockData(SessionLockDataDto sessionLockDataDto,
                                                                    String lockType) {
        SessionLocksData session = switch (LockType.fromString(lockType)) {
            case DEVICE -> sessionLocksDataService.createSessionLockData(
                    mapper.toDeviceSessionLocksData(sessionLockDataDto), DeviceSessionLocksData.class);
            case USER -> sessionLocksDataService.createSessionLockData(
                    mapper.toUserSessionLocksData(sessionLockDataDto), UserSessionLocksData.class);
        };
        SessionLockDataDto response = mapper.toSessionLockDataDto(session);
        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, SessionLockDataControllerPath.SESSION_LOCK_DATA + "/" + response.id())
                .body(response);
    }

    @Override
    public ResponseEntity<SessionLockDataDto> updateSessionLockData(SessionLockDataDto sessionLockDataDto,
                                                                    String lockType) {
        SessionLocksData session = switch (LockType.fromString(lockType)) {
            case DEVICE -> sessionLocksDataService.updateSessionLockData(
                    mapper.toDeviceSessionLocksData(sessionLockDataDto), DeviceSessionLocksData.class);
            case USER -> sessionLocksDataService.updateSessionLockData(
                    mapper.toUserSessionLocksData(sessionLockDataDto), UserSessionLocksData.class);
        };

        SessionLockDataDto response = mapper.toSessionLockDataDto(session);
        return ResponseEntity.ok()
                .header(HttpHeaders.LOCATION, SessionLockDataControllerPath.SESSION_LOCK_DATA + "/" + response.id())
                .body(response);
    }

    @Override
    public ResponseEntity<Void> deleteSessionLockData(String id, String lockType) {
        switch (LockType.fromString(lockType)) {
            case DEVICE -> sessionLocksDataService.deleteSessionLockDataById(id, DeviceSessionLocksData.class);
            case USER -> sessionLocksDataService.deleteSessionLockDataById(id, UserSessionLocksData.class);
        }

        return ResponseEntity.noContent().build();
    }
}
