
        package com.clinic.service;

import com.clinic.dto.PatientRequestDto;
import com.clinic.dto.PatientResponseDto;
import com.clinic.entity.Patient;
import com.clinic.enums.BloodGroup;
import com.clinic.enums.Gender;
import com.clinic.exception.DuplicateEmailException;
import com.clinic.exception.PatientNotFoundException;
import com.clinic.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
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
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientRequestDto requestDto;

    @BeforeEach
    void setUp() {

        patient = new Patient();
        patient.setId("mongo-id-123");
        patient.setPatientId("PAT-A1B2C3D4");
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setPhone("03001234567");
        patient.setGender(Gender.MALE);
        patient.setDateOfBirth(LocalDate.of(1990, 5, 15));
        patient.setAddress("123 Main St");
        patient.setBloodGroup(BloodGroup.A_POSITIVE);
        patient.setCreatedAt(LocalDateTime.now());
        patient.setUpdatedAt(LocalDateTime.now());

        requestDto = new PatientRequestDto();
        requestDto.setName("John Doe");
        requestDto.setEmail("john@example.com");
        requestDto.setPhone("03001234567");
        requestDto.setGender(Gender.MALE);
        requestDto.setDateOfBirth(LocalDate.of(1990, 5, 15));
        requestDto.setAddress("123 Main St");
        requestDto.setBloodGroup(BloodGroup.A_POSITIVE);

        lenient().when(patientRepository.findById("PAT-A1B2C3D4"))
                .thenReturn(Optional.of(patient));

        lenient().when(patientRepository.findByPatientId("PAT-A1B2C3D4"))
                .thenReturn(Optional.of(patient));
    }

    @Test
    void createPatient_Success() {

        when(patientRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.empty());

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(patient);

        PatientResponseDto result =
                patientService.createPatient(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");

        verify(patientRepository, times(1))
                .save(any(Patient.class));
    }

    @Test
    void createPatient_DuplicateEmail_ThrowsException() {

        when(patientRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.of(patient));

        assertThrows(
                DuplicateEmailException.class,
                () -> patientService.createPatient(requestDto)
        );

        verify(
                patientRepository,
                never()
        ).save(any(Patient.class));
    }

    @Test
    void getAllPatients_ReturnsList() {

        when(patientRepository.findAll())
                .thenReturn(List.of(patient));

        List<PatientResponseDto> result =
                patientService.getAllPatients();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName())
                .isEqualTo("John Doe");
    }

    @Test
    void getAllPatients_EmptyList_ReturnsEmpty() {

        when(patientRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<PatientResponseDto> result =
                patientService.getAllPatients();

        assertThat(result).isEmpty();
    }

    @Test
    void getPatientById_Success() {

        PatientResponseDto result =
                patientService.getPatientById("PAT-A1B2C3D4");

        assertThat(result).isNotNull();
        assertThat(result.getPatientId())
                .isEqualTo("PAT-A1B2C3D4");
    }

    @Test
    void getPatientById_NotFound_ThrowsException() {

        when(patientRepository.findById("PAT-FAKE"))
                .thenReturn(Optional.empty());

        PatientNotFoundException exception =
                assertThrows(
                        PatientNotFoundException.class,
                        () -> patientService.getPatientById("PAT-FAKE")
                );

        assertThat(exception.getMessage())
                .contains("not found");
    }

    @Test
    void updatePatient_Success() {

        when(patientRepository.save(any(Patient.class)))
                .thenReturn(patient);

        PatientResponseDto result =
                patientService.updatePatient(
                        "PAT-A1B2C3D4",
                        requestDto
                );

        assertThat(result).isNotNull();
        assertThat(result.getName())
                .isEqualTo("John Doe");

        verify(
                patientRepository,
                times(1)
        ).save(any(Patient.class));
    }

    @Test
    void updatePatient_NotFound_ThrowsException() {

        when(patientRepository.findById("PAT-FAKE"))
                .thenReturn(Optional.empty());

        assertThrows(
                PatientNotFoundException.class,
                () -> patientService.updatePatient(
                        "PAT-FAKE",
                        requestDto
                )
        );
    }

    @Test
    void deletePatient_Success() {

        // Patient exists
        when(patientRepository.existsById("PAT-A1B2C3D4"))
                .thenReturn(true);

        // Delete patient
        doNothing()
                .when(patientRepository)
                .deleteById("PAT-A1B2C3D4");

        patientService.deletePatient("PAT-A1B2C3D4");

        verify(
                patientRepository,
                times(1)
        ).existsById("PAT-A1B2C3D4");

        verify(
                patientRepository,
                times(1)
        ).deleteById("PAT-A1B2C3D4");
    }

    @Test
    void deletePatient_NotFound_ThrowsException() {

        // Patient does not exist
        when(patientRepository.existsById("PAT-FAKE"))
                .thenReturn(false);

        assertThrows(
                PatientNotFoundException.class,
                () -> patientService.deletePatient("PAT-FAKE")
        );

        // Delete should NOT happen
        verify(
                patientRepository,
                never()
        ).deleteById("PAT-FAKE");
    }

    @Test
    void searchPatientsByName_ReturnsMatchingPatients() {

        when(patientRepository
                .findByNameContainingIgnoreCase("John"))
                .thenReturn(List.of(patient));

        List<PatientResponseDto> result =
                patientService.searchPatientsByName("John");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName())
                .isEqualTo("John Doe");
    }
}
