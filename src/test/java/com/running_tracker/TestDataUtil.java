package com.running_tracker;

import com.running_tracker.api.dto.request.run.FinishRunRequestDto;
import com.running_tracker.api.dto.request.run.StartRunRequestDto;
import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.run.MeasurementDto;
import com.running_tracker.api.dto.response.run.RunResponseDto;
import com.running_tracker.api.dto.response.run.StartRunResponseDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import com.running_tracker.api.dto.response.user.UserStatisticsDTO;
import com.running_tracker.domain.entity.Run;
import com.running_tracker.domain.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataUtil {

    private static final String DISTANCE_UNIT = "meters";
    private static final String AVERAGE_SPEED_UNIT = "km/h";

    public static final UUID TEST_USER_ID = UUID.fromString("e51cb416-5757-425b-976e-160ddc24d9ab");

    public static final UUID TEST_RUN_ID = UUID.fromString("e51cb416-5757-425b-976e-160ddc24d9ac");

    public static UserResponseDto createUserResponseDto() {
        User user = createUser();
        return new UserResponseDto(user.getId(), user.getFirstName(), user.getLastName(), user.getBirthDate(), user.getSex());
    }

    public static UserRequestDto createUserRequestDto() {
        return new UserRequestDto( "Jon", "Smith",
                LocalDate.of(1999, 10, 1), "male");
    }

    public static User createUser() {
        return new User(UUID.fromString("e51cb416-5757-425b-976e-160ddc24d9ab"), "Jon", "Smith",
                LocalDate.of(1999, 10, 1), "male");
    }

    public static StartRunRequestDto createStartRunRequestDto() {
        StartRunRequestDto requestDto = new StartRunRequestDto();
        requestDto.setUserId(createUser().getId());
        requestDto.setStartLatitude(40.0);
        requestDto.setStartLongitude(-70.0);
        requestDto.setStartDatetime(LocalDateTime.now());
        return requestDto;
    }

    public static FinishRunRequestDto createFinishRunRequestDto() {
        FinishRunRequestDto requestDto = new FinishRunRequestDto();
        requestDto.setDistance(100.0);
        requestDto.setFinishLatitude(12.234);
        requestDto.setFinishLongitude(13.3423);
        requestDto.setFinishDatetime(LocalDateTime.now());
        return requestDto;
    }

    public static Run createRunEntity() {
        Run run = new Run();
        run.setId(TEST_USER_ID);
        run.setUser(createUser());
        run.setStartLatitude(40.0);
        run.setStartLongitude(-70.0);
        run.setStartDatetime(LocalDateTime.now().minusHours(1));
        run.setFinishLatitude(42.0);
        run.setFinishLongitude(-72.0);
        run.setFinishDatetime(LocalDateTime.now());
        run.setDistance(100.0);
        run.setAverageSpeed(2.5);
        return run;
    }

    public static RunResponseDto createRunDto() {
        RunResponseDto runResponseDto = new RunResponseDto();
        runResponseDto.setId(TEST_RUN_ID);
        runResponseDto.setUserId(createUser().getId());
        runResponseDto.setDistance(createMeasurementDto(100.0, DISTANCE_UNIT));
        runResponseDto.setAverageSpeed(createMeasurementDto(2.5, AVERAGE_SPEED_UNIT));
        runResponseDto.setStartLatitude(40.0);
        runResponseDto.setStartLongitude(-70.0);
        runResponseDto.setStartDatetime(LocalDateTime.now().minusHours(1));
        runResponseDto.setFinishLatitude(42.0);
        runResponseDto.setFinishLongitude(-72.0);
        runResponseDto.setFinishDatetime(LocalDateTime.now());
        return runResponseDto;
    }

    public static StartRunResponseDto createStartRunResponseDto() {
        StartRunResponseDto responseDto = new StartRunResponseDto();
        responseDto.setId(TEST_RUN_ID);
        responseDto.setStartDatetime(LocalDateTime.now().minusHours(6));
        responseDto.setStartLatitude(10.323);
        responseDto.setStartLongitude(11.438);
        return responseDto;
    }

    public static UserStatisticsDTO createUserStatisticsDTO(int totalRuns, double totalDistance, double averageSpeed) {
        return new UserStatisticsDTO(1, 100.0, 2.5);
    }

    public static MeasurementDto createMeasurementDto(double value, String unit) {
        MeasurementDto measurementDto = new MeasurementDto();
        measurementDto.setUnit(unit);
        measurementDto.setValue(value);
        return measurementDto;
    }
}

