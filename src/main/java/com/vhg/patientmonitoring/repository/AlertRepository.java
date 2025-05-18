package com.vhg.patientmonitoring.repository;

import com.vhg.patientmonitoring.model.alert.Alert;
import com.vhg.patientmonitoring.model.alert.AlertStatus;

import java.util.List;

/**
 * Interface for alert storage and management
 */
public interface AlertRepository {
    void addAlert(Alert alert);
    List<Alert> getPendingAlerts();
    List<Alert> getAlertsForPatient(String patientId);
    void updateAlertStatus(String alertId, AlertStatus newStatus);
}