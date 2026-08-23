package com.clinic.controller;

import com.clinic.dto.DoctorRequestDto;
import com.clinic.dto.DoctorResponseDto;
import com.clinic.enums.Specialization;
import com.clinic.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;

    @PostMapping
    public ResponseEntity<DoctorResponseDto> createDoctor(@Valid @RequestBody DoctorRequestDto requestDto) {
        return new ResponseEntity<>(doctorService.createDoctor(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllDoctors(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page != null && size != null) {
            return ResponseEntity.ok(doctorService.getAllDoctorsPaginated(page, size));
        }
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> getDoctorById(@PathVariable String id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDto> updateDoctor(
            @PathVariable String id,
            @Valid @RequestBody DoctorRequestDto requestDto) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable String id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/specialization")
    public ResponseEntity<?> getDoctorsBySpecialization(
            @RequestParam Specialization specialization,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page != null && size != null) {
            return ResponseEntity.ok(doctorService.getDoctorsBySpecializationPaginated(specialization, page, size));
        }
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization(specialization));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorResponseDto>> searchDoctors(@RequestParam String name) {
        return ResponseEntity.ok(doctorService.searchDoctorsByName(name));
    }
}