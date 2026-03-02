package com.jkefbq.gymentry.facade;

import com.jkefbq.gymentry.dto.PurchaseStatistics;
import com.jkefbq.gymentry.dto.VisitStatistics;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AdminStatisticsFacade {
    VisitStatistics getVisitStatisticsForPeriod(LocalDateTime from, LocalDateTime to, String address);
    PurchaseStatistics getPurchaseStatisticsForPeriod(LocalDate from, LocalDate to);
}
