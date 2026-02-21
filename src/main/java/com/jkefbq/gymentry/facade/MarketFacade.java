package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.SubscriptionRequestDto;
import com.jkefbq.gymentry.database.dto.SubscriptionResponseDto;
import com.jkefbq.gymentry.shop.dto.TariffType;

import java.math.BigDecimal;

public interface MarketFacade {
    SubscriptionResponseDto create(SubscriptionRequestDto requestDto, String email);
    BigDecimal calculatePrice(TariffType tariffType, Integer visitsCount);
}
