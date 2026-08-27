package com.clinic.repository;

import com.clinic.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String> {

    Optional<Patient> findByPatientId(String patientId);

    Optional<Patient> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Patient> findByNameContainingIgnoreCase(String name);

    Page<Patient> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Patient> findAll(Pageable pageable);
}