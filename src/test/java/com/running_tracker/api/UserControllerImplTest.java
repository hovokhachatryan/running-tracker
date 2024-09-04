package com.running_tracker.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.running_tracker.TestDataUtil;
import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.user.DeleteUserResponseDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import com.running_tracker.api.impl.UserControllerImpl;
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


class UserControllerImplTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserControllerImpl userController;
    private ObjectMapper objectMapper;

    private UserRequestDto userRequestDto;
    private UserResponseDto userResponseDto;
    private UUID userId;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userId = TestDataUtil.TEST_USER_ID;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());


        userRequestDto = TestDataUtil.createUserRequestDto();
        userResponseDto = TestDataUtil.createUserResponseDto();
    }

    @Test
    void saveUserTest() throws Exception {
        when(userService.save(any(UserRequestDto.class))).thenReturn(userResponseDto);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.firstName").value("Jon"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.sex").value("male"));

        verify(userService, times(1)).save(any(UserRequestDto.class));
    }

    @Test
    void findByIdTest() throws Exception {
        when(userService.findUserDtoById(any(UUID.class))).thenReturn(userResponseDto);

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.firstName").value("Jon"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.sex").value("male"));

        verify(userService, times(1)).findUserDtoById(any(UUID.class));
    }

    @Test
    void findAllTest() throws Exception {
        when(userService.findAll()).thenReturn(Collections.singletonList(userResponseDto));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId.toString()))
                .andExpect(jsonPath("$[0].firstName").value("Jon"))
                .andExpect(jsonPath("$[0].lastName").value("Smith"))
                .andExpect(jsonPath("$[0].sex").value("male"));

        verify(userService, times(1)).findAll();
    }

    @Test
    void updateUserTest() throws Exception {
        when(userService.update(any(UserRequestDto.class), any(UUID.class))).thenReturn(userResponseDto);

        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.firstName").value("Jon"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.sex").value("male"));

        verify(userService, times(1)).update(any(UserRequestDto.class), any(UUID.class));
    }

    @Test
    void deleteUserTest() throws Exception {
        DeleteUserResponseDto deleteResponse = new DeleteUserResponseDto("User deleted successfully");
        when(userService.delete(any(UUID.class))).thenReturn(deleteResponse);

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));

        verify(userService, times(1)).delete(any(UUID.class));
    }
}
