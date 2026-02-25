package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    SubscriptionDto create(SubscriptionDto dto);
    SubscriptionDto update(SubscriptionDto dto);
    SubscriptionDto validateAndGetActiveSubscription(String email) throws VisitsAreOverException, NonActiveSubscriptionException;
    List<SubscriptionDto> getAllSubscriptions(String email);
    SubscriptionDto activateSubscription(String email, UUID subscriptionId);
}
