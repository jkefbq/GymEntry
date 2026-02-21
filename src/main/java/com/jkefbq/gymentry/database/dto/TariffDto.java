package com.jkefbq.gymentry.database.dto;


import com.jkefbq.gymentry.shop.dto.TariffType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {
    private UUID id;
    private String tariffName;
    private String description;
    private BigDecimal pricePerLesson;
    private TariffType tariffType;
}
