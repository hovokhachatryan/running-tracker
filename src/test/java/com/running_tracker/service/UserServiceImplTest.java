package com.running_tracker.service;

import com.running_tracker.TestDataUtil;
import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.user.DeleteUserResponseDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import com.running_tracker.domain.entity.User;
import com.running_tracker.domain.repository.UserRepository;
import com.running_tracker.exception.CrudException;
import com.running_tracker.exception.ResourceNotFoundException;
import com.running_tracker.service.impl.UserServiceImpl;
import com.running_tracker.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto userRequestDto;
    private User user;
    private UserResponseDto userResponseDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        userRequestDto = TestDataUtil.createUserRequestDto();
        user = TestDataUtil.createUser();
        userResponseDto = TestDataUtil.createUserResponseDto();
    }

    @Test
    void saveTest() {
        when(userMapper.toEntity(any(UserRequestDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.save(userRequestDto);

        assertNotNull(result);
        assertEquals(userResponseDto.getId(), result.getId());
        verify(userMapper, times(1)).toEntity(userRequestDto);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void findUserDtoByIdTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.findUserDtoById(userId);

        assertNotNull(result);
        assertEquals(userResponseDto.getId(), result.getId());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void findUserDtoByIdExceptionTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserDtoById(userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(0)).toDto(any(User.class));
    }

    @Test
    void findUserByIdTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findUserByIdTestExceptionTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findUserById(userId));
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void findAllTest() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        List<UserResponseDto> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void updateTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.update(userRequestDto, userId);

        assertNotNull(result);
        assertEquals(userRequestDto.getFirstName(), result.getFirstName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toDto(user);
    }

    @Test
    void updateExceptionTest() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(userRequestDto, userId));

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void deleteTest() {
        doNothing().when(userRepository).deleteById(userId);

        DeleteUserResponseDto result = userService.delete(userId);

        assertNotNull(result);
        assertEquals(String.format("User with id %s successfully deleted", userId), result.getMessage());
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteExceptionTest() {
        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(userId);

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(userId));

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteExceptionTest2() {
        doThrow(RuntimeException.class).when(userRepository).deleteById(userId);

        assertThrows(CrudException.class, () -> userService.delete(userId));

        verify(userRepository, times(1)).deleteById(userId);
    }
}
