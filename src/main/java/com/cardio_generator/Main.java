package com.cardio_generator;

import com.data_management.DataStorage;
import java.io.IOException;

/**
 * Main class that serves as the entry point for the application.
 * Allows running either DataStorage or HealthDataSimulator based on command-line arguments.
 */
public class Main {
    /**
     * Main method that determines which class to run based on command-line arguments.
     * @param args Command-line arguments. If first argument is "DataStorage", runs DataStorage,
     *             otherwise runs HealthDataSimulator.
     */
    public static void main(String[] args) {
        try {
            if (args.length > 0 && args[0].equals("DataStorage")) {
                DataStorage.main(new String[]{});
            } else {
                HealthDataSimulator.main(args);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 