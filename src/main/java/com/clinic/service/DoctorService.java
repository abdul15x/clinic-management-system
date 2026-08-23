package com.clinic.service;

import com.clinic.dto.DoctorRequestDto;
import com.clinic.dto.DoctorResponseDto;
import com.clinic.enums.Specialization;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DoctorService {

    DoctorResponseDto createDoctor(DoctorRequestDto requestDto);

    List<DoctorResponseDto> getAllDoctors();

    DoctorResponseDto getDoctorById(String doctorId);

    DoctorResponseDto updateDoctor(String doctorId, DoctorRequestDto requestDto);

    void deleteDoctor(String doctorId);

    List<DoctorResponseDto> getDoctorsBySpecialization(Specialization specialization);

    List<DoctorResponseDto> searchDoctorsByName(String name);

    Page<DoctorResponseDto> getAllDoctorsPaginated(int page, int size);

    Page<DoctorResponseDto> getDoctorsBySpecializationPaginated(Specialization specialization, int page, int size);
}