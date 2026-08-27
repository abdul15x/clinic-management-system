package com.clinic.service;

import com.clinic.dto.DoctorRequestDto;
import com.clinic.dto.DoctorResponseDto;

import java.util.List;

public interface DoctorService {
    DoctorResponseDto createDoctor(DoctorRequestDto requestDto);
    List<DoctorResponseDto> getAllDoctors();
    DoctorResponseDto getDoctorById(String doctorId);
    DoctorResponseDto updateDoctor(String doctorId, DoctorRequestDto requestDto);
    void deleteDoctor(String doctorId);
}