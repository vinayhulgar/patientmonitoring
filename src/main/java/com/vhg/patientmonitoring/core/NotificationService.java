package com.vhg.patientmonitoring.core;

import com.vhg.patientmonitoring.model.alert.Alert;

/**
 * Service for sending notifications about alerts
 */
class NotificationService {
    public void sendUrgentNotification(Alert alert) {
        // Send urgent notification (SMS, push notification, alarm, etc.)
        // Implementation details...
    }

    public void sendStandardNotification(Alert alert) {
        // Send standard notification (email, dashboard update, etc.)
        // Implementation details...
    }
}