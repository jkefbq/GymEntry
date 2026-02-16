package com.jkefbq.gymentry.shop.database.dto;

import com.jkefbq.gymentry.shop.dto.TariffType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private UUID id;
    private Integer visitsTotal;
    private BigDecimal snapshotPrice;
    private LocalDate purchaseAt;
    private Boolean isUnlimited;
    private Integer visitsLeft;
    private List<String> permissions;
    private TariffType tariffType;
}
