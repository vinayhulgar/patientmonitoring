package com.vhg.patientmonitoring.device;

import com.vhg.patientmonitoring.model.reading.VitalSignReading;
import com.vhg.patientmonitoring.model.reading.VitalSignType;

/**
 * Interface for physical sensor devices
 */
public interface SensorDevice {
    String getDeviceId();
    String getDeviceType();
    boolean isConnected();
    void connect();
    void disconnect();
    VitalSignReading takeReading(String patientId, VitalSignType type);
}
