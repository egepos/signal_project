package com.cardio_generator.outputs;

/**
 * Interface for outputting patient data in different ways.
 * Implementing classes define how and where the data is sent or stored.
 *
 * @author Ege Postacioglu
 */
public interface OutputStrategy {
    /**
     * Outputs the given data for a patient.
     *
     * @param patientId The ID of the patient.
     * @param timestamp The time the data was generated.
     * @param label The type of data (e.g., "ECG", "Alert").
     * @param data The actual data value.
     */
    void output(int patientId, long timestamp, String label, String data);
}
