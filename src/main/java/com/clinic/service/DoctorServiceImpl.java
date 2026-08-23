package com.clinic.service;

import com.clinic.dto.DoctorRequestDto;
import com.clinic.dto.DoctorResponseDto;
import com.clinic.entity.Doctor;
import com.clinic.enums.Specialization;
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.exception.DuplicateEmailException;
import com.clinic.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public DoctorResponseDto createDoctor(DoctorRequestDto requestDto) {
        if (doctorRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Doctor with email " + requestDto.getEmail() + " already exists");
        }

        String doctorId = "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Doctor doctor = Doctor.builder()
                .doctorId(doctorId)
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .specialization(requestDto.getSpecialization())
                .qualification(requestDto.getQualification())
                .experience(requestDto.getExperience())
                .availability(requestDto.getAvailability())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Doctor saved = doctorRepository.save(doctor);
        return mapToResponseDto(saved);
    }

    @Override
    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorResponseDto getDoctorById(String doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + doctorId));
        return mapToResponseDto(doctor);
    }

    @Override
    public DoctorResponseDto updateDoctor(String doctorId, DoctorRequestDto requestDto) {
        Doctor existing = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new DoctorNotFoundException("Doctor not found with id: " + doctorId));

        existing.setName(requestDto.getName());
        existing.setEmail(requestDto.getEmail());
        existing.setPhone(requestDto.getPhone());
        existing.setSpecialization(requestDto.getSpecialization());
        existing.setQualification(requestDto.getQualification());
        existing.setExperience(requestDto.getExperience());
        existing.setAvailability(requestDto.getAvailability());
        existing.setUpdatedAt(LocalDateTime.now());

        Doctor updated = doctorRepository.save(existing);
        return mapToResponseDto(updated);
    }

    @Override
    public void deleteDoctor(String doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException("Doctor not found with id: " + doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }

    @Override
    public List<DoctorResponseDto> getDoctorsBySpecialization(Specialization specialization) {
        return doctorRepository.findBySpecialization(specialization)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDto> searchDoctorsByName(String name) {
        return doctorRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DoctorResponseDto> getAllDoctorsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Doctor> doctorPage = doctorRepository.findAll(pageable);
        return doctorPage.map(this::mapToResponseDto);
    }

    @Override
    public Page<DoctorResponseDto> getDoctorsBySpecializationPaginated(Specialization specialization, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Doctor> doctorPage = doctorRepository.findBySpecialization(specialization, pageable);
        return doctorPage.map(this::mapToResponseDto);
    }

    private DoctorResponseDto mapToResponseDto(Doctor doctor) {
        return DoctorResponseDto.builder()
                .doctorId(doctor.getDoctorId())
                .name(doctor.getName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experience(doctor.getExperience())
                .availability(doctor.getAvailability())
                .createdAt(doctor.getCreatedAt())
                .updatedAt(doctor.getUpdatedAt())
                .build();
    }
}