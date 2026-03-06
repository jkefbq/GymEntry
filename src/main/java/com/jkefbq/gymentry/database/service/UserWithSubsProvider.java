package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.UserDto;

import java.util.UUID;

public interface UserWithSubsProvider {
    UserDto findByEmail(String email);
    UserDto findByUserId(UUID userId);
}
