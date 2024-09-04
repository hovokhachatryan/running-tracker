package com.running_tracker.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.running_tracker.TestDataUtil;
import com.running_tracker.api.dto.request.run.FinishRunRequestDto;
import com.running_tracker.api.dto.request.run.StartRunRequestDto;
import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.run.RunResponseDto;
import com.running_tracker.api.dto.response.run.StartRunResponseDto;
import com.running_tracker.api.dto.response.user.DeleteUserResponseDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import com.running_tracker.api.dto.response.user.UserStatisticsDTO;
import com.running_tracker.api.impl.RunControllerImpl;
import com.running_tracker.api.impl.UserControllerImpl;
import com.running_tracker.service.RunService;
import com.running_tracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RunControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private RunService runService;
    @InjectMocks
    private RunControllerImpl runController;
    private ObjectMapper objectMapper;


    private UUID userId;
    private UUID runId;
    private StartRunRequestDto startRunRequestDto;
    private StartRunResponseDto startRunResponseDto;
    private FinishRunRequestDto finishRunRequestDto;
    private RunResponseDto runResponseDto;
    private UserStatisticsDTO userStatisticsDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(runController).build();
        userId = TestDataUtil.TEST_USER_ID;
        runId = TestDataUtil.TEST_RUN_ID;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Initialize test data
        startRunRequestDto = TestDataUtil.createStartRunRequestDto();
        startRunResponseDto = TestDataUtil.createStartRunResponseDto();
        finishRunRequestDto = TestDataUtil.createFinishRunRequestDto();
        runResponseDto = TestDataUtil.createRunDto();
        userStatisticsDTO = TestDataUtil.createUserStatisticsDTO(1, 100, 2.5);
    }


    @Test
    void startRunTest() throws Exception {
        when(runService.startRun(any(StartRunRequestDto.class))).thenReturn(startRunResponseDto);

        mockMvc.perform(post("/api/runs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startRunRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(startRunResponseDto.getId().toString()))
                .andExpect(jsonPath("$.startLatitude").value(startRunResponseDto.getStartLatitude()))
                .andExpect(jsonPath("$.startLongitude").value(startRunResponseDto.getStartLongitude()));

        verify(runService).startRun(any(StartRunRequestDto.class));
    }

    @Test
    void finishRunTest() throws Exception {
        when(runService.finishRun(any(FinishRunRequestDto.class), any(UUID.class))).thenReturn(runResponseDto);

        mockMvc.perform(put("/api/runs/{id}/finish", runId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(finishRunRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(runId.toString()))
                .andExpect(jsonPath("$.userId").value(userId.toString()))
                .andExpect(jsonPath("$.finishLatitude").value(runResponseDto.getFinishLatitude()))
                .andExpect(jsonPath("$.finishLongitude").value(runResponseDto.getFinishLongitude()))
                .andExpect(jsonPath("$.startLongitude").value(runResponseDto.getStartLongitude()))
                .andExpect(jsonPath("$.startLatitude").value(runResponseDto.getStartLatitude()))
                .andExpect(jsonPath("$.distance.value").value(runResponseDto.getDistance().getValue()))
                .andExpect(jsonPath("$.distance.unit").value(runResponseDto.getDistance().getUnit()))
                .andExpect(jsonPath("$.averageSpeed.value").value(runResponseDto.getAverageSpeed().getValue()))
                .andExpect(jsonPath("$.averageSpeed.unit").value(runResponseDto.getAverageSpeed().getUnit()));

        verify(runService).finishRun(any(FinishRunRequestDto.class), any(UUID.class));
    }

    @Test
    void getAllRunsForUserTest() throws Exception {
        when(runService.getAllRunsForUser(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(runResponseDto));

        mockMvc.perform(get("/api/runs")
                        .param("userId", userId.toString())
                        .param("fromDatetime", LocalDateTime.now().minusDays(1).toString())
                        .param("toDatetime", LocalDateTime.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(runId.toString()))
                .andExpect(jsonPath("$[0].userId").value(userId.toString()));

        verify(runService).getAllRunsForUser(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void getUserStatisticsTest() throws Exception {
        when(runService.getUserStatistics(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(userStatisticsDTO);

        mockMvc.perform(get("/api/runs/statistics")
                        .param("userId", userId.toString())
                        .param("fromDatetime", LocalDateTime.now().minusDays(1).toString())
                        .param("toDatetime", LocalDateTime.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRuns").value(userStatisticsDTO.getTotalRuns()))
                .andExpect(jsonPath("$.totalDistance").value(userStatisticsDTO.getTotalDistance()))
                .andExpect(jsonPath("$.averageSpeed").value(userStatisticsDTO.getAverageSpeed()));

        verify(runService).getUserStatistics(any(UUID.class), any(LocalDateTime.class), any(LocalDateTime.class));
    }

}
