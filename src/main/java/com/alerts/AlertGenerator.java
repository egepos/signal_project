package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        // Get all records for this patient
        java.util.List<com.data_management.PatientRecord> allRecords = patient.getRecords(Long.MIN_VALUE, Long.MAX_VALUE);
        // Sort records by timestamp
        allRecords.sort(new java.util.Comparator<com.data_management.PatientRecord>() {
            public int compare(com.data_management.PatientRecord r1, com.data_management.PatientRecord r2) {
                return Long.compare(r1.getTimestamp(), r2.getTimestamp());
            }
        });

        // Blood Pressure Alerts
        java.util.List<com.data_management.PatientRecord> bloodPressureRecords = new java.util.ArrayList<>();
        for (int i = 0; i < allRecords.size(); i++) {
            com.data_management.PatientRecord record = allRecords.get(i);
            if (record.getRecordType().equalsIgnoreCase("BloodPressure")) {
                bloodPressureRecords.add(record);
            }
        }
        // Trend Alert: 3 consecutive readings, each >10 mmHg change
        for (int i = 2; i < bloodPressureRecords.size(); i++) {
            double first = bloodPressureRecords.get(i-2).getMeasurementValue();
            double second = bloodPressureRecords.get(i-1).getMeasurementValue();
            double third = bloodPressureRecords.get(i).getMeasurementValue();
            if ((second - first > 10 && third - second > 10) || (first - second > 10 && second - third > 10)) {
                Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Blood Pressure Trend Alert", bloodPressureRecords.get(i).getTimestamp());
                triggerAlert(alert);
            }
        }
        // Critical Threshold Alert
        for (int i = 0; i < bloodPressureRecords.size(); i++) {
            com.data_management.PatientRecord record = bloodPressureRecords.get(i);
            double value = record.getMeasurementValue();
            if (value > 180 || value < 90 || value > 120 || value < 60) {
                Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Critical Blood Pressure Alert", record.getTimestamp());
                triggerAlert(alert);
            }
        }

        // Blood Saturation Alerts
        java.util.List<com.data_management.PatientRecord> saturationRecords = new java.util.ArrayList<>();
        for (int i = 0; i < allRecords.size(); i++) {
            com.data_management.PatientRecord record = allRecords.get(i);
            if (record.getRecordType().equalsIgnoreCase("Saturation")) {
                saturationRecords.add(record);
            }
        }
        for (int i = 0; i < saturationRecords.size(); i++) {
            double saturationValue = saturationRecords.get(i).getMeasurementValue();
            if (saturationValue < 92) {
                Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Low Saturation Alert", saturationRecords.get(i).getTimestamp());
                triggerAlert(alert);
            }
            // Rapid Drop Alert: drop of 5% or more within 10 minutes
            for (int j = i+1; j < saturationRecords.size(); j++) {
                double nextSaturationValue = saturationRecords.get(j).getMeasurementValue();
                long firstTimestamp = saturationRecords.get(i).getTimestamp();
                long secondTimestamp = saturationRecords.get(j).getTimestamp();
                if (secondTimestamp - firstTimestamp <= 10 * 60 * 1000 && saturationValue - nextSaturationValue >= 5) {
                    Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Rapid Saturation Drop Alert", saturationRecords.get(j).getTimestamp());
                    triggerAlert(alert);
                }
            }
        }

        // Combined Alert: Hypotensive Hypoxemia
        for (int i = 0; i < bloodPressureRecords.size(); i++) {
            com.data_management.PatientRecord bpRecord = bloodPressureRecords.get(i);
            for (int j = 0; j < saturationRecords.size(); j++) {
                com.data_management.PatientRecord satRecord = saturationRecords.get(j);
                if (Math.abs(bpRecord.getTimestamp() - satRecord.getTimestamp()) < 5 * 60 * 1000) {
                    if (bpRecord.getMeasurementValue() < 90 && satRecord.getMeasurementValue() < 92) {
                        Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Hypotensive Hypoxemia Alert", Math.max(bpRecord.getTimestamp(), satRecord.getTimestamp()));
                        triggerAlert(alert);
                    }
                }
            }
        }

        // ECG Data Alerts (simple peak detection)
        java.util.List<com.data_management.PatientRecord> ecgRecords = new java.util.ArrayList<>();
        for (int i = 0; i < allRecords.size(); i++) {
            com.data_management.PatientRecord record = allRecords.get(i);
            if (record.getRecordType().equalsIgnoreCase("ECG")) {
                ecgRecords.add(record);
            }
        }
        int windowSize = 5;
        for (int i = windowSize; i < ecgRecords.size(); i++) {
            double sum = 0;
            for (int j = i-windowSize; j < i; j++) {
                sum += ecgRecords.get(j).getMeasurementValue();
            }
            double average = sum / windowSize;
            double peak = ecgRecords.get(i).getMeasurementValue();
            if (peak > average * 1.5) {
                Alert alert = new Alert(String.valueOf(patient.getPatientId()), "ECG Peak Alert", ecgRecords.get(i).getTimestamp());
                triggerAlert(alert);
            }
        }

        // Triggered Alert (from button)
        for (int i = 0; i < allRecords.size(); i++) {
            com.data_management.PatientRecord record = allRecords.get(i);
            if (record.getRecordType().equalsIgnoreCase("Alert")) {
                Alert alert = new Alert(String.valueOf(patient.getPatientId()), "Triggered Alert", record.getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        // For now, just print the alert
        System.out.println("ALERT: Patient " + alert.getPatientId() + " - " + alert.getCondition() + " at " + alert.getTimestamp());
    }
}
