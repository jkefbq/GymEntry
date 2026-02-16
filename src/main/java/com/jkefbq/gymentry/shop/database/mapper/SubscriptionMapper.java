package com.jkefbq.gymentry.shop.database.mapper;

import com.jkefbq.gymentry.shop.database.dto.SubscriptionDto;
import com.jkefbq.gymentry.shop.database.entity.Subscription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {
    SubscriptionDto toDto(Subscription entity);
    Subscription toEntity(SubscriptionDto dto);
}
