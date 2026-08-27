package com.clinic.dto;

import com.clinic.enums.AppointmentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {

    @NotNull(message = "Patient is required")
    private String patientId;

    @NotNull(message = "Doctor is required")
    private String doctorId;

    @NotNull(message = "Appointment date is required")
    private LocalDate appointmentDate;

    @NotNull(message = "Appointment time is required")
    private LocalTime appointmentTime;

    private AppointmentStatus status;

    @Size(max = 500, message = "Notes must be less than 500 characters")
    private String notes;
}