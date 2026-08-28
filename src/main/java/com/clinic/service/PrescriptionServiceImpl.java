package com.clinic.service;

import com.clinic.dto.PrescriptionRequestDto;
import com.clinic.dto.PrescriptionResponseDto;
import com.clinic.entity.Doctor;
import com.clinic.entity.Patient;
import com.clinic.entity.Prescription;
import com.clinic.enums.PrescriptionStatus;
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.exception.PatientNotFoundException;
import com.clinic.exception.PrescriptionNotFoundException;
import com.clinic.repository.DoctorRepository;
import com.clinic.repository.PatientRepository;
import com.clinic.repository.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public PrescriptionResponseDto createPrescription(PrescriptionRequestDto requestDto) {
        Patient patient = patientRepository.findByPatientId(requestDto.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + requestDto.getPatientId()));

        Doctor doctor = doctorRepository.findByDoctorId(requestDto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + requestDto.getDoctorId()));

        PrescriptionStatus status = requestDto.getStatus() != null ? requestDto.getStatus() : PrescriptionStatus.ACTIVE;

        Prescription prescription = Prescription.builder()
                .prescriptionId("PRS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .appointmentId(requestDto.getAppointmentId())
                .patientId(requestDto.getPatientId())
                .doctorId(requestDto.getDoctorId())
                .medications(requestDto.getMedications())
                .dosage(requestDto.getDosage())
                .instructions(requestDto.getInstructions())
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();

        Prescription saved = prescriptionRepository.save(prescription);

        return mapToResponseDto(saved, patient.getName(), doctor.getName());
    }

    @Override
    public List<PrescriptionResponseDto> getAllPrescriptions() {
        return prescriptionRepository.findAll().stream()
                .map(p -> mapToResponseDto(p,
                        getPatientName(p.getPatientId()),
                        getDoctorName(p.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionResponseDto getPrescriptionById(String id) {
        Prescription prescription = prescriptionRepository.findByPrescriptionId(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with ID: " + id));
        return mapToResponseDto(prescription,
                getPatientName(prescription.getPatientId()),
                getDoctorName(prescription.getDoctorId()));
    }

    @Override
    public List<PrescriptionResponseDto> getPrescriptionsByPatient(String patientId) {
        return prescriptionRepository.findByPatientId(patientId).stream()
                .map(p -> mapToResponseDto(p,
                        getPatientName(p.getPatientId()),
                        getDoctorName(p.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionResponseDto> getPrescriptionsByDoctor(String doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId).stream()
                .map(p -> mapToResponseDto(p,
                        getPatientName(p.getPatientId()),
                        getDoctorName(p.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionResponseDto updatePrescription(String id, PrescriptionRequestDto requestDto) {
        Prescription prescription = prescriptionRepository.findByPrescriptionId(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with ID: " + id));

        Patient patient = patientRepository.findByPatientId(requestDto.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + requestDto.getPatientId()));

        Doctor doctor = doctorRepository.findByDoctorId(requestDto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + requestDto.getDoctorId()));

        prescription.setAppointmentId(requestDto.getAppointmentId());
        prescription.setPatientId(requestDto.getPatientId());
        prescription.setDoctorId(requestDto.getDoctorId());
        prescription.setMedications(requestDto.getMedications());
        prescription.setDosage(requestDto.getDosage());
        prescription.setInstructions(requestDto.getInstructions());
        if (requestDto.getStatus() != null) {
            prescription.setStatus(requestDto.getStatus());
        }

        Prescription saved = prescriptionRepository.save(prescription);
        return mapToResponseDto(saved, patient.getName(), doctor.getName());
    }

    @Override
    public PrescriptionResponseDto updateStatus(String id, String status) {
        Prescription prescription = prescriptionRepository.findByPrescriptionId(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with ID: " + id));

        prescription.setStatus(PrescriptionStatus.valueOf(status));
        Prescription saved = prescriptionRepository.save(prescription);
        return mapToResponseDto(saved,
                getPatientName(saved.getPatientId()),
                getDoctorName(saved.getDoctorId()));
    }

    @Override
    public void deletePrescription(String id) {
        Prescription prescription = prescriptionRepository.findByPrescriptionId(id)
                .orElseThrow(() -> new PrescriptionNotFoundException("Prescription not found with ID: " + id));
        prescriptionRepository.delete(prescription);
    }

    private PrescriptionResponseDto mapToResponseDto(Prescription prescription, String patientName, String doctorName) {
        return PrescriptionResponseDto.builder()
                .prescriptionId(prescription.getPrescriptionId())
                .appointmentId(prescription.getAppointmentId())
                .patientId(prescription.getPatientId())
                .patientName(patientName)
                .doctorId(prescription.getDoctorId())
                .doctorName(doctorName)
                .medications(prescription.getMedications())
                .dosage(prescription.getDosage())
                .instructions(prescription.getInstructions())
                .status(prescription.getStatus())
                .createdAt(prescription.getCreatedAt())
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