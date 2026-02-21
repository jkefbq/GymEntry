package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;

public interface SubscriptionService {
    SubscriptionDto create(SubscriptionDto dto);
    SubscriptionDto update(SubscriptionDto dto);
}
