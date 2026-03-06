package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionManager {
    Optional<SubscriptionDto> findById(UUID id);
    SubscriptionDto create(SubscriptionDto dto);
    SubscriptionDto update(SubscriptionDto dto);
    SubscriptionDto activateSubscription(UUID subscriptionId);
    List<SubscriptionDto> getAllForPeriod(LocalDate from, LocalDate to);
    SubscriptionDto deactivateSubscription(UUID subscriptionId);
    List<SubscriptionDto> getUserSubs(UUID userId);
    SubscriptionDto getActiveSubscription(UUID userId);
    void checkActiveSub(SubscriptionDto sub);
}
