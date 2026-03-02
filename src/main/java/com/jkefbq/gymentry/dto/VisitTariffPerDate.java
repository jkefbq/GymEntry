package com.jkefbq.gymentry.dto;

import com.jkefbq.gymentry.database.dto.TariffType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitTariffPerDate {
    private TariffType tariffType;
    private Long count;
}
