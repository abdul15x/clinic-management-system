package com.clinic.dto;

import com.clinic.enums.BloodGroup;
import com.clinic.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseDto {

    private String patientId;
    private String name;
    private String email;
    private String phone;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String address;
    private BloodGroup bloodGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}