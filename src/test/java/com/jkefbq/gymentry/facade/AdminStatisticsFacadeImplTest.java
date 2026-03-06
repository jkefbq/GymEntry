package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.TariffType;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.database.service.SubscriptionAnalytics;
import com.jkefbq.gymentry.database.service.SubscriptionManager;
import com.jkefbq.gymentry.database.service.VisitAnalytics;
import com.jkefbq.gymentry.database.service.VisitManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminStatisticsFacadeImplTest {

    private static final String MOCK_ADDRESS = "address";

    @Mock
    VisitManager visitManager;
    @Mock
    VisitAnalytics visitAnalytics;
    @Mock
    SubscriptionAnalytics subscriptionAnalytics;
    @Mock
    SubscriptionManager subscriptionManager;

    @InjectMocks
    AdminStatisticsFacadeImpl adminStat;

    public List<VisitDto> getVisits() {
        var visit = VisitDto.builder().createdAt(LocalDateTime.now()).id(UUID.randomUUID()).build();
        return List.of(visit, visit, visit, visit, visit, visit);
    }

    public List<SubscriptionDto> getSubscriptions() {
        var sub = SubscriptionDto.builder().active(false).visitsLeft(4).purchaseAt(LocalDate.now())
                .tariffType(TariffType.BASIC).snapshotPrice(BigDecimal.TEN).visitsTotal(10).build();
        return List.of(sub, sub, sub, sub, sub, sub, sub, sub);
    }

    @Test
    public void getVisitStatisticsForPeriodTest() {
        var from = LocalDateTime.now();
        var to = LocalDateTime.now();
        var wholeDays = ChronoUnit.DAYS.between(from, to);
        var visits = getVisits();
        when(visitManager.getAllForPeriod(from, to, MOCK_ADDRESS)).thenReturn(visits);

        adminStat.getVisitStatisticsForPeriod(from, to, MOCK_ADDRESS);

        verify(visitAnalytics).getAvgPerDay(visits.size(), wholeDays);
        verify(visitAnalytics).getPeakDay(visits);
        verify(visitAnalytics).getVisitsPerDate(visits);
        verify(visitAnalytics).getTariffsPerDate(visits);
    }

    @Test
    public void getPurchaseStatisticsForPeriodTest() {
        var from = LocalDate.now();
        var to = LocalDate.now();
        var wholeDays = ChronoUnit.DAYS.between(from, to);
        var subscriptions = getSubscriptions();
        when(subscriptionManager.getAllForPeriod(from, to)).thenReturn(subscriptions);

        adminStat.getPurchaseStatisticsForPeriod(from, to);

        verify(subscriptionAnalytics).getTotalRevenue(subscriptions);
        verify(subscriptionAnalytics).getAvgDayCheck(subscriptions, wholeDays);
        verify(subscriptionAnalytics).getAvgPerPurchaseCheck(subscriptions);
        verify(subscriptionAnalytics).getPeakDay(subscriptions);
        verify(subscriptionAnalytics).getPurchasesPerDate(subscriptions);
        verify(subscriptionAnalytics).getPurchasesPerTariff(subscriptions);
    }
}
