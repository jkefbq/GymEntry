package com.jkefbq.gymentry.database.mapper;

import com.jkefbq.gymentry.database.dto.TariffDto;
import com.jkefbq.gymentry.database.entity.Tariff;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TariffMapper {
    TariffDto toDto(Tariff tariff);
    Tariff toEntity(TariffDto dto);
}
