package com.vhg.patientmonitoring;

import com.vhg.patientmonitoring.core.PatientMonitoringSystem;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PatientMonitoringApplication {
	public static void main(String[] args) {
		PatientMonitoringSystem system = new PatientMonitoringSystem();
		system.initialize();

		// Add shutdown hook
		Runtime.getRuntime().addShutdownHook(new Thread(system::shutdown));

		// In a real application, there would be a user interface here
		// (web app, desktop app, mobile app, etc.)
	}
}
