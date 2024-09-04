package com.running_tracker.service.impl;

import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.user.DeleteUserResponseDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import com.running_tracker.domain.entity.User;
import com.running_tracker.domain.repository.UserRepository;
import com.running_tracker.exception.CrudException;
import com.running_tracker.exception.ResourceNotFoundException;
import com.running_tracker.service.UserService;
import com.running_tracker.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponseDto save(UserRequestDto requestDto) {
        Instant start = Instant.now();
        try {
            User user = userMapper.toEntity(requestDto);
            User savedUser = userRepository.save(user);
            log.info(String.format("User [id:%s] was saved", savedUser.getId()));
            return userMapper.toDto(savedUser);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new CrudException(String.format("Failed to save user with firstName %s and lastName %s in database: %s",
                    requestDto.getFirstName(), requestDto.getLastName(),  exception.getMessage()));
        } finally {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            log.info(String.format("Save user logic finished. [durationMs = %s]", duration));
        }

    }

    @Override
    public UserResponseDto findUserDtoById(UUID id) {
        Instant start = Instant.now();
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id[%s] not found", id)));
            log.info(String.format("User with id %s was found", id));
            return userMapper.toDto(user);
        } catch (ResourceNotFoundException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new CrudException(String.format("Failed to find user with id %s in database: %s", id, exception.getMessage()));
        } finally {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            log.info(String.format("User: %s. Find user dto logic finished. [durationMs = %s]", id, duration));
        }

    }

    @Override
    public User findUserById(UUID id) {
        Instant start = Instant.now();
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("User with id[%s] not found", id)));
            log.info(String.format("User with id %s was found", id));
            return user;
        } catch (ResourceNotFoundException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new CrudException(String.format("Failed to find user with id %s in database: %s", id, exception.getMessage()));
        } finally {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            log.info(String.format("User: %s. Find user logic finished. [durationMs = %s]", id, duration));
        }
    }

    @Override
    public List<UserResponseDto> findAll() {
        Instant start = Instant.now();
        try {
            List<User> users = userRepository.findAll();
            log.info(String.format("Successfully found %s users", users.size()));
            return users.stream()
                    .map(userMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new CrudException(String.format("Error finding all users. Error msg: %s", exception.getMessage()));
        } finally {
            Instant end = Instant.now();
            log.info(String.format("Find all users logic finished. [durationMs = %s]", Duration.between(start, end)));
        }
    }

    @Override
    public UserResponseDto update(UserRequestDto responseDto, UUID id) {
        Instant start = Instant.now();
        try {
            User user = findUserById(id);

            user.setFirstName(responseDto.getFirstName());
            user.setLastName(responseDto.getLastName());
            user.setBirthDate(responseDto.getBirthDate());
            user.setSex(responseDto.getSex());
            User savedUser = userRepository.save(user);
            log.info(String.format("User with id %s updated successfully", id));
            return userMapper.toDto(savedUser);
        } catch (ResourceNotFoundException exception) {
            log.error(exception.getMessage());
            throw exception;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new CrudException(String.format("Failed to update user with id %s in database: %s", id, exception.getMessage()));
        } finally {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            log.info(String.format("User: %s. Update user logic finished. [durationMs = %s]", id, duration));
        }
    }

    @Override
    public DeleteUserResponseDto delete(UUID id) {
        Instant start = Instant.now();
        try {
            userRepository.deleteById(id);
            log.info(String.format("User with id %s was deleted", id));
            return new DeleteUserResponseDto(String.format("User with id %s successfully deleted", id));
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException(exception.getMessage());
        } catch (Exception exception) {
            throw new CrudException(String.format("Failed to delete user with id %s in database: %s", id, exception.getMessage()));
        }finally {
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            log.info(String.format("User: %s. delete user logic finished. [durationMs = %s]", id, duration));
        }

    }
}
