package com.jkefbq.gymentry.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.TariffDto;
import com.jkefbq.gymentry.database.service.TariffService;
import com.jkefbq.gymentry.database.dto.TariffType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class SubscriptionPriceCalculator {

    private final TariffService tariffService;

    public BigDecimal calculate(SubscriptionDto subscriptionDto) {
        TariffDto tariffDto = tariffService.getByType(subscriptionDto.getTariffType())
                .orElseThrow(() -> new NoSuchElementException("tariff with type " + subscriptionDto.getTariffType() + " not found"));
        return tariffDto.getPricePerLesson().multiply(
                BigDecimal.valueOf(subscriptionDto.getVisitsTotal())
        );
    }

    public BigDecimal calculate(TariffType tariffType, Integer visitsCount) {
        TariffDto tariffDto = tariffService.getByType(tariffType)
                .orElseThrow(() -> new NoSuchElementException("tariff with type " + tariffType + " not found"));
        return tariffDto.getPricePerLesson().multiply(BigDecimal.valueOf(visitsCount));
    }
}
