package com.running_tracker.service;

import com.running_tracker.api.dto.request.run.FinishRunRequestDto;
import com.running_tracker.api.dto.request.run.StartRunRequestDto;
import com.running_tracker.api.dto.response.run.RunResponseDto;
import com.running_tracker.api.dto.response.run.StartRunResponseDto;
import com.running_tracker.api.dto.response.user.UserStatisticsDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing running sessions and retrieving user statistics.
 */
public interface RunService {

    /**
     * Starts a new run.
     *
     * @param requestDto the request to start a new run
     * @return the response containing details of the started run
     */
    StartRunResponseDto startRun(StartRunRequestDto requestDto);

    /**
     * Finishes a run for the user based on the provided finish run request data.
     *
     * @param requestDto the data to finish the run
     * @param id the unique identifier of the run to be finished
     * @return the response containing details of the finished run
     */
    RunResponseDto finishRun(FinishRunRequestDto requestDto, UUID id);

    /**
     * Retrieves all runs for a specific user within the given time range.
     *
     * @param userId the unique identifier of the user
     * @param fromDatetime the start of the time range (optional)
     * @param toDatetime the end of the time range (optional)
     * @return a list of responses, each containing details of a user's run
     */
    List<RunResponseDto> getAllRunsForUser(UUID userId, LocalDateTime fromDatetime, LocalDateTime toDatetime);

    /**
     * Retrieves statistics for a user based on their running activities within the given time range.
     *
     * @param userId the unique identifier of the user
     * @param fromDatetime the start of the time range (optional)
     * @param toDatetime the end of the time range (optional)
     * @return the response containing the user's running statistics
     */
    UserStatisticsDTO getUserStatistics(UUID userId, LocalDateTime fromDatetime, LocalDateTime toDatetime);

}