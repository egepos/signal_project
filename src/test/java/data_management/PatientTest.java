package data_management;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Patient class, especially the getRecords method.
 *
 * @author Ege Postacioglu
 */
public class PatientTest {
    @Test
    public void testGetRecordsInRange() {
        Patient patient = new Patient(1);
        patient.addRecord(80, "HeartRate", 1000L);
        patient.addRecord(90, "HeartRate", 2000L);
        patient.addRecord(100, "HeartRate", 3000L);

        List<PatientRecord> records = patient.getRecords(1500L, 3500L);
        assertEquals(2, records.size());
        assertEquals(90, records.get(0).getMeasurementValue());
        assertEquals(100, records.get(1).getMeasurementValue());
    }

    @Test
    public void testGetRecordsNoMatch() {
        Patient patient = new Patient(1);
        patient.addRecord(80, "HeartRate", 1000L);

        List<PatientRecord> records = patient.getRecords(2000L, 3000L);
        assertTrue(records.isEmpty());
    }
} 