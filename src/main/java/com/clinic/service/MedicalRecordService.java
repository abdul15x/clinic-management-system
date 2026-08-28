package com.clinic.service;

import com.clinic.dto.MedicalRecordRequestDto;
import com.clinic.dto.MedicalRecordResponseDto;

import java.util.List;

public interface MedicalRecordService {
    MedicalRecordResponseDto createMedicalRecord(MedicalRecordRequestDto requestDto);
    List<MedicalRecordResponseDto> getAllMedicalRecords();
    MedicalRecordResponseDto getMedicalRecordById(String id);
    List<MedicalRecordResponseDto> getMedicalRecordsByPatient(String patientId);
    List<MedicalRecordResponseDto> getMedicalRecordsByDoctor(String doctorId);
    MedicalRecordResponseDto updateMedicalRecord(String id, MedicalRecordRequestDto requestDto);
    void deleteMedicalRecord(String id);
}