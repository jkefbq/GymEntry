package com.jkefbq.gymentry.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PurchaseTariffTypePerDate {
    private TariffType tariffType;
    private Long purchaseCount;
    private BigDecimal purchaseSum;
}
