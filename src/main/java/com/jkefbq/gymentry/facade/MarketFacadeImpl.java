package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.service.SubscriptionPriceCalculator;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.SubscriptionRequestDto;
import com.jkefbq.gymentry.database.dto.SubscriptionResponseDto;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapper;
import com.jkefbq.gymentry.database.service.SubscriptionService;
import com.jkefbq.gymentry.database.service.UserService;
import com.jkefbq.gymentry.database.dto.TariffType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarketFacadeImpl implements MarketFacade {

    private final SubscriptionPriceCalculator subscriptionPriceCalculator;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public SubscriptionResponseDto create(SubscriptionRequestDto requestDto, String email) {
        UUID subscriptionOwnerId = userService.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("user with email " + email + " not found"))
                .getId();
        SubscriptionDto completedDto = mapToSubscriptionDto(requestDto, subscriptionOwnerId);
        SubscriptionDto savedDto = subscriptionService.create(completedDto);
        return subscriptionMapper.toResponseDto(savedDto);
    }

    @Override
    @Transactional
    public BigDecimal calculatePrice(TariffType tariffType, Integer visitsCount) {
        return subscriptionPriceCalculator.calculate(tariffType, visitsCount);
    }

    @Transactional
    protected SubscriptionDto mapToSubscriptionDto(SubscriptionRequestDto requestDto, UUID subscriptionOwnerId) {
        SubscriptionDto dto = subscriptionMapper.toDto(requestDto);
        dto.setPurchaseAt(LocalDate.now());
        dto.setVisitsLeft(dto.getVisitsTotal());
        dto.setActive(false);
        dto.setSnapshotPrice(subscriptionPriceCalculator.calculate(dto));
        dto.setUserId(subscriptionOwnerId);
        return dto;
    }
}
