package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.VisitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitService {
    VisitDto create(VisitDto dto);
    List<VisitDto> getAll();
    List<VisitDto> getAllForPeriod(LocalDateTime from, LocalDateTime to, String address);
}
