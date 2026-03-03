package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.GymInfoDto;
import com.jkefbq.gymentry.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.database.dto.TariffType;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.database.mapper.VisitMapper;
import com.jkefbq.gymentry.database.repository.VisitRepository;
import com.jkefbq.gymentry.dto.PeakVisitsDay;
import com.jkefbq.gymentry.dto.VisitPerDate;
import com.jkefbq.gymentry.dto.VisitTariffPerDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VisitServiceImplTest {

    @Mock
    VisitRepository visitRepository;
    @Mock
    VisitMapper visitMapper;

    @InjectMocks
    VisitServiceImpl visitService;

    public VisitDto getVisitDto() {
        return VisitDto.builder().id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .subscription(new SubscriptionDto())
                .gym(new GymInfoDto())
                .build();
    }

    @Test
    public void createTest() {
        visitService.create(new VisitDto());
        verify(visitRepository).save(any());
    }

    @Test
    public void getAllTest() {
        visitService.getAll();
        verify(visitRepository).findAll();
    }

    @Test
    public void getAllForPeriod() {
        var from = LocalDateTime.now();
        var to = LocalDateTime.now();
        var address = "address";

        visitService.getAllForPeriod(from, to, address);

        verify(visitRepository).getAllForPeriod(from, to, address);
    }

    @Test
    public void getAvgPerDayTest() {
        var visitCount = 500;
        var wholeDays = 25L;
        BigDecimal trueResult = BigDecimal.valueOf(visitCount / wholeDays);

        BigDecimal res = visitService.getAvgPerDay(visitCount, wholeDays);

        assertEquals(trueResult, res);
    }

    @Test
    public void getVisitsPerDateTest() {
        var v1 = VisitDto.builder().createdAt(LocalDateTime.now()).build();
        var v2 = VisitDto.builder().createdAt(LocalDateTime.now()).build();
        var v3 = VisitDto.builder().createdAt(LocalDateTime.now().plusDays(1)).build();
        var v4 = VisitDto.builder().createdAt(LocalDateTime.now().plusDays(2)).build();
        var v5 = VisitDto.builder().createdAt(LocalDateTime.now().plusDays(2)).build();
        List<VisitDto> visits = List.of(v1, v2, v3, v4, v5);

        List<VisitPerDate> resultList = visitService.getVisitsPerDate(visits);

        var mergedV1V2 = resultList.stream().filter(e -> e.getDate().equals(v1.getCreatedAt().toLocalDate())).findFirst().orElseThrow();
        assertEquals(3, resultList.size());
        assertEquals(2, mergedV1V2.getVisitCount());
    }

    @Test
    public void getPeakDayTest() {
        var target = VisitDto.builder().createdAt(LocalDateTime.now().plusDays(1)).build();
        var regular = getVisitDto();
        var visits = List.of(regular, target, target, target);

        PeakVisitsDay peak = visitService.getPeakDay(visits);

        assertEquals(target.getCreatedAt().toLocalDate(), peak.getDate());
        assertEquals(3, peak.getVisitCount());
    }

    @Test
    public void getTariffsPerDateTest() {
        var visitBasicTariff = VisitDto.builder()
                .subscription(SubscriptionDto.builder().tariffType(TariffType.BASIC).build())
                .build();
        var visitPremiumTariff = VisitDto.builder()
                .subscription(SubscriptionDto.builder().tariffType(TariffType.PREMIUM).build())
                .build();
        var visits = List.of(visitPremiumTariff, visitPremiumTariff, visitPremiumTariff, visitBasicTariff);

        List<VisitTariffPerDate> visitsByTariff = visitService.getTariffsPerDate(visits);

        var premium = visitsByTariff.stream().filter(e -> e.getTariffType() == TariffType.PREMIUM).findFirst().orElseThrow();
        assertEquals(2, visitsByTariff.size());
        assertEquals(3, premium.getCount());
    }
}
