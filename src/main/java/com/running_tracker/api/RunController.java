package com.running_tracker.api;

import com.running_tracker.api.dto.request.run.FinishRunRequestDto;
import com.running_tracker.api.dto.request.run.StartRunRequestDto;
import com.running_tracker.api.dto.response.ErrorResponseDto;
import com.running_tracker.api.dto.response.run.RunResponseDto;
import com.running_tracker.api.dto.response.run.StartRunResponseDto;
import com.running_tracker.api.dto.response.user.UserStatisticsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller for managing runs and statistics.
 */
public interface RunController {

    /**
     * Starts a new run for the user.
     *
     * @param requestDto the start run request data
     * @return the response containing the run start details
     */
    @Operation(summary = "Create a new run",
            responses = {
                    @ApiResponse(description = "Run created successfully", responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StartRunResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<StartRunResponseDto> startRun(StartRunRequestDto requestDto);

    /**
     * Finishes a run for the user.
     *
     * @param requestDto the finish run request data
     * @param id the ID of the run to be finished
     * @return the response containing the finished run details
     */
    @Operation(summary = "Finish a run",
            responses = {
                    @ApiResponse(description = "Run finished successfully", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RunResponseDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<RunResponseDto> finishRun(FinishRunRequestDto requestDto, UUID id);

    /**
     * Retrieves all runs for a user within the given time range.
     *
     * @param userId the ID of the user
     * @param fromDatetime the start of the time range (optional)
     * @param toDatetime the end of the time range (optional)
     * @return a list of runs for the user
     */
    @Operation(summary = "Get all runs for a user",
            responses = {
                    @ApiResponse(description = "Successfully found all runs", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RunResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<List<RunResponseDto>> getAllRunsForUser(UUID userId, LocalDateTime fromDatetime, LocalDateTime toDatetime);

    /**
     * Retrieves statistics for a user within the given time range.
     *
     * @param userId the ID of the user
     * @param fromDatetime the start of the time range (optional)
     * @param toDatetime the end of the time range (optional)
     * @return the user's statistics
     */
    @Operation(summary = "Get user statistics",
            responses = {
                    @ApiResponse(description = "Successfully retrieved user statistics", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserStatisticsDTO.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<UserStatisticsDTO> getUserStatistics(UUID userId, LocalDateTime fromDatetime, LocalDateTime toDatetime);

}
