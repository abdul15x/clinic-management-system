package com.clinic.dto;

import com.clinic.enums.PrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponseDto {

    private String prescriptionId;
    private String appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private String doctorName;
    private String medications;
    private String dosage;
    private String instructions;
    private PrescriptionStatus status;
    private LocalDateTime createdAt;
}