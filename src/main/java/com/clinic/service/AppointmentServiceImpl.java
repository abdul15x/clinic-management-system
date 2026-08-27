package com.clinic.service;

import com.clinic.dto.AppointmentRequestDto;
import com.clinic.dto.AppointmentResponseDto;
import com.clinic.entity.Appointment;
import com.clinic.entity.Doctor;
import com.clinic.entity.Patient;
import com.clinic.enums.AppointmentStatus;
import com.clinic.exception.AppointmentNotFoundException;
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.exception.PatientNotFoundException;
import com.clinic.repository.AppointmentRepository;
import com.clinic.repository.DoctorRepository;
import com.clinic.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto requestDto) {
        Patient patient = (Patient) patientRepository.findByPatientId(requestDto.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + requestDto.getPatientId()));

        Doctor doctor = doctorRepository.findByDoctorId(requestDto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + requestDto.getDoctorId()));

        AppointmentStatus status = requestDto.getStatus() != null ? requestDto.getStatus() : AppointmentStatus.SCHEDULED;

        Appointment appointment = Appointment.builder()
                .appointmentId("APT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .patientId(requestDto.getPatientId())
                .doctorId(requestDto.getDoctorId())
                .appointmentDate(requestDto.getAppointmentDate())
                .appointmentTime(requestDto.getAppointmentTime())
                .status(status)
                .notes(requestDto.getNotes())
                .createdAt(LocalDateTime.now())
                .build();

        Appointment saved = appointmentRepository.save(appointment);

        return mapToResponseDto(saved, patient.getName(), doctor.getName());
    }

    @Override
    public List<AppointmentResponseDto> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(a -> mapToResponseDto(a,
                        getPatientName(a.getPatientId()),
                        getDoctorName(a.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto getAppointmentById(String id) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + id));
        return mapToResponseDto(appointment,
                getPatientName(appointment.getPatientId()),
                getDoctorName(appointment.getDoctorId()));
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByPatient(String patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(a -> mapToResponseDto(a,
                        getPatientName(a.getPatientId()),
                        getDoctorName(a.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentResponseDto> getAppointmentsByDoctor(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(a -> mapToResponseDto(a,
                        getPatientName(a.getPatientId()),
                        getDoctorName(a.getDoctorId())))
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto updateAppointment(String id, AppointmentRequestDto requestDto) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + id));

        Patient patient = (Patient) patientRepository.findByPatientId(requestDto.getPatientId())
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + requestDto.getPatientId()));

        Doctor doctor = (Doctor) doctorRepository.findByDoctorId(requestDto.getDoctorId())
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with ID: " + requestDto.getDoctorId()));

        appointment.setPatientId(requestDto.getPatientId());
        appointment.setDoctorId(requestDto.getDoctorId());
        appointment.setAppointmentDate(requestDto.getAppointmentDate());
        appointment.setAppointmentTime(requestDto.getAppointmentTime());
        if (requestDto.getStatus() != null) {
            appointment.setStatus(requestDto.getStatus());
        }
        appointment.setNotes(requestDto.getNotes());

        Appointment saved = appointmentRepository.save(appointment);
        return mapToResponseDto(saved, patient.getName(), doctor.getName());
    }

    @Override
    public AppointmentResponseDto updateStatus(String id, String status) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + id));

        appointment.setStatus(AppointmentStatus.valueOf(status));
        Appointment saved = appointmentRepository.save(appointment);
        return mapToResponseDto(saved,
                getPatientName(saved.getPatientId()),
                getDoctorName(saved.getDoctorId()));
    }

    @Override
    public void deleteAppointment(String id) {
        Appointment appointment = appointmentRepository.findByAppointmentId(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found with ID: " + id));
        appointmentRepository.delete(appointment);
    }

    private AppointmentResponseDto mapToResponseDto(Appointment appointment, String patientName, String doctorName) {
        return AppointmentResponseDto.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientId(appointment.getPatientId())
                .patientName(patientName)
                .doctorId(appointment.getDoctorId())
                .doctorName(doctorName)
                .appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getStatus())
                .notes(appointment.getNotes())
                .createdAt(appointment.getCreatedAt())
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