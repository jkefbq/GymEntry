package com.jkefbq.gymentry.database.repository;

import com.jkefbq.gymentry.database.entity.NotVerifiedUser;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotVerifiedUserRepository extends JpaRepository<@NonNull NotVerifiedUser, @NonNull UUID> {
    boolean existsByEmail(String email);
    Optional<NotVerifiedUser> getByEmail(String email);
    void deleteByEmail(String email);
}
