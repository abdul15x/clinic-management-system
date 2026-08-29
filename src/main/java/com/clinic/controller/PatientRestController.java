package com.clinic.controller;

import com.clinic.dto.PatientRequestDto;
import com.clinic.dto.PatientResponseDto;
import com.clinic.service.PatientService;
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
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "Patient management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class PatientRestController {

    private final PatientService patientService;

    @Operation(summary = "Get all patients", description = "Retrieves list of all patients")
    @ApiResponse(responseCode = "200", description = "List of patients returned")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<PatientResponseDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @Operation(summary = "Get patient by ID", description = "Retrieves a single patient by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<PatientResponseDto> getPatientById(@PathVariable String id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @Operation(summary = "Create patient", description = "Creates a new patient record")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto requestDto) {
        return new ResponseEntity<>(patientService.createPatient(requestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update patient", description = "Updates an existing patient record")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable String id,
                                                            @Valid @RequestBody PatientRequestDto requestDto) {
        return ResponseEntity.ok(patientService.updatePatient(id, requestDto));
    }

    @Operation(summary = "Delete patient", description = "Deletes a patient record (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - ADMIN only"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}