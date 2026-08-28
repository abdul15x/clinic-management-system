package com.clinic.service;

import com.clinic.dto.MedicalRecordRequestDto;
import com.clinic.dto.MedicalRecordResponseDto;
import com.clinic.entity.Doctor;
import com.clinic.entity.MedicalRecord;
import com.clinic.entity.Patient;
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.exception.MedicalRecordNotFoundException;
import com.clinic.exception.PatientNotFoundException;
import com.clinic.repository.DoctorRepository;
import com.clinic.repository.MedicalRecordRepository;
import com.clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public MedicalRecordResponseDto createMedicalRecord(MedicalRecordRequestDto requestDto) {
        Patient patient = patientRepository.findByPatientId(requestDto.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + requestDto.getPatientId()));

        Doctor doctor = doctorRepository.findByDoctorId(requestDto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + requestDto.getDoctorId()));

        MedicalRecord record = MedicalRecord.builder()
                .recordId("REC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .patientId(requestDto.getPatientId())
                .doctorId(requestDto.getDoctorId())
                .diagnosis(requestDto.getDiagnosis())
                .treatment(requestDto.getTreatment())
                .notes(requestDto.getNotes())
                .createdAt(LocalDateTime.now())
                .build();

        MedicalRecord saved = medicalRecordRepository.save(record);

        return mapToResponseDto(saved, patient.getName(), doctor.getName());
    }

    @Override
    public List<MedicalRecordResponseDto> getAllMedicalRecords() {
        return medicalRecordRepository.findAll().stream()
                .map(r -> mapToResponseDto(r,
                        getPatientName(r.getPatientId()),
                        getDoctorName(r.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordResponseDto getMedicalRecordById(String id) {
        MedicalRecord record = medicalRecordRepository.findByRecordId(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));
        return mapToResponseDto(record,
                getPatientName(record.getPatientId()),
                getDoctorName(record.getDoctorId()));
    }

    @Override
    public List<MedicalRecordResponseDto> getMedicalRecordsByPatient(String patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream()
                .map(r -> mapToResponseDto(r,
                        getPatientName(r.getPatientId()),
                        getDoctorName(r.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicalRecordResponseDto> getMedicalRecordsByDoctor(String doctorId) {
        return medicalRecordRepository.findByDoctorId(doctorId).stream()
                .map(r -> mapToResponseDto(r,
                        getPatientName(r.getPatientId()),
                        getDoctorName(r.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public MedicalRecordResponseDto updateMedicalRecord(String id, MedicalRecordRequestDto requestDto) {
        MedicalRecord record = medicalRecordRepository.findByRecordId(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));

        Patient patient = patientRepository.findByPatientId(requestDto.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + requestDto.getPatientId()));

        Doctor doctor = doctorRepository.findByDoctorId(requestDto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + requestDto.getDoctorId()));

        record.setPatientId(requestDto.getPatientId());
        record.setDoctorId(requestDto.getDoctorId());
        record.setDiagnosis(requestDto.getDiagnosis());
        record.setTreatment(requestDto.getTreatment());
        record.setNotes(requestDto.getNotes());

        MedicalRecord saved = medicalRecordRepository.save(record);
        return mapToResponseDto(saved, patient.getName(), doctor.getName());
    }

    @Override
    public void deleteMedicalRecord(String id) {
        MedicalRecord record = medicalRecordRepository.findByRecordId(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Medical record not found with ID: " + id));
        medicalRecordRepository.delete(record);
    }

    private MedicalRecordResponseDto mapToResponseDto(MedicalRecord record, String patientName, String doctorName) {
        return MedicalRecordResponseDto.builder()
                .recordId(record.getRecordId())
                .patientId(record.getPatientId())
                .patientName(patientName)
                .doctorId(record.getDoctorId())
                .doctorName(doctorName)
                .diagnosis(record.getDiagnosis())
                .treatment(record.getTreatment())
                .notes(record.getNotes())
                .createdAt(record.getCreatedAt())
                .build();
    }

    private String getPatientName(String patientId) {
        return patientRepository.findByPatientId(patientId)
                .map(Patient::getName)
                .orElse("Unknown");
    }

    private String getDoctorName(String doctorId) {
        return doctorRepository.findByDoctorId(doctorId)
                .map(Doctor::getName)
                .orElse("Unknown");
    }
}