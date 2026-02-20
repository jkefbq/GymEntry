package com.jkefbq.gymentry.shop.dto;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.entity.User;

import java.math.BigDecimal;

public interface SubscriptionPricingStrategy {
    BigDecimal calculatePrice(SubscriptionDto subscription, User user);
    TariffType getTariffType();
}
