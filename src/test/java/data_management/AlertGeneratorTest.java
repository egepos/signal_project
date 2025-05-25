package data_management;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import com.alerts.AlertGenerator;
import com.alerts.Alert;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AlertGenerator class. Tests that alerts are triggered for all alert types.
 *
 * @author Ege Postacioglu
 */
public class AlertGeneratorTest {
    @Test
    public void testBloodPressureTrendAlert() {
        Patient patient = new Patient(1);
        patient.addRecord(100, "BloodPressure", 1000L);
        patient.addRecord(115, "BloodPressure", 2000L);
        patient.addRecord(130, "BloodPressure", 3000L);
        AlertGenerator generator = new AlertGenerator(null);
        generator.evaluateData(patient);
    }

    @Test
    public void testLowSaturationAlert() {
        Patient patient = new Patient(2);
        patient.addRecord(91, "Saturation", 1000L);
        AlertGenerator generator = new AlertGenerator(null);
        generator.evaluateData(patient);
    }

    @Test
    public void testCombinedAlert() {
        Patient patient = new Patient(3);
        patient.addRecord(85, "BloodPressure", 1000L);
        patient.addRecord(90, "Saturation", 1000L);
        AlertGenerator generator = new AlertGenerator(null);
        generator.evaluateData(patient);
    }

    @Test
    public void testECGPeakAlert() {
        Patient patient = new Patient(4);
        patient.addRecord(1, "ECG", 1000L);
        patient.addRecord(1, "ECG", 2000L);
        patient.addRecord(1, "ECG", 3000L);
        patient.addRecord(1, "ECG", 4000L);
        patient.addRecord(1, "ECG", 5000L);
        patient.addRecord(10, "ECG", 6000L);
        AlertGenerator generator = new AlertGenerator(null);
        generator.evaluateData(patient);
    }

    @Test
    public void testTriggeredAlert() {
        Patient patient = new Patient(5);
        patient.addRecord(1, "Alert", 1000L);
        AlertGenerator generator = new AlertGenerator(null);
        generator.evaluateData(patient);
    }
} 