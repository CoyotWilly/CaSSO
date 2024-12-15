package com.coyotwilly.casso.mappers;

import com.coyotwilly.casso.contracts.mappers.ISessionLocksDataMapper;
import com.coyotwilly.casso.dtos.SessionLockDataDto;
import com.coyotwilly.casso.models.abstracts.SessionLocksData;
import com.coyotwilly.casso.models.entities.DeviceSessionLocksData;
import com.coyotwilly.casso.models.entities.UserSessionLocksData;
import org.springframework.stereotype.Service;

@Service
public class SessionLocksDataMapper implements ISessionLocksDataMapper {
    @Override
    public DeviceSessionLocksData toDeviceSessionLocksData(SessionLockDataDto dto) {
        return new DeviceSessionLocksData(dto.id(), dto.expirationTime());
    }

    @Override
    public UserSessionLocksData toUserSessionLocksData(SessionLockDataDto dto) {
        return new UserSessionLocksData(dto.id(), dto.expirationTime());
    }

    @Override
    public SessionLockDataDto toSessionLockDataDto(SessionLocksData data) {
        return new SessionLockDataDto(data.getEntityId(), data.getLockExpirationTime());
    }
}
