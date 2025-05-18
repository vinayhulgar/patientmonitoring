package com.vhg.patientmonitoring.agent.model;

import java.util.List;
import java.util.Map;

/**
 * Interface for working with AI/ML models
 */
public interface ModelInterface {
    Map<String, Object> predict(Map<String, Object> input);
    void train(List<Map<String, Object>> trainingData);
    void saveModel(String path);
    void loadModel(String path);
}