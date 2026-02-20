package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.NotVerifiedUserDto;

import java.util.Optional;

public interface NotVerifiedUserService {
    NotVerifiedUserDto create(NotVerifiedUserDto dto);
    boolean existsByEmail(String email);
    Optional<NotVerifiedUserDto> findUserByEmail(String email);
    void deleteByEmail(String email);
}
