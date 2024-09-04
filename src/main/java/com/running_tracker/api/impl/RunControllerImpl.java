package com.running_tracker.api.impl;

import com.running_tracker.api.RunController;
import com.running_tracker.api.dto.request.run.FinishRunRequestDto;
import com.running_tracker.api.dto.request.run.StartRunRequestDto;
import com.running_tracker.api.dto.response.run.RunResponseDto;
import com.running_tracker.api.dto.response.run.StartRunResponseDto;
import com.running_tracker.api.dto.response.user.UserStatisticsDTO;
import com.running_tracker.service.RunService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/runs")
@RequiredArgsConstructor
public class RunControllerImpl implements RunController {

    private final RunService runService;

    @Override
    @PostMapping
    public ResponseEntity<StartRunResponseDto> startRun(@Valid @RequestBody StartRunRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(runService.startRun(requestDto));
    }

    @Override
    @PutMapping("/{id}/finish")
    public ResponseEntity<RunResponseDto> finishRun(@Valid @RequestBody FinishRunRequestDto requestDto, @PathVariable UUID id) {
        return ResponseEntity.ok(runService.finishRun(requestDto, id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RunResponseDto>> getAllRunsForUser(@RequestParam UUID userId,
                                                                  @RequestParam(required = false) LocalDateTime fromDatetime,
                                                                  @RequestParam(required = false) LocalDateTime toDatetime) {
        return ResponseEntity.ok(runService.getAllRunsForUser(userId, fromDatetime, toDatetime));
    }

    @Override
    @GetMapping("/statistics")
    public ResponseEntity<UserStatisticsDTO> getUserStatistics(@RequestParam UUID userId,
                                                               @RequestParam(required = false)
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDatetime,
                                                               @RequestParam(required = false)
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDatetime) {
        return ResponseEntity.ok(runService.getUserStatistics(userId, fromDatetime, toDatetime));
    }
}
