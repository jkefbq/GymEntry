package com.jkefbq.gymentry.database.service;

import com.jkefbq.gymentry.database.dto.TariffDto;
import com.jkefbq.gymentry.database.dto.TariffType;

import java.util.List;
import java.util.Optional;

public interface TariffService {
    List<TariffDto> getAll();
    Optional<TariffDto> getByType(TariffType type);
    List<TariffDto> saveAll(List<TariffDto> tariffList);
    TariffDto create(TariffDto tariffDto);
    void deleteAll(List<TariffDto> tariffs);
}