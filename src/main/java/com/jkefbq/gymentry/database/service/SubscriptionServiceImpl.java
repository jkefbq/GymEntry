package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.entity.Subscription;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapper;
import com.jkefbq.gymentry.database.repository.SubscriptionRepository;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;

    @Override
    @Transactional
    public SubscriptionDto create(SubscriptionDto dto) {
        refreshSubscriptionData(dto);
        Subscription notSavedEntity = subscriptionMapper.toEntity(dto);
        Subscription savedEntity = subscriptionRepository.save(notSavedEntity);
        return subscriptionMapper.toDto(savedEntity);
    }

    @Override
    @Transactional
    public SubscriptionDto update(SubscriptionDto dto) {
        refreshSubscriptionData(dto);
        Subscription notSavedEntity = subscriptionMapper.toEntity(dto);
        Subscription savedEntity = subscriptionRepository.save(notSavedEntity);
        return subscriptionMapper.toDto(savedEntity);
    }

    @Transactional
    @Override
    public SubscriptionDto validateAndGetActiveSubscription(String email) throws VisitsAreOverException, NonActiveSubscriptionException {
        SubscriptionDto subscriptionDto = userService.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("user with email " + email + " not found"))
                .getSubscriptions().stream()
                .filter(SubscriptionDto::isActive)
                .findFirst()
                .orElseThrow(() -> new NonActiveSubscriptionException("you do not have any active subscriptions"));
        if (subscriptionDto.getVisitsLeft() <= 0) {
            throw new VisitsAreOverException("you have used up all your visits in your subscription");
        }
        return subscriptionDto;
    }

    @Override
    public List<SubscriptionDto> getAllSubscriptions(String email) {
        return userService.findByEmail(email).orElseThrow().getSubscriptions();
    }

    @Override
    @Transactional
    public SubscriptionDto activateSubscription(String email, UUID subscriptionId) {
        List<SubscriptionDto> subscriptions = userService.findByEmail(email).orElseThrow().getSubscriptions();
        if (!hasActiveSubscriptions(subscriptions)) {
            SubscriptionDto newActiveSubscription = subscriptions.stream()
                    .filter(s -> s.getId().equals(subscriptionId))
                    .findFirst()
                    .orElseThrow();
            newActiveSubscription.setActive(true);
            return update(newActiveSubscription);
        }
        throw new IllegalArgumentException("user already have active subscription");
    }

    protected boolean hasActiveSubscriptions(List<SubscriptionDto> subscriptions) {
        return subscriptions.stream().anyMatch(SubscriptionDto::isActive);
    }

    private void refreshSubscriptionData(SubscriptionDto subscriptionDto) {
        if (subscriptionDto.getVisitsLeft() <= 0) {
            subscriptionDto.setActive(false);
        }
    }
}
