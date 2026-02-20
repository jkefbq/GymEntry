package com.jkefbq.gymentry.database.mapper;

import com.jkefbq.gymentry.database.dto.NotVerifiedUserDto;
import com.jkefbq.gymentry.database.entity.NotVerifiedUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotVerifiedUserMapper {
    NotVerifiedUserDto toDto(NotVerifiedUser user);
    NotVerifiedUser toEntity(NotVerifiedUserDto dto);
}
