package com.clinic.service;

import com.clinic.dto.PatientRequestDto;
import com.clinic.dto.PatientResponseDto;
import com.clinic.entity.Patient;
import com.clinic.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public PatientResponseDto createPatient(PatientRequestDto requestDto) {
        String patientId = "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setName(requestDto.getName());
        patient.setEmail(requestDto.getEmail());
        patient.setPhone(requestDto.getPhone());
        patient.setGender(requestDto.getGender());
        patient.setDateOfBirth(requestDto.getDateOfBirth());
        patient.setAddress(requestDto.getAddress());
        patient.setBloodGroup(requestDto.getBloodGroup());

        Patient saved = patientRepository.save(patient);
        return mapToResponseDto(saved);
    }

    @Override
    public List<PatientResponseDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDto> responseList = new ArrayList<>();
        for (Patient patient : patients) {
            responseList.add(mapToResponseDto(patient));
        }
        return responseList;
    }

    @Override
    public PatientResponseDto getPatientById(String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));
        return mapToResponseDto(patient);
    }

    @Override
    public PatientResponseDto updatePatient(String patientId, PatientRequestDto requestDto) {
        Patient existing = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        existing.setName(requestDto.getName());
        existing.setEmail(requestDto.getEmail());
        existing.setPhone(requestDto.getPhone());
        existing.setGender(requestDto.getGender());
        existing.setDateOfBirth(requestDto.getDateOfBirth());
        existing.setAddress(requestDto.getAddress());
        existing.setBloodGroup(requestDto.getBloodGroup());

        Patient updated = patientRepository.save(existing);
        return mapToResponseDto(updated);
    }

    @Override
    public void deletePatient(String patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new RuntimeException("Patient not found with id: " + patientId);
        }
        patientRepository.deleteById(patientId);
    }

    @Override
    public List<PatientResponseDto> searchPatientsByName(String name) {
        List<Patient> patients = patientRepository.findByNameContainingIgnoreCase(name);
        List<PatientResponseDto> responseList = new ArrayList<>();
        for (Patient patient : patients) {
            responseList.add(mapToResponseDto(patient));
        }
        return responseList;
    }

    private PatientResponseDto mapToResponseDto(Patient patient) {
        PatientResponseDto dto = new PatientResponseDto();
        dto.setPatientId(patient.getPatientId());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setPhone(patient.getPhone());
        dto.setGender(patient.getGender());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setAddress(patient.getAddress());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setUpdatedAt(patient.getUpdatedAt());
        return dto;
    }
}