package com.clinic.service;

import com.clinic.dto.DoctorRequestDto;
import com.clinic.dto.DoctorResponseDto;
import com.clinic.entity.Doctor;
import com.clinic.enums.Specialization;
import com.clinic.exception.DoctorNotFoundException;
import com.clinic.exception.DuplicateEmailException;
import com.clinic.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorRequestDto requestDto;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId("mongo-id-456");
        doctor.setDoctorId("DOC-E5F6G7H8");
        doctor.setName("Dr. Sarah Smith");
        doctor.setEmail("sarah@clinic.com");
        doctor.setPhone("03009876543");
        doctor.setSpecialization(Specialization.CARDIOLOGY);
        doctor.setQualification("MBBS, FCPS");
        doctor.setExperience(10);
        doctor.setAvailability("Mon-Fri 9AM-5PM");
        doctor.setCreatedAt(LocalDateTime.now());
        doctor.setUpdatedAt(LocalDateTime.now());

        requestDto = new DoctorRequestDto();
        requestDto.setName("Dr. Sarah Smith");
        requestDto.setEmail("sarah@clinic.com");
        requestDto.setPhone("03009876543");
        requestDto.setSpecialization(Specialization.CARDIOLOGY);
        requestDto.setQualification("MBBS, FCPS");
        requestDto.setExperience(10);
        requestDto.setAvailability("Mon-Fri 9AM-5PM");

        lenient().when(doctorRepository.findById("DOC-E5F6G7H8")).thenReturn(Optional.of(doctor));
        lenient().when(doctorRepository.findByDoctorId("DOC-E5F6G7H8")).thenReturn(Optional.of(doctor));
    }

    @Test
    void createDoctor_Success() {
        when(doctorRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.empty());
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorResponseDto result = doctorService.createDoctor(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Dr. Sarah Smith");
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void createDoctor_DuplicateEmail_ThrowsException() {
        when(doctorRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(doctor));

        assertThrows(DuplicateEmailException.class,
                () -> doctorService.createDoctor(requestDto));
    }

    @Test
    void getAllDoctors_ReturnsList() {
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));

        List<DoctorResponseDto> result = doctorService.getAllDoctors();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDoctorId()).isEqualTo("DOC-E5F6G7H8");
    }

    @Test
    void getAllDoctors_EmptyList_ReturnsEmpty() {
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        List<DoctorResponseDto> result = doctorService.getAllDoctors();

        assertThat(result).isEmpty();
    }

    @Test
    void getDoctorById_Success() {
        DoctorResponseDto result = doctorService.getDoctorById("DOC-E5F6G7H8");

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Dr. Sarah Smith");
    }

    @Test
    void getDoctorById_NotFound_ThrowsException() {
        when(doctorRepository.findById("DOC-FAKE")).thenReturn(Optional.empty());

        DoctorNotFoundException exception = assertThrows(DoctorNotFoundException.class,
                () -> doctorService.getDoctorById("DOC-FAKE"));

        assertThat(exception.getMessage()).contains("not found");
    }

    @Test
    void updateDoctor_Success() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorResponseDto result = doctorService.updateDoctor("DOC-E5F6G7H8", requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getQualification()).isEqualTo("MBBS, FCPS");
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void updateDoctor_NotFound_ThrowsException() {
        when(doctorRepository.findById("DOC-FAKE")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class,
                () -> doctorService.updateDoctor("DOC-FAKE", requestDto));
    }

    @Test
    void deleteDoctor_Success() {
        doNothing().when(doctorRepository).delete(any(Doctor.class));

        doctorService.deleteDoctor("DOC-E5F6G7H8");

        verify(doctorRepository, times(1)).delete(any(Doctor.class));
    }

    @Test
    void deleteDoctor_NotFound_ThrowsException() {
        when(doctorRepository.findById("DOC-FAKE")).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class,
                () -> doctorService.deleteDoctor("DOC-FAKE"));
    }
}