package com.inteliMedExpress.classes.medicalRecords;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.inteliMedExpress.utils.HttpsUtil;
import javax.net.ssl.HttpsURLConnection;

public class MedicalRecord {
    // Server URLs
    private static final String SERVER_BASE_URL = "https://127.0.0.1:8080/api/auth";
    private static final String GET_MEDICAL_RECORDS_URL = SERVER_BASE_URL + "/medical-records";
    private static final String ADD_MEDICAL_RECORD_URL = SERVER_BASE_URL + "/medical-records/add";
    private static final String UPDATE_MEDICAL_RECORD_URL = SERVER_BASE_URL + "/medical-records/update";
    private static final String DELETE_MEDICAL_RECORD_URL = SERVER_BASE_URL + "/medical-records/delete";

    // Medical Record Attributes
    private Integer recordId;
    private String patientName;
    private String patientSurname;
    private String doctorSurname;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String status;
    private LocalDate recordDate;

    // Constructors
    public MedicalRecord() {
        if (!HttpsUtil.isSSLInitialized()) {
            HttpsUtil.setupSSL();
        }
    }

    public MedicalRecord(Integer recordId, String patientName, String patientSurname,
                         String doctorSurname, String diagnosis, String treatment,
                         String prescription, String status, LocalDate recordDate) {
        this.recordId = recordId;
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.doctorSurname = doctorSurname;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
        this.status = status;
        this.recordDate = recordDate;

        if (!HttpsUtil.isSSLInitialized()) {
            HttpsUtil.setupSSL();
        }
    }

    // Getters and Setters
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientSurname() { return patientSurname; }
    public void setPatientSurname(String patientSurname) { this.patientSurname = patientSurname; }

    public String getDoctorSurname() { return doctorSurname; }
    public void setDoctorSurname(String doctorSurname) { this.doctorSurname = doctorSurname; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDate recordDate) { this.recordDate = recordDate; }

    // Get all medical records
    public static List<MedicalRecord> getAllMedicalRecords() throws IOException {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            if (!HttpsUtil.isSSLInitialized()) {
                HttpsUtil.setupSSL();
            }
            // Set up the connection
            URL url = new URL(GET_MEDICAL_RECORDS_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check if the request was successful
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            // Read the response
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse the JSON response
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(response.toString());

            // Process each medical record in the array
            for (Object obj : jsonArray) {
                JSONObject recordJson = (JSONObject) obj;

                // Extract medical record data with type conversion
                Long idLong = (Long) recordJson.get("recordId");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                String patientName = (String) recordJson.get("patientName");
                String patientSurname = (String) recordJson.get("patientSurname");
                String doctorSurname = (String) recordJson.get("doctorSurname");
                String diagnosis = (String) recordJson.get("diagnosis");
                String treatment = (String) recordJson.get("treatment");
                String prescription = (String) recordJson.get("prescription");
                String status = (String) recordJson.get("status");

                // Parse record date
                LocalDate recordDate = null;
                String recordDateStr = (String) recordJson.get("recordDate");
                if (recordDateStr != null && !recordDateStr.isEmpty()) {
                    recordDate = LocalDate.parse(recordDateStr);
                }

                // Create and add the medical record to our list
                MedicalRecord medicalRecord = new MedicalRecord(id, patientName, patientSurname,
                        doctorSurname, diagnosis, treatment, prescription, status, recordDate);
                medicalRecords.add(medicalRecord);
            }

        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching medical records: " + e.getMessage());
            throw new IOException("Failed to fetch medical records", e);
        } finally {
            // Clean up resources
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return medicalRecords;
    }

    // Add a new medical record
    public boolean addToServer() throws IOException {
        URL url = new URL(ADD_MEDICAL_RECORD_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            if (!HttpsUtil.isSSLInitialized()) {
                HttpsUtil.setupSSL();
            }
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            JSONObject recordData = new JSONObject();
            recordData.put("PatientName", this.patientName);
            recordData.put("PatientSurname", this.patientSurname);
            recordData.put("DoctorSurname", this.doctorSurname);
            recordData.put("Diagnosis", this.diagnosis);
            recordData.put("Treatment", this.treatment);
            recordData.put("Prescription", this.prescription);
            recordData.put("RecordStatus", this.status);
            recordData.put("RecordDate", this.recordDate != null ? this.recordDate.toString() : null);

            // Convert JSON to string and get bytes
            String jsonInputString = recordData.toJSONString();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    // Update an existing medical record
    public boolean updateOnServer() throws IOException {
        URL url = new URL(UPDATE_MEDICAL_RECORD_URL + "/" + this.recordId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            if (!HttpsUtil.isSSLInitialized()) {
                HttpsUtil.setupSSL();
            }
            // Set up the HTTP request
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload with record ID
            JSONObject recordData = new JSONObject();
            recordData.put("recordId", this.recordId);
            recordData.put("patientName", this.patientName);
            recordData.put("patientSurname", this.patientSurname);
            recordData.put("doctorSurname", this.doctorSurname);
            recordData.put("diagnosis", this.diagnosis);
            recordData.put("treatment", this.treatment);
            recordData.put("prescription", this.prescription);
            recordData.put("status", this.status);
            recordData.put("recordDate", this.recordDate != null ? this.recordDate.toString() : null);

            // Convert JSON to string and get bytes
            String jsonInputString = recordData.toJSONString();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    // Delete a medical record
    public static boolean deleteMedicalRecord(Integer recordId) throws IOException {
        URL url = new URL(DELETE_MEDICAL_RECORD_URL + "/" + recordId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            if (!HttpsUtil.isSSLInitialized()) {
                HttpsUtil.setupSSL();
            }
            // Set up the HTTP request
            connection.setRequestMethod("DELETE");

            // Get response code
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordId=" + recordId +
                ", patientName='" + patientName + '\'' +
                ", patientSurname='" + patientSurname + '\'' +
                ", doctorSurname='" + doctorSurname + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", status='" + status + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }
}