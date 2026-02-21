package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.entity.User;
import com.jkefbq.gymentry.database.mapper.UserMapper;
import com.jkefbq.gymentry.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto create(UserDto dto) {
        User notSavedUser = userMapper.toEntity(dto);
        notSavedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepo.save(notSavedUser);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public Optional<UserDto> findByEmail(String email) {
        return userRepo.getUserByEmail(email).map(userMapper::toDto);
    }

    @Override
    @Transactional
    public boolean isCorrectEmailAndPassword(String email, String passwordToCheck) {
        Optional<UserDto> user = findByEmail(email);
        return user.isPresent() && passwordEncoder.matches(passwordToCheck, user.get().getPassword());
    }

    @Override
    @Transactional
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
}
