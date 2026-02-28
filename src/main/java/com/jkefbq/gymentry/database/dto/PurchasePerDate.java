package com.jkefbq.gymentry.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PurchasePerDate {
    private LocalDate date;
    private Long purchaseCount;
    private BigDecimal purchaseSum;
}
