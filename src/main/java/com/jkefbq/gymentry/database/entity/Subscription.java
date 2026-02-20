package com.jkefbq.gymentry.database.entity;

import com.jkefbq.gymentry.shop.dto.TariffType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private Integer visitsTotal;
    private BigDecimal snapshotPrice;
    private LocalDate purchaseAt;
    private Boolean isUnlimited;
    private Integer visitsLeft;
    private List<String> permissions;
    @Enumerated(EnumType.STRING)
    private TariffType tariffType;
    @ManyToOne
    private User user;
}
