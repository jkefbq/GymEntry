package com.jkefbq.gymentry.database.repository;

import com.jkefbq.gymentry.database.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull UUID> {
    Optional<User> getUserByEmail(String email);
    boolean existsByEmail(String email);
}
