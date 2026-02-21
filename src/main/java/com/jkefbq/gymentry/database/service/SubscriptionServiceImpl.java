package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.entity.Subscription;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapper;
import com.jkefbq.gymentry.database.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public SubscriptionDto create(SubscriptionDto dto) {
        Subscription notSavedEntity = subscriptionMapper.toEntity(dto);
        Subscription savedEntity = subscriptionRepository.save(notSavedEntity);
        return subscriptionMapper.toDto(savedEntity);
    }

    @Override
    @Transactional
    public SubscriptionDto update(SubscriptionDto dto) {
        Subscription notSavedEntity = subscriptionMapper.toEntity(dto);
        Subscription savedEntity = subscriptionRepository.save(notSavedEntity);
        return subscriptionMapper.toDto(savedEntity);
    }
}
