package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.dto.PeakPurchasesDay;
import com.jkefbq.gymentry.dto.PurchasePerDate;
import com.jkefbq.gymentry.dto.PurchaseTariffTypePerDate;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.entity.Subscription;
import com.jkefbq.gymentry.database.mapper.SubscriptionMapper;
import com.jkefbq.gymentry.database.repository.SubscriptionRepository;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .filter(SubscriptionDto::getActive)
                .findFirst()
                .orElseThrow(() -> new NonActiveSubscriptionException("you do not have any active subscriptions"));
        if (subscriptionDto.getVisitsLeft() <= 0) {
            throw new VisitsAreOverException("you have used up all your visits in your subscription");
        }
        return subscriptionDto;
    }

    @Override
    @Transactional
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

    @Override
    @Transactional
    public List<SubscriptionDto> getAllForPeriod(LocalDate from, LocalDate to) {
        return subscriptionRepository.getAllForPeriod(from, to).stream().map(subscriptionMapper::toDto).toList();
    }

    @Override
    public BigDecimal getAvgDayCheck(List<SubscriptionDto> subscriptionsForPeriod, Long wholeDays) {
        return subscriptionsForPeriod.stream()
                .map(SubscriptionDto::getSnapshotPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(wholeDays), RoundingMode.HALF_UP);
    }

    @Override
    public PeakPurchasesDay getPeakDay(List<SubscriptionDto> subscriptionsForPeriod) {
        List<PurchasePerDate> purchasePerDates = getPurchasesPerDate(subscriptionsForPeriod);
        return purchasePerDates.stream()
                .max(Comparator.comparing(PurchasePerDate::getPurchaseSum))
                .map(PeakPurchasesDay::new)
                .orElseThrow(() -> new IllegalStateException("array of purchases is empty"));
    }

    @Override
    public List<PurchasePerDate> getPurchasesPerDate(List<SubscriptionDto> subscriptionsForPeriod) {
        return subscriptionsForPeriod.stream()
                .collect(Collectors.toMap(
                        SubscriptionDto::getPurchaseAt,
                        sub -> new PurchasePerDate(sub.getPurchaseAt(), 1L, sub.getSnapshotPrice()),
                        (purchase1,purchase2) -> {
                            purchase1.setPurchaseCount(purchase1.getPurchaseCount() + 1L);
                            purchase1.setPurchaseSum(purchase1.getPurchaseSum().add(purchase2.getPurchaseSum()));
                            return purchase1;
                        }
                )).values().stream()
                .sorted(Comparator.comparing(PurchasePerDate::getDate))
                .toList();
    }

    @Override
    public List<PurchaseTariffTypePerDate> getPurchasesPerTariff(List<SubscriptionDto> subscriptionsForPeriod) {
        return subscriptionsForPeriod.stream()
                .collect(Collectors.toMap(
                        SubscriptionDto::getTariffType,
                        sub -> new PurchaseTariffTypePerDate(sub.getTariffType(), 1L, sub.getSnapshotPrice()),
                        (purchase1,purchase2) -> {
                            purchase1.setPurchaseCount(purchase1.getPurchaseCount() + 1L);
                            purchase1.setPurchaseSum(purchase1.getPurchaseSum().add(purchase2.getPurchaseSum()));
                            return purchase1;
                        }
                )).values().stream().toList();
    }

    @Override
    public BigDecimal getTotalRevenue(List<SubscriptionDto> subscriptionsForPeriod) {
        return subscriptionsForPeriod.stream()
                .map(SubscriptionDto::getSnapshotPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getAvgPerPersonCheck(List<SubscriptionDto> subscriptionsForPeriod) {
        return subscriptionsForPeriod.stream()
                .map(SubscriptionDto::getSnapshotPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(subscriptionsForPeriod.size()), RoundingMode.HALF_UP);
    }

    protected boolean hasActiveSubscriptions(List<SubscriptionDto> subscriptions) {
        return subscriptions.stream().anyMatch(SubscriptionDto::getActive);
    }

    private void refreshSubscriptionData(SubscriptionDto subscriptionDto) {
        if (subscriptionDto.getVisitsLeft() <= 0) {
            subscriptionDto.setActive(false);
        }
    }
}
