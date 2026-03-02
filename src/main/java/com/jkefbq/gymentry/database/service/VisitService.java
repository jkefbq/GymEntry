package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.dto.PeakVisitsDay;
import com.jkefbq.gymentry.database.dto.VisitDto;
import com.jkefbq.gymentry.dto.VisitPerDate;
import com.jkefbq.gymentry.dto.VisitTariffPerDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {
    VisitDto create(VisitDto dto);
    List<VisitDto> getAll();
    List<VisitDto> getAllForPeriod(LocalDateTime from, LocalDateTime to, String address);
    BigDecimal getAvgPerDay(Integer visitCount, Long wholeDays);
    List<VisitPerDate> getVisitsPerDate(List<VisitDto> visitsForPeriod);
    PeakVisitsDay getPeakDay(List<VisitDto> visitsForPeriod);
    List<VisitTariffPerDate> getTariffsPerDate(List<VisitDto> visitsForPeriod);
}
