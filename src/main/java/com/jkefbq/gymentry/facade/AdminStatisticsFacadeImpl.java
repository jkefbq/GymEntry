package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.dto.PurchaseStatistics;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.dto.VisitStatistics;
import com.jkefbq.gymentry.database.service.SubscriptionService;
import com.jkefbq.gymentry.database.service.UserService;
import com.jkefbq.gymentry.database.service.VisitService;
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

    private final VisitService visitService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @Transactional
    @Override
    public VisitStatistics getVisitStatisticsForPeriod(LocalDateTime from, LocalDateTime to, String gymAddress) {
        List<VisitDto> visitsForPeriod = visitService.getAllForPeriod(from, to, gymAddress);
        Long wholeDays = ChronoUnit.DAYS.between(from, to);
        return VisitStatistics.builder()
                .from(from)
                .to(to)
                .gymAddress(gymAddress)
                .totalVisits(visitsForPeriod.size())
                .avgPerDay(visitService.getAvgPerDay(visitsForPeriod.size(), wholeDays))
                .peakVisitsDay(visitService.getPeakDay(visitsForPeriod))
                .byDay(visitService.getVisitsPerDate(visitsForPeriod))
                .byTariffType(visitService.getTariffsPerDate(visitsForPeriod))
                .build();
    }

    @Transactional
    @Override
    public PurchaseStatistics getPurchaseStatisticsForPeriod(LocalDate from, LocalDate to) {
        List<SubscriptionDto> subscriptionsForPeriod = subscriptionService.getAllForPeriod(from, to);
        Long wholeDays = ChronoUnit.DAYS.between(from, to);
        return PurchaseStatistics.builder()
                .from(from)
                .to(to)
                .totalPurchases(subscriptionsForPeriod.size())
                .totalRevenue(subscriptionService.getTotalRevenue(subscriptionsForPeriod))
                .avgDayCheck(subscriptionService.getAvgDayCheck(subscriptionsForPeriod, wholeDays))
                .avgPersonCheck(subscriptionService.getAvgPerPersonCheck(subscriptionsForPeriod))
                .peakPurchasesDay(subscriptionService.getPeakDay(subscriptionsForPeriod))
                .byDay(subscriptionService.getPurchasesPerDate(subscriptionsForPeriod))
                .byTariffType(subscriptionService.getPurchasesPerTariff(subscriptionsForPeriod))
                .build();
    }
}
