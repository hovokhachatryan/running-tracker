package com.running_tracker.service.mapper;

import com.running_tracker.api.dto.request.run.StartRunRequestDto;
import com.running_tracker.api.dto.response.run.RunResponseDto;
import com.running_tracker.api.dto.response.run.StartRunResponseDto;
import com.running_tracker.domain.entity.Run;
import com.running_tracker.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RunMapper {

    @Mapping(target = "userId", source = "run.user.id")
    @Mapping(target = "distance.value", source = "run.distance")
    @Mapping(target = "distance.unit", source = "run.distanceUnit")
    @Mapping(target = "averageSpeed.value", source = "run.averageSpeed")
    @Mapping(target = "averageSpeed.unit", source = "run.averageSpeedUnit")
    RunResponseDto toRunDto(Run run);

    @Mapping(target = "user", source = "user")
    Run toEntity(StartRunRequestDto requestDto, User user);

    StartRunResponseDto toStartRunResponseDto(Run run);
}
