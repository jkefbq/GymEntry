package com.jkefbq.gymentry.database.dto;

import lombok.Data;

@Data
public class SubscriptionRequestDto {
    private Integer visitsTotal;
    private TariffType tariffType;
}
