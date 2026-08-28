package com.clinic.repository;

import com.clinic.entity.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, String> {
    Optional<MedicalRecord> findByRecordId(String recordId);
    List<MedicalRecord> findByPatientId(String patientId);
    List<MedicalRecord> findByDoctorId(String doctorId);
}