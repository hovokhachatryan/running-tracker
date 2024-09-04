package com.running_tracker.api.dto.request.run;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class FinishRunRequestDto {

    @NotNull
    private Double finishLatitude;
    @NotNull
    private Double finishLongitude;
    @NotNull
    private LocalDateTime finishDatetime;
    private Double distance;
}
