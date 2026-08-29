package com.clinic.controller;

import com.clinic.dto.DoctorRequestDto;
import com.clinic.dto.DoctorResponseDto;
import com.clinic.service.DoctorService;
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
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Doctor management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class DoctorRestController {

    private final DoctorService doctorService;

    @Operation(summary = "Get all doctors", description = "Retrieves list of all doctors")
    @ApiResponse(responseCode = "200", description = "List of doctors returned")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @Operation(summary = "Get doctor by ID", description = "Retrieves a single doctor by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable String id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @Operation(summary = "Create doctor", description = "Creates a new doctor record (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Doctor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied - ADMIN only")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDto> createDoctor(@Valid @RequestBody DoctorRequestDto requestDto) {
        return new ResponseEntity<>(doctorService.createDoctor(requestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update doctor", description = "Updates an existing doctor record (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Doctor updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - ADMIN only"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DoctorResponseDto> updateDoctor(@PathVariable String id,
                                                          @Valid @RequestBody DoctorRequestDto requestDto) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, requestDto));
    }

    @Operation(summary = "Delete doctor", description = "Deletes a doctor record (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Doctor deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied - ADMIN only"),
            @ApiResponse(responseCode = "404", description = "Doctor not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable String id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}