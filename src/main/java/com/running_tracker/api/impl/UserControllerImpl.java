package com.running_tracker.api.impl;

import com.running_tracker.api.UserController;
import com.running_tracker.api.dto.request.user.UserRequestDto;
import com.running_tracker.api.dto.response.user.DeleteUserResponseDto;
import com.running_tracker.api.dto.response.user.UserResponseDto;
import com.running_tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;


    @Override
    @PostMapping
    public ResponseEntity<UserResponseDto> save(@Valid @RequestBody UserRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(requestDto));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findUserDtoById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        return ResponseEntity.ok(userService.findAll());

    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@Valid @RequestBody UserRequestDto requestDto, @PathVariable UUID id) {
        return ResponseEntity.ok(userService.update(requestDto, id));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteUserResponseDto> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.delete(id));
    }
}
