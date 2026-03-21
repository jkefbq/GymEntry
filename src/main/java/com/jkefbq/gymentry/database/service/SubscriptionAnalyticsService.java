package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.dto.PeakPurchasesDay;
import com.jkefbq.gymentry.dto.PurchasePerDate;
import com.jkefbq.gymentry.dto.PurchaseTariffTypePerDate;

import java.math.BigDecimal;
import java.util.List;

public interface SubscriptionAnalyticsService {
    BigDecimal getAvgDayCheck(List<SubscriptionDto> subscriptionsForPeriod, Long wholeDays);
    PeakPurchasesDay getPeakDay(List<SubscriptionDto> subscriptionsForPeriod);
    List<PurchasePerDate> getPurchasesPerDate(List<SubscriptionDto> subscriptionsForPeriod);
    List<PurchaseTariffTypePerDate> getPurchasesPerTariff(List<SubscriptionDto> subscriptionsForPeriod);
    BigDecimal getTotalRevenue(List<SubscriptionDto> subscriptionsForPeriod);
    BigDecimal getAvgPerPurchaseCheck(List<SubscriptionDto> subscriptionsForPeriod);
}
