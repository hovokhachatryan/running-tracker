package com.running_tracker.api;


import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.user.DeleteUserResponseDto;
import com.running_tracker.api.dto.response.ErrorResponseDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing users.
 */
public interface UserController {

    /**
     * Save a new User.
     *
     * @param requestDto the user details to save
     * @return the response containing the User details and new ID
     */
    @Operation(summary = "Save a new user",
            responses = {
                    @ApiResponse(description = "User created successfully", responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<UserResponseDto> save(UserRequestDto requestDto);

    /**
     * Find user by ID.
     *
     * @param id the UUID of the user
     * @return User details
     */
    @Operation(summary = "Find user by ID",
            responses = {
                    @ApiResponse(description = "Successfully found user", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<UserResponseDto> findById(UUID id);

    /**
     * Find all users.
     *=
     * @return All users details
     */
    @Operation(summary = "Find all users",
            responses = {
                    @ApiResponse(
                            description = "Successfully found all users",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            type = "object",
                                            implementation = UserResponseDto.class,
                                            description = "A list of users"
                                    )
                            )
                    ),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<List<UserResponseDto>> findAll();

    /**
     * Update user by id.
     *
     * @param requestDto user details to update
     * @param id         the user to update
     * @return the response containing the User details
     */
    @Operation(summary = "Update user by id",
            responses = {
                    @ApiResponse(description = "User updated successfully", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<UserResponseDto> update(UserRequestDto requestDto, UUID id);

    /**
     * Delete user by id.
     *
     * @param id the user to save
     * @return response containing a message about successful deletion
     */
    @Operation(summary = "delete user by id.",
            responses = {
                    @ApiResponse(description = "User deleted successfully", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeleteUserResponseDto.class))),
                    @ApiResponse(description = "Bad request", responseCode = "400",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class))),
                    @ApiResponse(description = "Internal server error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDto.class)))
            })
    ResponseEntity<DeleteUserResponseDto> delete(UUID id);
}
