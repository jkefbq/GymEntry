package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.PeakPurchasesDay;
import com.jkefbq.gymentry.database.dto.PurchasePerDate;
import com.jkefbq.gymentry.database.dto.PurchaseTariffTypePerDate;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.exception.NonActiveSubscriptionException;
import com.jkefbq.gymentry.exception.VisitsAreOverException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SubscriptionService {
    SubscriptionDto create(SubscriptionDto dto);
    SubscriptionDto update(SubscriptionDto dto);
    SubscriptionDto validateAndGetActiveSubscription(String email) throws VisitsAreOverException, NonActiveSubscriptionException;
    List<SubscriptionDto> getAllSubscriptions(String email);
    SubscriptionDto activateSubscription(String email, UUID subscriptionId);
    List<SubscriptionDto> getAllForPeriod(LocalDate from, LocalDate to);
    BigDecimal getAvgDayCheck(List<SubscriptionDto> subscriptionsForPeriod, Long wholeDays);
    PeakPurchasesDay getPeakDay(List<SubscriptionDto> subscriptionsForPeriod);
    List<PurchasePerDate> getPurchasesPerDate(List<SubscriptionDto> subscriptionsForPeriod);
    List<PurchaseTariffTypePerDate> getPurchasesPerTariff(List<SubscriptionDto> subscriptionsForPeriod);
    BigDecimal getTotalRevenue(List<SubscriptionDto> subscriptionsForPeriod);
    BigDecimal getAvgPerPersonCheck(List<SubscriptionDto> subscriptionsForPeriod);
}
