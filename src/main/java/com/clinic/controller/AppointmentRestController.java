package com.clinic.controller;

import com.clinic.dto.AppointmentRequestDto;
import com.clinic.dto.AppointmentResponseDto;
import com.clinic.service.AppointmentService;
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
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@Tag(name = "Appointments", description = "Appointment booking and management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class AppointmentRestController {

    private final AppointmentService appointmentService;

    @Operation(summary = "Get all appointments", description = "Retrieves list of all appointments")
    @ApiResponse(responseCode = "200", description = "List of appointments returned")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @Operation(summary = "Get appointment by ID", description = "Retrieves a single appointment by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment found"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable String id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @Operation(summary = "Get appointments by patient", description = "Retrieves all appointments for a specific patient")
    @ApiResponse(responseCode = "200", description = "List of appointments returned")
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
    }

    @Operation(summary = "Get appointments by doctor", description = "Retrieves all appointments for a specific doctor")
    @ApiResponse(responseCode = "200", description = "List of appointments returned")
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByDoctor(@PathVariable String doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
    }

    @Operation(summary = "Create appointment", description = "Books a new appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Appointment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponseDto> createAppointment(@Valid @RequestBody AppointmentRequestDto requestDto) {
        return new ResponseEntity<>(appointmentService.createAppointment(requestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Update appointment", description = "Updates an existing appointment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Appointment updated successfully"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponseDto> updateAppointment(@PathVariable String id,
                                                                    @Valid @RequestBody AppointmentRequestDto requestDto) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, requestDto));
    }

    @Operation(summary = "Update appointment status", description = "Changes appointment status (SCHEDULED, COMPLETED, CANCELLED)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'RECEPTIONIST')")
    public ResponseEntity<AppointmentResponseDto> updateStatus(@PathVariable String id,
                                                               @RequestParam String status) {
        return ResponseEntity.ok(appointmentService.updateStatus(id, status));
    }

    @Operation(summary = "Delete appointment", description = "Deletes an appointment (ADMIN only)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Appointment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}