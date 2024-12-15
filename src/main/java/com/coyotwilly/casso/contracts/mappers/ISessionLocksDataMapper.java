package com.coyotwilly.casso.contracts.mappers;

import com.coyotwilly.casso.dtos.SessionLockDataDto;
import com.coyotwilly.casso.models.abstracts.SessionLocksData;
import com.coyotwilly.casso.models.entities.DeviceSessionLocksData;
import com.coyotwilly.casso.models.entities.UserSessionLocksData;

public interface ISessionLocksDataMapper {
    DeviceSessionLocksData toDeviceSessionLocksData(SessionLockDataDto dto);
    UserSessionLocksData toUserSessionLocksData(SessionLockDataDto dto);
    SessionLockDataDto toSessionLockDataDto(SessionLocksData data);
}
