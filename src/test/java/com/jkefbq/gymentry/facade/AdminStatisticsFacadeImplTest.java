package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.TariffType;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.database.service.SubscriptionService;
import com.jkefbq.gymentry.database.service.VisitService;
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

    @Mock
    VisitService visitService;
    @Mock
    SubscriptionService subscriptionService;

    @InjectMocks
    AdminStatisticsFacadeImpl adminStat;

    private static final String MOCK_ADDRESS = "address";

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
        when(visitService.getAllForPeriod(from, to, MOCK_ADDRESS)).thenReturn(visits);

        adminStat.getVisitStatisticsForPeriod(from, to, MOCK_ADDRESS);

        verify(visitService).getAvgPerDay(visits.size(), wholeDays);
        verify(visitService).getPeakDay(visits);
        verify(visitService).getVisitsPerDate(visits);
        verify(visitService).getTariffsPerDate(visits);
    }

    @Test
    public void getPurchaseStatisticsForPeriodTest() {
        var from = LocalDate.now();
        var to = LocalDate.now();
        var wholeDays = ChronoUnit.DAYS.between(from, to);
        var subscriptions = getSubscriptions();
        when(subscriptionService.getAllForPeriod(from, to)).thenReturn(subscriptions);

        adminStat.getPurchaseStatisticsForPeriod(from, to);

        verify(subscriptionService).getTotalRevenue(subscriptions);
        verify(subscriptionService).getAvgDayCheck(subscriptions, wholeDays);
        verify(subscriptionService).getAvgPerPersonCheck(subscriptions);
        verify(subscriptionService).getPeakDay(subscriptions);
        verify(subscriptionService).getPurchasesPerDate(subscriptions);
        verify(subscriptionService).getPurchasesPerTariff(subscriptions);
    }
}
