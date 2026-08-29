package com.clinic.controller;

import com.clinic.dto.PrescriptionRequestDto;
import com.clinic.dto.PrescriptionResponseDto;
import com.clinic.service.PrescriptionService;
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
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescriptions", description = "Prescription management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PrescriptionRestController {

    private final PrescriptionService prescriptionService;

    @Operation(summary = "Get all prescriptions", description = "Retrieves list of all prescriptions")
    @ApiResponse(responseCode = "200", description = "List of prescriptions returned")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<PrescriptionResponseDto>> getAllPrescriptions() {
        return ResponseEntity.ok(prescriptionService.getAllPrescriptions());
    }

    @Operation(summary = "Get prescription by ID", description = "Retrieves a single prescription by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescription found"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<PrescriptionResponseDto> getPrescriptionById(@PathVariable String id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    @Operation(summary = "Get prescriptions by patient", description = "Retrieves all prescriptions for a specific patient")
    @ApiResponse(responseCode = "200", description = "List of prescriptions returned")
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptionsByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatient(patientId));
    }

    @Operation(summary = "Get prescriptions by doctor", description = "Retrieves all prescriptions by a specific doctor")
    @ApiResponse(responseCode = "200", description = "List of prescriptions returned")
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<PrescriptionResponseDto>> getPrescriptionsByDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByDoctor(doctorId));
    }

    @Operation(summary = "Create prescription", description = "Creates a new prescription (ADMIN, DOCTOR)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Prescription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionResponseDto> createPrescription(@Valid @RequestBody PrescriptionRequestDto requestDto) {
        return new ResponseEntity<>(prescriptionService.createPrescription(requestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update prescription", description = "Updates an existing prescription (ADMIN, DOCTOR)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Prescription updated successfully"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionResponseDto> updatePrescription(@PathVariable String id,
                                                                      @Valid @RequestBody PrescriptionRequestDto requestDto) {
        return ResponseEntity.ok(prescriptionService.updatePrescription(id, requestDto));
    }

    @Operation(summary = "Update prescription status", description = "Changes prescription status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Prescription not found")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<PrescriptionResponseDto> updateStatus(@PathVariable String id,
                                                                @RequestParam String status) {
        return ResponseEntity.ok(prescriptionService.updateStatus(id, status));
    }

    @Operation(summary = "Delete prescription", description = "Deletes a prescription (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Prescription deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePrescription(@PathVariable String id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}