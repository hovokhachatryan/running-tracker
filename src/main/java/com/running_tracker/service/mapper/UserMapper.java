package com.running_tracker.service.mapper;

import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import com.running_tracker.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequestDto requestDto);

    UserResponseDto toDto(User user);
}
