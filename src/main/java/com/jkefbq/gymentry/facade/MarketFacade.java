package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.dto.SubscriptionRequestDto;
import com.jkefbq.gymentry.dto.SubscriptionResponseDto;
import com.jkefbq.gymentry.database.dto.TariffType;

import java.math.BigDecimal;

public interface MarketFacade {
    SubscriptionResponseDto create(SubscriptionRequestDto requestDto, String email);
    BigDecimal calculatePrice(TariffType tariffType, Integer visitsCount);
}
