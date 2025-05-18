package com.vhg.patientmonitoring;

import com.vhg.patientmonitoring.model.patient.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest
public class PatientTest {

    @Test
    void TestPatientCreation(){
        Patient patient = new Patient(UUID.randomUUID(),
                "John", 34, "MALE");
        assertThat(patient.getName()).isEqualTo("John");
        assertThat(patient.getGender()).isEqualTo("MALE");
    }


}
