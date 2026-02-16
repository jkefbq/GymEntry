package com.jkefbq.gymentry.shop.service;

import com.jkefbq.gymentry.shop.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.shop.database.entity.User;
import com.jkefbq.gymentry.shop.dto.SubscriptionPricingStrategy;
import com.jkefbq.gymentry.shop.dto.TariffType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BasicTariffPricingStrategy implements SubscriptionPricingStrategy {

    @Getter
    private final TariffType tariffType = TariffType.BASIC;
    @Value("${app.tariff.basic.price-per-visit-rub}")
    private BigDecimal pricePerVisit;

    @Override
    public BigDecimal calculatePrice(SubscriptionDto dto, User user) {
        return pricePerVisit.multiply(
                BigDecimal.valueOf(dto.getVisitsTotal())
        );
    }
}
