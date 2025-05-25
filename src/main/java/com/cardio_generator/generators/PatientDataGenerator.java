package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for generating patient data in the simulation.
 * Implementing classes should define how data is generated for each patient.
 *
 * @author Ege Postacioglu
 */
public interface PatientDataGenerator {
    /**
     * Generates data for a specific patient and outputs it using the given strategy.
     *
     * @param patientId The ID of the patient.
     * @param outputStrategy The strategy used to output the generated data.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
