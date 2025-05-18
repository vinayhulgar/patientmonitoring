package com.vhg.patientmonitoring.agent;

/**
 * Represents a decision made by the AI agent
 */
class Decision {
    private DecisionType type;
    private String description;
    private double confidence;

    // Constructor, getters, setters

    public Decision(DecisionType type, String description, double confidence) {
        this.type = type;
        this.description = description;
        this.confidence = confidence;
    }

    public DecisionType getType() {
        return type;
    }
}