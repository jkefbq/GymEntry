package com.jkefbq.gymentry.database.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitTariffPerDate {
    private TariffType tariffType;
    private Long count;
}
