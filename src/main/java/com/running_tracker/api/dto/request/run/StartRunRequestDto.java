package com.running_tracker.api.dto.request.run;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class StartRunRequestDto {

    @NotNull
    private UUID userId;
    @NotNull
    private Double startLatitude;
    @NotNull
    private Double startLongitude;
    @NotNull
    private LocalDateTime startDatetime;

}
