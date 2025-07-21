package com.inteliMedExpress.classes.patients;

import java.io.IOException;
import java.util.List;

public class PatientService {

    public List<Patient> getAllPatients() throws IOException {
        return Patient.getAllPatients();
    }



    public boolean addPatient(Patient patient) throws IOException {
        return patient.addToServer();
    }

    public boolean updatePatient(Patient patient) throws IOException {
        return patient.updateOnServer();
    }

    public boolean deletePatient(int patientId) throws IOException {
        return Patient.deletePatient(patientId);
    }
}
