package com.inteliMedExpress.classes.medicalRecords;

import java.io.IOException;
import java.util.List;

public class MedicalRecordsService {

    public List<MedicalRecord> getAllMedicalRecords() throws IOException {
        return MedicalRecord.getAllMedicalRecords();
    }

    public boolean addMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        return medicalRecord.addToServer();
    }

    public boolean updateMedicalRecord(MedicalRecord medicalRecord) throws IOException {
        return medicalRecord.updateOnServer();
    }

    public boolean deleteMedicalRecord(int recordId) throws IOException {
        return MedicalRecord.deleteMedicalRecord(recordId);
    }
}