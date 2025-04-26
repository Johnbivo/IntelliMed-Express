package com.inteliMedExpress.classes.Prescriptions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class PrescriptionService {

    private String department;

    public PrescriptionService() {
        this.department = "General";
    }

    public PrescriptionService(String department) {
        this.department = department;
    }
    public String getDepartment() {
        return department;
    }
    public List<Prescription> getAllPrescriptions() throws IOException {
        return Prescription.getAllPrescriptions();
    }

    public boolean addPrescription(Prescription prescription) throws IOException {
        return prescription.addToServer();
    }

    public boolean updatePrescription(Prescription prescription) throws IOException {
        return prescription.updateOnServer();
    }

    public boolean deletePrescription(int prescriptionId) throws IOException {
        return Prescription.deletePrescription(prescriptionId);
    }

    public List<Prescription> getPrescriptionsByPatientId(Integer patientId) throws IOException {
        return Prescription.getPrescriptionsByPatientId(patientId);
    }

    public List<Prescription> getPrescriptionsByDoctorId(Integer doctorId) throws IOException {
        return Prescription.getPrescriptionsByDoctorId(doctorId);
    }

    public List<Prescription> getPrescriptionsByMedicationId(Integer medicationId) throws IOException {
        return Prescription.getPrescriptionsByMedicationId(medicationId);
    }
}