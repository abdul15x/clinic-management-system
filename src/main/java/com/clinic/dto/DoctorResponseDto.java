package com.clinic.dto;

import com.clinic.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDto {

    private String doctorId;
    private String name;
    private String email;
    private String phone;
    private Specialization specialization;
    private String qualification;
    private Integer experience;
    private String availability;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}