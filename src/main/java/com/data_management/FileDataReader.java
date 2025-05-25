package com.data_management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads patient data from files in a specified directory and stores it in DataStorage.
 * Each line in the file should be formatted as: patientId,timestamp,recordType,measurementValue
 *
 * @author Ege Postacioglu
 */
public class FileDataReader implements DataReader {
    private String directoryPath;

    /**
     * Constructs a FileDataReader for the given directory.
     * @param directoryPath the path to the directory containing data files
     */
    public FileDataReader(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    /**
     * Reads data from all files in the directory and stores it in DataStorage.
     * Expects each line to be formatted as:
     * patientId,timestamp,recordType,measurementValue
     * Example: 1,1700000000000,HeartRate,85.0
     *
     * @param dataStorage the storage where data will be stored
     * @throws IOException if there is an error reading the data
     */
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new IOException("Directory does not exist: " + directoryPath);
        }
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Skip empty lines
                        if (line.trim().isEmpty()) continue;
                        String[] parts = line.split(",");
                        if (parts.length != 4) continue; // Skip malformed lines
                        try {
                            int patientId = Integer.parseInt(parts[0].trim());
                            long timestamp = Long.parseLong(parts[1].trim());
                            String recordType = parts[2].trim();
                            double measurementValue = Double.parseDouble(parts[3].trim());
                            dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                        } catch (NumberFormatException e) {
                            // Skip lines with invalid numbers
                            continue;
                        }
                    }
                }
            }
        }
    }
} 