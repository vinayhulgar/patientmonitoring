package com.vhg.patientmonitoring.repository;

import com.vhg.patientmonitoring.model.reading.VitalSignReading;
import com.vhg.patientmonitoring.model.reading.VitalSignType;

import java.util.List;

/**
 * Interface for vital sign readings storage
 */
public interface ReadingRepository {
    void addReading(VitalSignReading reading);
    List<VitalSignReading> getReadingsForPatient(String patientId, int timeWindowMinutes);
    List<VitalSignReading> getReadingsForPatientByType(String patientId, VitalSignType type, int timeWindowMinutes);
    void purgeOldReadings(int daysToKeep);
}