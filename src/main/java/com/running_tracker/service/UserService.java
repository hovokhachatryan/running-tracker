package com.running_tracker.service;

import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.user.DeleteUserResponseDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import com.running_tracker.domain.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {

    /**
     * Saves a new user based on the provided user request data.
     *
     * @param requestDto the data to create a new user
     * @return the response containing details of the created user
     */
    UserResponseDto save(UserRequestDto requestDto);

    /**
     * Finds a user dto by ID.
     *
     * @param id the unique identifier of the user
     * @return the response containing details of the found user
     */
    UserResponseDto findUserDtoById(UUID id);

    /**
     * Retrieves a user entity by ID.
     * This method returns the full user entity.
     *
     * @param id the unique identifier of the user
     * @return the user entity
     */
    User findUserById(UUID id);

    /**
     * Finds all users in the system.
     *
     * @return a list of responses, each containing details of a user
     */
    List<UserResponseDto> findAll();

    /**
     * Updates an existing user based on the provided user request data.
     *
     * @param responseDto the updated user details
     * @param id          the unique identifier of the user to update
     * @return the response containing the updated user details
     */
    UserResponseDto update(UserRequestDto responseDto, UUID id);

    /**
     * Deletes a user by their unique ID.
     *
     * @param id the unique identifier of the user to delete
     * @return a response containing a message about the successful deletion
     */
    DeleteUserResponseDto delete(UUID id);

}
