package com.jkefbq.gymentry.shop.database.mapper;

import com.jkefbq.gymentry.shop.database.dto.UserDto;
import com.jkefbq.gymentry.shop.database.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
