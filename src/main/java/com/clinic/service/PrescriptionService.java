package com.clinic.service;

import com.clinic.dto.PrescriptionRequestDto;
import com.clinic.dto.PrescriptionResponseDto;

import java.util.List;

public interface PrescriptionService {
    PrescriptionResponseDto createPrescription(PrescriptionRequestDto requestDto);
    List<PrescriptionResponseDto> getAllPrescriptions();
    PrescriptionResponseDto getPrescriptionById(String id);
    List<PrescriptionResponseDto> getPrescriptionsByPatient(String patientId);
    List<PrescriptionResponseDto> getPrescriptionsByDoctor(String doctorId);
    PrescriptionResponseDto updatePrescription(String id, PrescriptionRequestDto requestDto);
    PrescriptionResponseDto updateStatus(String id, String status);
    void deletePrescription(String id);
}