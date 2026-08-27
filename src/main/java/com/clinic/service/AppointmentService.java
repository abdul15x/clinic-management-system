package com.clinic.service;

import com.clinic.dto.AppointmentRequestDto;
import com.clinic.dto.AppointmentResponseDto;

import java.util.List;

public interface AppointmentService {
    AppointmentResponseDto createAppointment(AppointmentRequestDto requestDto);
    List<AppointmentResponseDto> getAllAppointments();
    AppointmentResponseDto getAppointmentById(String id);
    List<AppointmentResponseDto> getAppointmentsByPatient(String patientId);
    List<AppointmentResponseDto> getAppointmentsByDoctor(String doctorId);
    AppointmentResponseDto updateAppointment(String id, AppointmentRequestDto requestDto);
    AppointmentResponseDto updateStatus(String id, String status);
    void deleteAppointment(String id);
}