package com.clinic.controller;

import com.clinic.dto.MedicalRecordRequestDto;
import com.clinic.dto.MedicalRecordResponseDto;
import com.clinic.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records", description = "Medical record management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class MedicalRecordRestController {

    private final MedicalRecordService medicalRecordService;

    @Operation(summary = "Get all medical records", description = "Retrieves list of all medical records")
    @ApiResponse(responseCode = "200", description = "List of medical records returned")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<MedicalRecordResponseDto>> getAllMedicalRecords() {
        return ResponseEntity.ok(medicalRecordService.getAllMedicalRecords());
    }

    @Operation(summary = "Get medical record by ID", description = "Retrieves a single medical record by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medical record found"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<MedicalRecordResponseDto> getMedicalRecordById(@PathVariable String id) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordById(id));
    }

    @Operation(summary = "Get records by patient", description = "Retrieves all medical records for a specific patient")
    @ApiResponse(responseCode = "200", description = "List of medical records returned")
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<MedicalRecordResponseDto>> getMedicalRecordsByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByPatient(patientId));
    }

    @Operation(summary = "Get records by doctor", description = "Retrieves all medical records by a specific doctor")
    @ApiResponse(responseCode = "200", description = "List of medical records returned")
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<MedicalRecordResponseDto>> getMedicalRecordsByDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(medicalRecordService.getMedicalRecordsByDoctor(doctorId));
    }

    @Operation(summary = "Create medical record", description = "Creates a new medical record (ADMIN, DOCTOR)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medical record created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<MedicalRecordResponseDto> createMedicalRecord(@Valid @RequestBody MedicalRecordRequestDto requestDto) {
        return new ResponseEntity<>(medicalRecordService.createMedicalRecord(requestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update medical record", description = "Updates an existing medical record (ADMIN, DOCTOR)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medical record updated successfully"),
            @ApiResponse(responseCode = "404", description = "Medical record not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<MedicalRecordResponseDto> updateMedicalRecord(@PathVariable String id,
                                                                        @Valid @RequestBody MedicalRecordRequestDto requestDto) {
        return ResponseEntity.ok(medicalRecordService.updateMedicalRecord(id, requestDto));
    }

    @Operation(summary = "Delete medical record", description = "Deletes a medical record (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Medical record deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}