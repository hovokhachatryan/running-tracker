package com.running_tracker.service;

import com.running_tracker.TestDataUtil;
import com.running_tracker.api.dto.request.run.FinishRunRequestDto;
import com.running_tracker.api.dto.request.run.StartRunRequestDto;
import com.running_tracker.api.dto.response.run.RunResponseDto;
import com.running_tracker.api.dto.response.run.StartRunResponseDto;
import com.running_tracker.api.dto.response.user.UserStatisticsDTO;
import com.running_tracker.domain.entity.Run;
import com.running_tracker.domain.repository.RunRepository;
import com.running_tracker.exception.ResourceNotFoundException;
import com.running_tracker.service.impl.RunServiceImpl;
import com.running_tracker.service.mapper.RunMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RunServiceImplTest {

    @InjectMocks
    private RunServiceImpl runService;

    @Mock
    private RunRepository runRepository;

    @Mock
    private RunMapper runMapper;

    @Mock
    private RunCalculatorService runCalculatorService;

    @Mock
    private UserService userService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startRunTest() {
        StartRunRequestDto requestDto = TestDataUtil.createStartRunRequestDto();
        Run runEntity = TestDataUtil.createRunEntity();
        StartRunResponseDto expectedResponse = TestDataUtil.createStartRunResponseDto();

        when(userService.findUserById(any(UUID.class))).thenReturn(TestDataUtil.createUser());
        when(runMapper.toEntity(any(), any())).thenReturn(runEntity);
        when(runRepository.save(any(Run.class))).thenReturn(runEntity);
        when(runMapper.toStartRunResponseDto(any(Run.class))).thenReturn(expectedResponse);

        StartRunResponseDto actualResponse = runService.startRun(requestDto);

        assertEquals(expectedResponse, actualResponse);
        verify(runRepository, times(1)).save(any(Run.class));
    }

    @Test
    void finishRunTest() {
        FinishRunRequestDto requestDto = TestDataUtil.createFinishRunRequestDto();
        UUID runId = UUID.randomUUID();
        Run existingRun = TestDataUtil.createRunEntity();
        RunResponseDto expectedResponse = TestDataUtil.createRunDto();

        when(runRepository.findById(runId)).thenReturn(Optional.of(existingRun));
        when(runRepository.save(any(Run.class))).thenReturn(existingRun);
        when(runMapper.toRunDto(any(Run.class))).thenReturn(expectedResponse);

        RunResponseDto actualResponse = runService.finishRun(requestDto, runId);

        assertEquals(expectedResponse, actualResponse);
        verify(runRepository, times(1)).save(any(Run.class));
    }

    @Test
    void finishRunWithoutDistanceTest() {
        FinishRunRequestDto requestDto = TestDataUtil.createFinishRunRequestDto();
        requestDto.setDistance(null);
        UUID runId = UUID.randomUUID();
        Run existingRun = TestDataUtil.createRunEntity();
        RunResponseDto expectedResponse = TestDataUtil.createRunDto();

        when(runRepository.findById(runId)).thenReturn(Optional.of(existingRun));
        when(runCalculatorService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(120.0);
        when(runCalculatorService.calculateAverageSpeed(anyDouble(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(10.0);
        when(runRepository.save(any(Run.class))).thenReturn(existingRun);
        when(runMapper.toRunDto(any(Run.class))).thenReturn(expectedResponse);

        RunResponseDto actualResponse = runService.finishRun(requestDto, runId);

        assertEquals(expectedResponse, actualResponse);
        verify(runCalculatorService, times(1)).calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble());
        verify(runRepository, times(1)).save(any(Run.class));
    }

    @Test
    void finishRunExceptionTest() {
        UUID runId = UUID.randomUUID();
        FinishRunRequestDto requestDto = TestDataUtil.createFinishRunRequestDto();

        when(runRepository.findById(runId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> runService.finishRun(requestDto, runId));
        verify(runRepository, times(1)).findById(runId);
    }

    @Test
    void getAllRunsForUserTest() {
        UUID userId = UUID.randomUUID();
        LocalDateTime fromDatetime = LocalDateTime.now().minusDays(7);
        LocalDateTime toDatetime = LocalDateTime.now();
        List<Run> runs = List.of(TestDataUtil.createRunEntity());
        RunResponseDto expectedRunResponseDto = TestDataUtil.createRunDto();

        when(runRepository.findAllByUserIdAndStartDatetimeBetween(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(runs);
        when(runMapper.toRunDto(any(Run.class))).thenReturn(expectedRunResponseDto);

        List<RunResponseDto> result = runService.getAllRunsForUser(userId, fromDatetime, toDatetime);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedRunResponseDto, result.get(0));
    }

    @Test
     void getAllRunsForUserNoDatesTest() {
        UUID userId = UUID.randomUUID();
        List<Run> runs = List.of(TestDataUtil.createRunEntity());
        RunResponseDto expectedRunResponseDto = TestDataUtil.createRunDto();

        when(runRepository.findAllByUserId(any(UUID.class)))
                .thenReturn(runs);
        when(runMapper.toRunDto(any(Run.class))).thenReturn(expectedRunResponseDto);

        List<RunResponseDto> result = runService.getAllRunsForUser(userId, null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedRunResponseDto, result.get(0));
    }

    @Test
    void getAllRunsForUserNoToDateTest() {
        LocalDateTime fromDatetime = LocalDateTime.now().minusDays(7);
        UUID userId = UUID.randomUUID();
        List<Run> runs = List.of(TestDataUtil.createRunEntity());
        RunResponseDto expectedRunResponseDto = TestDataUtil.createRunDto();

        when(runRepository.findAllByUserIdAndStartDatetimeBetween(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(runs);
        when(runMapper.toRunDto(any(Run.class))).thenReturn(expectedRunResponseDto);

        List<RunResponseDto> result = runService.getAllRunsForUser(userId, fromDatetime, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedRunResponseDto, result.get(0));
    }

    @Test
    void getUserStatisticsTest() {
        UUID userId = UUID.randomUUID();
        List<Run> runs = List.of(TestDataUtil.createRunEntity());
        UserStatisticsDTO expectedStatistics = TestDataUtil.createUserStatisticsDTO(1, 100.0, 2.5);

        when(runRepository.findAllByUserIdAndStartDatetimeBetween(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(runs);

        UserStatisticsDTO result = runService.getUserStatistics(userId, LocalDateTime.now().minusDays(7), LocalDateTime.now());

        assertEquals(expectedStatistics, result);
    }

    @Test
    void getUserStatisticsNoRunTest() {
        UUID userId = UUID.randomUUID();

        when(runRepository.findAllByUserIdAndStartDatetimeBetween(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        UserStatisticsDTO result = runService.getUserStatistics(userId, LocalDateTime.now().minusDays(7), LocalDateTime.now());

        assertEquals(0, result.getTotalRuns());
        assertEquals(0.0, result.getTotalDistance());
        assertEquals(0.0, result.getAverageSpeed());
    }

    @Test
    void getUserStatisticsNoDatesTest() {
        UUID userId = UUID.randomUUID();
        List<Run> runs = List.of(TestDataUtil.createRunEntity());
        UserStatisticsDTO expectedStatistics = TestDataUtil.createUserStatisticsDTO(1, 100.0, 2.5);

        when(runRepository.findAllByUserId(any(UUID.class))).thenReturn(runs);

        UserStatisticsDTO result = runService.getUserStatistics(userId, null, null);

        assertEquals(expectedStatistics, result);
    }
}
