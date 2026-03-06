package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.PartialUserDto;
import com.jkefbq.gymentry.database.dto.UserWithPassword;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    PartialUserDto create(UserWithPassword dto);
    Optional<PartialUserDto> findByEmail(String email);
    Optional<UserWithPassword> findWithPasswordByEmail(String email);
    boolean isCorrectEmailAndPassword(String email, String passwordToCheck);
    boolean existsByEmail(String email);
    PartialUserDto update(PartialUserDto user);
    Optional<PartialUserDto> findById(UUID userId);
    Optional<UUID> getUserIdByEmail(String email);
}
