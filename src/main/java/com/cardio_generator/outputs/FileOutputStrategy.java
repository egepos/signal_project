package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

// Fixed: Class name should match file name
/**
 * Outputs patient data to files in a specified directory.
 * Each type of data is written to a separate file.
 *
 * @author Ege Postacioglu
 */
public class FileOutputStrategy implements OutputStrategy {

    // Fixed: Variable names should be in camelCase
    private String baseDirectory;

    // Fixed: Variable names should be in camelCase
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Creates a new FileOutputStrategy for the given directory.
     *
     * @param baseDirectory The directory where files will be written.
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs the given data for a patient to a file.
     *
     * @param patientId The ID of the patient.
     * @param timestamp The time the data was generated.
     * @param label The type of data.
     * @param data The actual data value.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Fixed: Variable names should be in camelCase
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}