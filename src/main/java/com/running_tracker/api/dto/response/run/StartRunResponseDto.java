package com.running_tracker.api.dto.response.run;

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
public class StartRunResponseDto {

    private UUID id;
    private Double startLatitude;
    private Double startLongitude;
    private LocalDateTime startDatetime;
}
