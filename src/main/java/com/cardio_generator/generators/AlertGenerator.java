package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alert data for patients in the health data simulation.
 * This class simulates alert events (triggered and resolved) for each patient.
 *
 * @author Ege Postacioglu
 */
public class AlertGenerator implements PatientDataGenerator {
    // Fixed: Constants should be in UPPER_CASE
    /**
     * Random number generator used for simulating alert events.
     */
    public static final Random RANDOM_GENERATOR = new Random();
    // Fixed: Variable names should be in camelCase 
    /**
     * Tracks the alert state for each patient.
     * false = resolved, true = triggered.
     */
    private boolean[] alertStates;

    /**
     * Creates a new AlertGenerator for a given number of patients.
     *
     * @param patientCount The number of patients to simulate.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates alert data for a specific patient and outputs it using the given strategy.
     *
     * @param patientId The ID of the patient.
     * @param outputStrategy The strategy used to output the alert data.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (RANDOM_GENERATOR.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Fixed: Variable names should be in camelCase
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = RANDOM_GENERATOR.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
