package com.jkefbq.gymentry.shop.database.service;

import com.jkefbq.gymentry.shop.database.dto.UserDto;
import com.jkefbq.gymentry.shop.database.entity.User;
import com.jkefbq.gymentry.shop.database.mapper.UserMapper;
import com.jkefbq.gymentry.shop.database.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepo;

    public UserDto create(UserDto dto) {
        User notSavedUser = userMapper.toEntity(dto);
        User savedUser = userRepo.save(notSavedUser);
        return userMapper.toDto(savedUser);
    }
}
