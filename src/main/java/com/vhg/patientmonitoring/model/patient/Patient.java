package com.vhg.patientmonitoring.model.patient;

import java.util.*;

public class Patient {

    private UUID patientId;
    private String name;
    private int age;
    private String gender;
    private String medicalHistory;
    private List<Medication> currentMedications;
    private Map<String, String> demographics;
    private EmergencyContact emergencyContact;

// Constructor, getters, setters

    public Patient(UUID patientId, String name, int age, String gender) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.currentMedications = new ArrayList<>();
        this.demographics = new HashMap<>();
    }

    // Other methods...

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public UUID getPatientId() {
        return patientId;
    }

    public Object getMedicalHistory() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public void setCurrentMedications(List<Medication> currentMedications) {
        this.currentMedications = currentMedications;
    }

    public Map<String, String> getDemographics() {
        return demographics;
    }

    public void setDemographics(Map<String, String> demographics) {
        this.demographics = demographics;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public Object getCurrentMedications() {
        return null;
    }
}