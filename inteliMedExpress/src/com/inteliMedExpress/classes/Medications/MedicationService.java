package com.inteliMedExpress.classes.Medications;

import java.io.IOException;
import java.util.List;

public class MedicationService {

    public List<Medication> getAllMedications() throws IOException {
        return Medication.getAllMedications();
    }

    // Note: These methods should only be available to pharmacy staff
    public boolean addMedication(Medication medication) throws IOException {
        return medication.addToServer();
    }

    public boolean updateMedication(Medication medication) throws IOException {
        return medication.updateOnServer();
    }

    public boolean deleteMedication(int medicationId) throws IOException {
        return Medication.deleteMedication(medicationId);
    }

    // Specialized method to update stock quantity
    public boolean updateMedicationStock(Medication medication, int newQuantity) throws IOException {
        return medication.updateStock(newQuantity);
    }

    // Helper method to find a medication by ID from a list
    public Medication findMedicationById(List<Medication> medications, int medicationId) {
        for (Medication medication : medications) {
            if (medication.getMedicationId() == medicationId) {
                return medication;
            }
        }
        return null;
    }

    // Helper method to check if a medication is low in stock
    public boolean isLowStock(Medication medication, int threshold) {
        return medication.getStockQuantity() != null && medication.getStockQuantity() <= threshold;
    }
}