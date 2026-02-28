package com.jkefbq.gymentry.database.repository;

import com.jkefbq.gymentry.database.entity.Subscription;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<@NonNull Subscription, @NonNull UUID> {

    @Query("SELECT s FROM Subscription s WHERE s.purchaseAt >= :from AND s.purchaseAt <= :to")
    List<Subscription> getAllForPeriod(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
