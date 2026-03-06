package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.database.service.SubscriptionAnalytics;
import com.jkefbq.gymentry.database.service.SubscriptionManager;
import com.jkefbq.gymentry.database.service.VisitAnalytics;
import com.jkefbq.gymentry.database.service.VisitManager;
import com.jkefbq.gymentry.dto.PurchaseStatistics;
import com.jkefbq.gymentry.dto.VisitStatistics;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStatisticsFacadeImpl implements AdminStatisticsFacade {

    private final VisitManager visitManager;
    private final VisitAnalytics visitAnalytics;
    private final SubscriptionAnalytics subscriptionAnalytics;
    private final SubscriptionManager subscriptionManager;

    @Transactional
    @Override
    public VisitStatistics getVisitStatisticsForPeriod(LocalDateTime from, LocalDateTime to, String gymAddress) {
        List<VisitDto> visitsForPeriod = visitManager.getAllForPeriod(from, to, gymAddress);
        Long wholeDays = ChronoUnit.DAYS.between(from, to);
        return VisitStatistics.builder()
                .from(from)
                .to(to)
                .gymAddress(gymAddress)
                .totalVisits(visitsForPeriod.size())
                .avgPerDay(visitAnalytics.getAvgPerDay(visitsForPeriod.size(), wholeDays))
                .peakVisitsDay(visitAnalytics.getPeakDay(visitsForPeriod))
                .byDay(visitAnalytics.getVisitsPerDate(visitsForPeriod))
                .byTariffType(visitAnalytics.getTariffsPerDate(visitsForPeriod))
                .build();
    }

    @Transactional
    @Override
    public PurchaseStatistics getPurchaseStatisticsForPeriod(LocalDate from, LocalDate to) {
        List<SubscriptionDto> subscriptionsForPeriod = subscriptionManager.getAllForPeriod(from, to);
        Long wholeDays = ChronoUnit.DAYS.between(from, to);
        return PurchaseStatistics.builder()
                .from(from)
                .to(to)
                .totalPurchases(subscriptionsForPeriod.size())
                .totalRevenue(subscriptionAnalytics.getTotalRevenue(subscriptionsForPeriod))
                .avgDayCheck(subscriptionAnalytics.getAvgDayCheck(subscriptionsForPeriod, wholeDays))
                .avgPurchaseCheck(subscriptionAnalytics.getAvgPerPurchaseCheck(subscriptionsForPeriod))
                .peakPurchasesDay(subscriptionAnalytics.getPeakDay(subscriptionsForPeriod))
                .byDay(subscriptionAnalytics.getPurchasesPerDate(subscriptionsForPeriod))
                .byTariffType(subscriptionAnalytics.getPurchasesPerTariff(subscriptionsForPeriod))
                .build();
    }
}
