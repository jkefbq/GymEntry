package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.dto.PeakVisitsDay;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.dto.VisitPerDate;
import com.jkefbq.gymentry.dto.VisitTariffPerDate;
import com.jkefbq.gymentry.database.entity.Visit;
import com.jkefbq.gymentry.database.mapper.VisitMapper;
import com.jkefbq.gymentry.database.repository.VisitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitServiceImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @Transactional
    @Override
    public VisitDto create(VisitDto dto) {
        Visit notSavedEntity = visitMapper.toEntity(dto);
        Visit savedEntity = visitRepository.save(notSavedEntity);
        return visitMapper.toDto(savedEntity);
    }

    @Override
    @Transactional
    public List<VisitDto> getAll() {
        return visitRepository.findAll().stream().map(visitMapper::toDto).toList();
    }

    @Transactional
    @Override
    public List<VisitDto> getAllForPeriod(LocalDateTime from, LocalDateTime to, String address) {
        return visitRepository.getAllForPeriod(from, to, address).stream().map(visitMapper::toDto).toList();
    }

    @Override
    public BigDecimal getAvgPerDay(Integer visitCount, Long wholeDays) {
        return BigDecimal.valueOf(visitCount)
                .divide(
                        BigDecimal.valueOf(wholeDays),
                        RoundingMode.HALF_UP
                );
    }

    @Override
    public List<VisitPerDate> getVisitsPerDate(List<VisitDto> visitsForPeriod) {
        return visitsForPeriod.stream()
                .map(visit -> visit.getCreatedAt().toLocalDate())
                .collect(Collectors.toMap(
                        visit -> visit,
                        visit -> 1L,
                        Long::sum
                )).entrySet().stream()
                .map(entry -> new VisitPerDate(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(VisitPerDate::getDate))
                .toList();
    }

    @Override
    public PeakVisitsDay getPeakDay(List<VisitDto> visitsForPeriod) {
        List<VisitPerDate> visitsPerDates = getVisitsPerDate(visitsForPeriod);
        return visitsPerDates.stream()
                .max(Comparator.comparing(VisitPerDate::getVisitCount))
                .map(visit -> new PeakVisitsDay(visit.getDate(), visit.getVisitCount()))
                .orElseThrow(() -> new IllegalStateException("result array is empty"));
    }

    @Override
    public List<VisitTariffPerDate> getTariffsPerDate(List<VisitDto> visitsForPeriod) {
        return visitsForPeriod.stream()
                .map(visit -> visit.getSubscription().getTariffType())
                .collect(Collectors.toMap(
                        tariff -> tariff,
                        tariff -> 1L,
                        Long::sum
                )).entrySet().stream()
                .map(entry -> new VisitTariffPerDate(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(VisitTariffPerDate::getCount))
                .toList();
    }

}