package com.jkefbq.gymentry.database.repository;

import com.jkefbq.gymentry.database.entity.Tariff;
import com.jkefbq.gymentry.database.dto.TariffType;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TariffRepository extends JpaRepository<@NonNull Tariff, @NonNull UUID> {
    Optional<Tariff> getByTariffType(TariffType tariffType);
}
