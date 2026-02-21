package com.jkefbq.gymentry.database.dto;

import com.jkefbq.gymentry.shop.dto.TariffType;
import lombok.Data;

@Data
public class SubscriptionRequestDto {
    private Integer visitsTotal;
    private TariffType tariffType;
}
