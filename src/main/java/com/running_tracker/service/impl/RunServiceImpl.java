package com.running_tracker.service.impl;

import com.running_tracker.api.dto.request.run.FinishRunRequestDto;
import com.running_tracker.api.dto.request.run.StartRunRequestDto;
import com.running_tracker.api.dto.response.run.RunResponseDto;
import com.running_tracker.api.dto.response.run.StartRunResponseDto;
import com.running_tracker.api.dto.response.user.UserStatisticsDTO;
import com.running_tracker.domain.entity.Run;
import com.running_tracker.domain.repository.RunRepository;
import com.running_tracker.exception.CrudException;
import com.running_tracker.exception.RunCalculationException;
import com.running_tracker.exception.ResourceNotFoundException;
import com.running_tracker.service.RunCalculatorService;
import com.running_tracker.service.RunService;
import com.running_tracker.service.UserService;
import com.running_tracker.service.mapper.RunMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RunServiceImpl implements RunService {

    private static final String DISTANCE_UNIT = "meters";
    private static final String AVERAGE_SPEED_UNIT = "km/h";

    private final UserService userService;
    private final RunCalculatorService runCalculatorService;

    private final RunMapper runMapper;
    private final RunRepository runRepository;

    @Override
    public StartRunResponseDto startRun(StartRunRequestDto requestDto) {
        Instant start = Instant.now();
        try {
            Run run = runMapper.toEntity(requestDto, userService.findUserById(requestDto.getUserId()));
            StartRunResponseDto startRunResponseDto = runMapper.toStartRunResponseDto(runRepository.save(run));
            log.info(String.format("Run was created for user with id %s", requestDto.getUserId()));
            return startRunResponseDto;
        } catch (Exception exception) {
            throw new CrudException(exception.getMessage());
        } finally {
            Instant end = Instant.now();
            log.info(String.format("Start run logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }

    }

    @Override
    public RunResponseDto finishRun(FinishRunRequestDto requestDto, UUID id) {
        Instant start = Instant.now();
        try {
            Run run = runRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("Run with id %s does not exists", id)));

            // Set finish run details
            run.setFinishLatitude(requestDto.getFinishLatitude());
            run.setFinishLongitude(requestDto.getFinishLongitude());
            run.setFinishDatetime(requestDto.getFinishDatetime());

            // Set distance
            run.setDistance(calculateDistance(requestDto, run));
            run.setDistanceUnit(DISTANCE_UNIT);

            //Set average speed
            run.setAverageSpeed(runCalculatorService.calculateAverageSpeed(run.getDistance(), run.getStartDatetime(), requestDto.getFinishDatetime()));
            run.setAverageSpeedUnit(AVERAGE_SPEED_UNIT);


            RunResponseDto runResponseDto = runMapper.toRunDto(runRepository.save(run));
            log.info(String.format("Run was finished for user [userId:%s]", runResponseDto.getUserId()));
            return runResponseDto;
        } catch (ResourceNotFoundException exception) {
            log.error(String.format("Resource not found, Error:%s", exception.getMessage()));
            throw exception;
        } catch (RunCalculationException exception) {
            log.error(String.format("Processing exception, Error:%s", exception.getMessage()));
            throw exception;
        } catch (Exception exception) {
            log.error(String.format("Crud exception, Error:%s", exception.getMessage()));
            throw new CrudException(exception.getMessage());
        } finally {
            Instant end = Instant.now();
            log.info(String.format("Finish run logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }


    @Override
    public List<RunResponseDto> getAllRunsForUser(UUID userId, LocalDateTime fromDatetime, LocalDateTime toDatetime) {
        Instant start = Instant.now();
        try {
            // If fromDatetime is null, return all runs for the user
            if (fromDatetime == null) {
                return runRepository.findAllByUserId(userId).stream()
                        .map(runMapper::toRunDto)
                        .collect(Collectors.toList());
            }

            // If fromDatetime is not null but toDatetime is null, set toDatetime to now
            toDatetime = (toDatetime != null) ? toDatetime : LocalDateTime.now();

            List<Run> runs = runRepository.findAllByUserIdAndStartDatetimeBetween(userId, fromDatetime, toDatetime);
            log.info(String.format("found all runs for user with id %s", userId));

            return runs.stream()
                    .map(runMapper::toRunDto)
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            log.error(String.format("Crud exception, Error:%s", exception.getMessage()));
            throw new CrudException(exception.getMessage());
        } finally {
            Instant end = Instant.now();
            log.info(String.format("Get all runs for user logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }

    @Override
    public UserStatisticsDTO getUserStatistics(UUID userId, LocalDateTime fromDatetime, LocalDateTime toDatetime) {
        Instant start = Instant.now();
        try {
            // Default toDatetime to now if fromDatetime is provided but toDatetime is null
            toDatetime = (fromDatetime != null && toDatetime == null) ? LocalDateTime.now() : toDatetime;

            // Retrieve all runs for the user within the given time range
            List<Run> runs = (fromDatetime != null)
                    ? runRepository.findAllByUserIdAndStartDatetimeBetween(userId, fromDatetime, toDatetime)
                    : runRepository.findAllByUserId(userId);

            log.info(String.format("Found %s runs with user with id %s ",runs.size(), userId));

            // If no runs are found, return default statistics
            if (runs.isEmpty()) {
                return new UserStatisticsDTO(0, 0.0, 0.0);
            }

            // Calculate total distance and average speed, handle null values
            double totalDistance = runs.stream()
                    .mapToDouble(run -> run.getDistance() != null ? run.getDistance() : 0.0)
                    .sum();

            double averageSpeed = runs.stream()
                    .mapToDouble(run -> run.getAverageSpeed() != null ? run.getAverageSpeed() : 0.0)
                    .average()
                    .orElse(0.0);

            return new UserStatisticsDTO(runs.size(), totalDistance, averageSpeed);
        } catch (Exception exception) {
            log.error(String.format("Crud exception, Error: %s", exception.getMessage()));
            throw new CrudException(exception.getMessage());
        } finally {
            Instant end = Instant.now();
            log.info(String.format("Get user run statistics logic finished. [durationMs = %s]", Duration.between(start, end).toMillis()));
        }
    }

    // Calculate distance based on the provided finish run request data
    private double calculateDistance(FinishRunRequestDto requestDto, Run run) {
        return Objects.nonNull(requestDto.getDistance())
                ? requestDto.getDistance()
                : runCalculatorService.calculateDistance(run.getStartLatitude(), run.getStartLongitude(),
                requestDto.getFinishLatitude(), requestDto.getFinishLongitude());
    }
}
