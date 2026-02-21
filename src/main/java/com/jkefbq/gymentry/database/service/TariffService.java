package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.TariffDto;
import com.jkefbq.gymentry.shop.dto.TariffType;

import java.util.List;
import java.util.Optional;

public interface TariffService {
    List<TariffDto> getAll();
    Optional<TariffDto> getByType(TariffType type);
}