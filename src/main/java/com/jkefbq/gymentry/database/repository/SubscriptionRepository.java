package com.jkefbq.gymentry.database.repository;

import com.jkefbq.gymentry.database.entity.Subscription;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<@NonNull Subscription, @NonNull UUID> {
}
