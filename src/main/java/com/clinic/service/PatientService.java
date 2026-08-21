package com.clinic.service;

import com.clinic.dto.PatientRequestDto;
import com.clinic.dto.PatientResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PatientService {

    PatientResponseDto createPatient(PatientRequestDto requestDto);

    List<PatientResponseDto> getAllPatients();

    PatientResponseDto getPatientById(String patientId);

    PatientResponseDto updatePatient(String patientId, PatientRequestDto requestDto);

    void deletePatient(String patientId);

    List<PatientResponseDto> searchPatientsByName(String name);

    Page<PatientResponseDto> getAllPatientsPaginated(int page, int size);

    Page<PatientResponseDto> searchPatientsByNamePaginated(String name, int page, int size);
}