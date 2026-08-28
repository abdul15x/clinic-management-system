package com.clinic.dto;

import com.clinic.enums.PrescriptionStatus;
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
public class PrescriptionRequestDto {

    @NotNull(message = "Appointment is required")
    private String appointmentId;

    @NotNull(message = "Patient is required")
    private String patientId;

    @NotNull(message = "Doctor is required")
    private String doctorId;

    @NotBlank(message = "Medications are required")
    @Size(max = 1000, message = "Medications must be less than 1000 characters")
    private String medications;

    @NotBlank(message = "Dosage is required")
    @Size(max = 500, message = "Dosage must be less than 500 characters")
    private String dosage;

    @Size(max = 2000, message = "Instructions must be less than 2000 characters")
    private String instructions;

    private PrescriptionStatus status;
}