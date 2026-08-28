package com.clinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordRequestDto {

    @NotNull(message = "Patient is required")
    private String patientId;

    @NotNull(message = "Doctor is required")
    private String doctorId;

    @NotBlank(message = "Diagnosis is required")
    @Size(max = 1000, message = "Diagnosis must be less than 1000 characters")
    private String diagnosis;

    @NotBlank(message = "Treatment is required")
    @Size(max = 1000, message = "Treatment must be less than 1000 characters")
    private String treatment;

    @Size(max = 2000, message = "Notes must be less than 2000 characters")
    private String notes;
}