package com.jkefbq.gymentry.shop.database.service;

import com.jkefbq.gymentry.shop.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.shop.database.entity.Subscription;
import com.jkefbq.gymentry.shop.database.mapper.SubscriptionMapper;
import com.jkefbq.gymentry.shop.database.repository.SubscriptionRepository;
import com.jkefbq.gymentry.shop.dto.TariffType;
import com.jkefbq.gymentry.shop.service.PricingStrategyFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subRepo;
    private final SubscriptionMapper subMapper;
    private final PricingStrategyFactory pricingStrategyFactory;

    public SubscriptionDto create(SubscriptionDto entity) {
        Subscription notSavedEntity = subMapper.toEntity(entity);
        Subscription savedEntity = subRepo.save(notSavedEntity);
        return subMapper.toDto(savedEntity);
    }

    public SubscriptionDto calculatePrice(SubscriptionDto dto) {
        TariffType tariff = dto.getTariffType();
        pricingStrategyFactory.getStrategy(tariff).calculatePrice(dto, );
    }
}
