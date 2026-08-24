package com.clinic.dto;

import com.clinic.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String userId;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}