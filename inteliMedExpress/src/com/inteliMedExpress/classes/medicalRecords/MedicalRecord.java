package com.inteliMedExpress.classes.medicalRecords;

import com.inteliMedExpress.classes.appointments.Appointment;

import java.util.Date;
import java.util.List;

public class MedicalRecord {
    private Integer RecordID;
    private String PatientName;
    private String PatientSurname;
    private String DoctorSurname;
    private String Diagnosis;
    private String Treatment;
    private String Prescription;
    private String Status;
    private Date RecordDate;



    private static final String SERVER_BASE_URL = "https://127.0.0.1:8080/api/generalMedicine";
    private static final String GET_MEDICAL_RECORDS_URL = SERVER_BASE_URL + "/MedicalRecords";
    private static final String ADD_MEDICAL_RECORDS_URL = SERVER_BASE_URL + "/MedicalRecords/add";
    private static final String UPDATE_MEDICAL_RECORDS_URL = SERVER_BASE_URL + "/MedicalRecords/update";
    private static final String DELETE_MEDICAL_RECORDS_URL = SERVER_BASE_URL + "/MedicalRecords/delete";



    public MedicalRecord() {}

    public MedicalRecord(Integer RecordID, String patientName, String patientSurname, String doctorSurname, String Diagnosis, String Treatment, String Prescription, String Status, Date Date) {
        this.RecordID = RecordID;
        this.PatientName = patientName;
        this.PatientSurname = patientSurname;
        this.DoctorSurname = doctorSurname;
        this.Diagnosis = Diagnosis;
        this.Treatment = Treatment;
        this.Prescription = Prescription;
        this.Status = Status;
        this.RecordDate = Date;

    }


    //Getters Setters

    public Integer getRecordID() {
        return RecordID;
    }

    public void setRecordID(Integer recordID) {
        RecordID = recordID;
    }

    public String getPatientName() {
        return PatientName;
    }

    public void setPatientName(String patientName) {
        PatientName = patientName;
    }

    public String getPatientSurname() {
        return PatientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        PatientSurname = patientSurname;
    }

    public String getDoctorSurname() {
        return DoctorSurname;
    }

    public void setDoctorSurname(String doctorSurname) {
        DoctorSurname = doctorSurname;
    }

    public String getDiagnosis() {
        return Diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        Diagnosis = diagnosis;
    }

    public String getTreatment() {
        return Treatment;
    }

    public void setTreatment(String treatment) {
        Treatment = treatment;
    }

    public String getPrescription() {
        return Prescription;
    }

    public void setPrescription(String prescription) {
        Prescription = prescription;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Date getRecordDate() {
        return RecordDate;
    }

    public void setRecordDate(Date recordDate) {
        RecordDate = recordDate;
    }






    public static List<MedicalRecord> loadMedicalRecords() {




        return List.of(new MedicalRecord(null, null, null, null, null, null, null, null, null));
    } //PENDING IMPLEMENTATION
}
