package com.clinic.service;

import com.clinic.dto.PatientRequestDto;
import com.clinic.dto.PatientResponseDto;
import com.clinic.entity.Patient;
import com.clinic.exception.DuplicateEmailException;
import com.clinic.exception.PatientNotFoundException;
import com.clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    @Override
    public PatientResponseDto createPatient(PatientRequestDto requestDto) {
        if (patientRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Patient with email " + requestDto.getEmail() + " already exists");
        }

        String patientId = "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Patient patient = Patient.builder()
                .patientId(patientId)
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .gender(requestDto.getGender())
                .dateOfBirth(requestDto.getDateOfBirth())
                .address(requestDto.getAddress())
                .bloodGroup(requestDto.getBloodGroup())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Patient saved = patientRepository.save(patient);
        return mapToResponseDto(saved);
    }

    @Override
    public List<PatientResponseDto> getAllPatients() {
        return patientRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PatientResponseDto getPatientById(String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + patientId));
        return mapToResponseDto(patient);
    }

    @Override
    public PatientResponseDto updatePatient(String patientId, PatientRequestDto requestDto) {
        Patient existing = patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + patientId));

        existing.setName(requestDto.getName());
        existing.setEmail(requestDto.getEmail());
        existing.setPhone(requestDto.getPhone());
        existing.setGender(requestDto.getGender());
        existing.setDateOfBirth(requestDto.getDateOfBirth());
        existing.setAddress(requestDto.getAddress());
        existing.setBloodGroup(requestDto.getBloodGroup());
        existing.setUpdatedAt(LocalDateTime.now());

        Patient updated = patientRepository.save(existing);
        return mapToResponseDto(updated);
    }

    @Override
    public void deletePatient(String patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new PatientNotFoundException("Patient not found with id: " + patientId);
        }
        patientRepository.deleteById(patientId);
    }

    @Override
    public List<PatientResponseDto> searchPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PatientResponseDto> getAllPatientsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll(pageable);
        return patientPage.map(this::mapToResponseDto);
    }

    @Override
    public Page<PatientResponseDto> searchPatientsByNamePaginated(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findByNameContainingIgnoreCase(name, pageable);
        return patientPage.map(this::mapToResponseDto);
    }

    private PatientResponseDto mapToResponseDto(Patient patient) {
        return PatientResponseDto.builder()
                .patientId(patient.getPatientId())
                .name(patient.getName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .gender(patient.getGender())
                .dateOfBirth(patient.getDateOfBirth())
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
}