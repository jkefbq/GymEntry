package com.jkefbq.gymentry.shop.database.entity;

import com.jkefbq.gymentry.shop.dto.TariffType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tariffs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tariff {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    private String tariffName;
    private String description;
    @Enumerated(EnumType.STRING)
    private TariffType tariffType;
}
