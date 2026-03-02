package com.jkefbq.gymentry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseStatistics {
    private LocalDate from;
    private LocalDate to;
    private Integer totalPurchases;
    private BigDecimal totalRevenue;
    private BigDecimal avgDayCheck;
    private BigDecimal avgPersonCheck;
    private PeakPurchasesDay peakPurchasesDay;
    private List<PurchasePerDate> byDay;
    private List<PurchaseTariffTypePerDate> byTariffType;
}
