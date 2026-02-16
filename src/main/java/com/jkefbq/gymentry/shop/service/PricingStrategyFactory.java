package com.jkefbq.gymentry.shop.service;

import com.jkefbq.gymentry.shop.dto.SubscriptionPricingStrategy;
import com.jkefbq.gymentry.shop.dto.TariffType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@AllArgsConstructor
public class PricingStrategyFactory {

    private final List<SubscriptionPricingStrategy> strategies;

    public SubscriptionPricingStrategy getStrategy(TariffType tariffType) {
        return strategies.stream()
                .filter(strategy -> strategy.getTariffType() == tariffType)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("no suitable strategy found for " + tariffType));
    }
}
