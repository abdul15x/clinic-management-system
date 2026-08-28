package com.clinic.repository;

import com.clinic.entity.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {
    Optional<Prescription> findByPrescriptionId(String prescriptionId);
    List<Prescription> findByPatientId(String patientId);
    List<Prescription> findByDoctorId(String doctorId);
    List<Prescription> findByAppointmentId(String appointmentId);
}