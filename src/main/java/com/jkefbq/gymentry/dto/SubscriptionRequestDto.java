package com.jkefbq.gymentry.dto;

import com.jkefbq.gymentry.database.dto.TariffType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionRequestDto {
    private Integer visitsTotal;
    private TariffType tariffType;
}
