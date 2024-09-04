package com.running_tracker.api.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserStatisticsDTO {

    private Integer totalRuns;
    private Double totalDistance;
    private Double averageSpeed;
}
