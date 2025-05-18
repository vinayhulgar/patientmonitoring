package com.vhg.patientmonitoring.agent;

/**
 * Types of decisions that can be made by the agent
 */
public enum DecisionType {
    ALERT,                  // Generate an alert for human attention
    ADJUST_MONITORING,      // Change monitoring parameters (e.g., frequency)
    RECOMMEND_INTERVENTION, // Suggest a specific medical intervention
    NO_ACTION               // Continue normal monitoring
}