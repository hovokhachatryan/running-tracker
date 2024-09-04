package com.running_tracker.api.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserResponseDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String sex;
}
