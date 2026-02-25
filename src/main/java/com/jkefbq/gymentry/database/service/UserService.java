package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.UserDto;

import java.util.Optional;

public interface UserService {
    UserDto create(UserDto dto);
    Optional<UserDto> findByEmail(String email);
    boolean isCorrectEmailAndPassword(String email, String passwordToCheck);
    boolean existsByEmail(String email);
    UserDto update(UserDto user);
}
