package com.vhg.patientmonitoring.repository;

import com.vhg.patientmonitoring.model.patient.Patient;

import java.util.List;

/**
 * Interface for patient data storage
 */
public interface PatientRepository {
    void addPatient(Patient patient);
    Patient getPatientById(String patientId);
    List<Patient> getAllPatients();
    void updatePatient(Patient patient);
    void removePatient(String patientId);
}