package com.running_tracker.api.dto.response;

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
public class ErrorResponseDto {

    private String title;
    private String details;
    private int statusCode;
    private String resourceURL;

}
