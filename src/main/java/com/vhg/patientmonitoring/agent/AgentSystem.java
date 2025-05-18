package com.vhg.patientmonitoring.agent;

import com.vhg.patientmonitoring.model.alert.Alert;
import com.vhg.patientmonitoring.model.patient.Patient;
import com.vhg.patientmonitoring.model.reading.VitalSignReading;
import com.vhg.patientmonitoring.model.reading.VitalSignType;

import java.util.List;
import java.util.Map;

/**
 * Core agentic AI interface
 */
public interface AgentSystem {
    void processPatientData(String patientId);
    void analyzeTrends(String patientId, VitalSignType type, int timeWindowMinutes);
    List<Alert> detectAnomalies(List<VitalSignReading> readings);
    void learnPatientBaselines(String patientId);
    Decision makeDecision(Patient patient, List<VitalSignReading> readings, Map<String, Object> context);
}