package com.jkefbq.gymentry.database.repository;

import com.jkefbq.gymentry.database.entity.Subscription;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<@NonNull Subscription, @NonNull UUID> {}
