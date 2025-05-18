package com.vhg.patientmonitoring.device.impl;

import com.vhg.patientmonitoring.device.SensorDevice;
import com.vhg.patientmonitoring.model.reading.VitalSignReading;
import com.vhg.patientmonitoring.model.reading.VitalSignType;

/**
 * Implementation for a heart rate monitor
 */
class HeartRateMonitor implements SensorDevice {
    private String deviceId;
    private boolean connected;

    // Constructor and implementation of SensorDevice methods

    @Override
    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String getDeviceType() {
        return "Heart Rate Monitor";
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public void connect() {
        // Logic to connect to the physical device
        this.connected = true;
    }

    @Override
    public void disconnect() {
        // Logic to disconnect from the physical device
        this.connected = false;
    }

    @Override
    public VitalSignReading takeReading(String patientId, VitalSignType type) {
        // Simulated reading - in a real app, would interface with actual hardware
        double heartRate = 60 + (Math.random() * 40); // Random value between 60-100
        return new VitalSignReading(patientId, VitalSignType.HEART_RATE, heartRate, "bpm", this);
    }
}