package com.clinic.repository;

import com.clinic.entity.Doctor;
import com.clinic.enums.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

    Optional<Doctor> findByEmail(String email);

    List<Doctor> findBySpecialization(Specialization specialization);

    Page<Doctor> findBySpecialization(Specialization specialization, Pageable pageable);

    List<Doctor> findByNameContainingIgnoreCase(String name);

    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Doctor> findAll(Pageable pageable);
}