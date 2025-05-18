package com.vhg.patientmonitoring.core;

import com.vhg.patientmonitoring.agent.AIAgentSystem;
import com.vhg.patientmonitoring.agent.AgentSystem;
import com.vhg.patientmonitoring.device.SensorDevice;
import com.vhg.patientmonitoring.model.alert.Alert;
import com.vhg.patientmonitoring.model.patient.Patient;
import com.vhg.patientmonitoring.model.reading.VitalSignReading;
import com.vhg.patientmonitoring.model.reading.VitalSignType;
import com.vhg.patientmonitoring.repository.AlertRepository;
import com.vhg.patientmonitoring.repository.PatientRepository;
import com.vhg.patientmonitoring.repository.ReadingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.vhg.patientmonitoring.model.alert.AlertSeverity.*;
import static java.text.DateFormat.MEDIUM;

/**
 * Main class for the Patient Monitoring System
 */
public class PatientMonitoringSystem {
    private PatientRepository patientRepository;
    private ReadingRepository readingRepository;
    private AlertRepository alertRepository;
    private List<SensorDevice> connectedDevices;
    private AgentSystem agentSystem;
    private NotificationService notificationService;
    private ExecutorService executorService;
    private boolean running;

    public PatientMonitoringSystem() {
        // Initialize repositories
        // In a real application, these would be implementations backed by a database

        this.connectedDevices = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(10);
        this.notificationService = new NotificationService();
    }

    public void initialize() {
        // Initialize the system components
        this.agentSystem = new AIAgentSystem(this);

        // Connect to all available devices
        connectToDevices();

        // Start the monitoring threads
        this.running = true;
        startMonitoring();
    }

    private void connectToDevices() {
        // Discover and connect to available sensor devices
        // Implementation details...
    }

    private void startMonitoring() {
        // Start the monitoring loop for each patient
        List<Patient> patients = patientRepository.getAllPatients();

        for (Patient patient : patients) {
            executorService.submit(() -> monitorPatient(String.valueOf(patient.getPatientId())));
        }
    }

    private void monitorPatient(String patientId) {
        while (running) {
            try {
                // Collect readings from all relevant devices
                collectReadings(patientId);

                // Process the patient data through the AI agent
                agentSystem.processPatientData(patientId);

                // Wait before the next monitoring cycle
                Thread.sleep(5000); // 5 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                // Log error
                System.err.println("Error monitoring patient " + patientId + ": " + e.getMessage());
            }
        }
    }

    private void collectReadings(String patientId) {
        // Collect readings from all devices for this patient
        for (SensorDevice device : connectedDevices) {
            try {
                VitalSignType type = mapDeviceToVitalSignType(device);
                if (type != null) {
                    VitalSignReading reading = device.takeReading(patientId, type);
                    readingRepository.addReading(reading);
                }
            } catch (Exception e) {
                // Log device error
                System.err.println("Error collecting reading from device " + device.getDeviceId() + ": " + e.getMessage());
            }
        }
    }

    private VitalSignType mapDeviceToVitalSignType(SensorDevice device) {
        // Map a device to the type of vital sign it monitors
        String deviceType = device.getDeviceType().toLowerCase();

        if (deviceType.contains("heart") || deviceType.contains("pulse")) {
            return VitalSignType.HEART_RATE;
        } else if (deviceType.contains("pressure")) {
            return VitalSignType.BLOOD_PRESSURE;
        } else if (deviceType.contains("oxygen") || deviceType.contains("o2")) {
            return VitalSignType.BLOOD_OXYGEN;
        } else if (deviceType.contains("temp")) {
            return VitalSignType.TEMPERATURE;
        }
        // Add mappings for other device types

        return null;
    }

    public void shutdown() {
        this.running = false;
        this.executorService.shutdown();

        // Disconnect from all devices
        for (SensorDevice device : connectedDevices) {
            try {
                device.disconnect();
            } catch (Exception e) {
                // Log error
                System.err.println("Error disconnecting from device " + device.getDeviceId() + ": " + e.getMessage());
            }
        }
    }

    public void handleAlert(Alert alert) {
        // Store the alert
        alertRepository.addAlert(alert);

        // Send notification based on alert severity
        switch (alert.getSeverity()) {
            case CRITICAL:
            case HIGH:
                // Immediate notification to medical staff
                notificationService.sendUrgentNotification(alert);
                break;
            case MEDIUM:
                // Standard notification
                notificationService.sendStandardNotification(alert);
                break;
            case LOW:
                // Just log the alert
                break;
        }
    }

    // Getters for repositories

    public PatientRepository getPatientRepository() {
        return patientRepository;
    }

    public ReadingRepository getReadingRepository() {
        return readingRepository;
    }

    public AlertRepository getAlertRepository() {
        return alertRepository;
    }
}