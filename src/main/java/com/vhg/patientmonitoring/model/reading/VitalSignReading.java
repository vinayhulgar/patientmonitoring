package com.vhg.patientmonitoring.model.reading;

import com.vhg.patientmonitoring.device.SensorDevice;

import java.time.LocalDateTime;

/**
 * Represents a reading from a specific vital sign monitor
 */
public class VitalSignReading {
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    private String patientId;

    public VitalSignType getType() {
        return type;
    }

    public void setType(VitalSignType type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public SensorDevice getSource() {
        return source;
    }

    public void setSource(SensorDevice source) {
        this.source = source;
    }

    private VitalSignType type;
    private double value;
    private String unit;
    private LocalDateTime timestamp;
    private SensorDevice source;

    // Constructor, getters, setters

    public VitalSignReading(String patientId, VitalSignType type, double value, String unit, SensorDevice source) {
        this.patientId = patientId;
        this.type = type;
        this.value = value;
        this.unit = unit;
        this.timestamp = LocalDateTime.now();
        this.source = source;
    }
}
