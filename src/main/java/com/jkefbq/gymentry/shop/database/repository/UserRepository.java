package com.jkefbq.gymentry.shop.database.repository;

import com.jkefbq.gymentry.shop.database.entity.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<@NonNull User, @NonNull UUID> {}
