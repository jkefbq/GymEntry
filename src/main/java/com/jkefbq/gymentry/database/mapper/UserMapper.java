package com.jkefbq.gymentry.database.mapper;

import com.jkefbq.gymentry.database.dto.UserDto;
import com.jkefbq.gymentry.database.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {SubscriptionMapper.class})
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
