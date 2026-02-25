package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.entity.User;
import com.jkefbq.gymentry.database.mapper.UserMapper;
import com.jkefbq.gymentry.database.repository.UserRepository;
import com.jkefbq.gymentry.security.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        notSavedUser.setMemberSince(LocalDate.now());
        notSavedUser.setRole(UserRole.USER);
        notSavedUser.setTotalVisits(0);
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

    @Override
    public UserDto update(UserDto dto) {
        User notSavedEntity = userMapper.toEntity(dto);
        User savedEntity = userRepo.save(notSavedEntity);
        return userMapper.toDto(savedEntity);
    }
}
