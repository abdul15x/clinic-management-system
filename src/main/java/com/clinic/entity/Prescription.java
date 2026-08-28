package com.clinic.entity;

import com.clinic.enums.PrescriptionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "prescriptions")
public class Prescription {

    @Id
    private String id;
    private String prescriptionId;
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private String medications;
    private String dosage;
    private String instructions;
    private PrescriptionStatus status;
    private LocalDateTime createdAt;
}