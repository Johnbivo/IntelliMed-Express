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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.inteliMedExpress.utils.HttpsUtil;
import javax.net.ssl.HttpsURLConnection;

public class MedicalRecord {
    // Server URLs
    private static final String SERVER_BASE_URL = "https://springserver-kl8q.onrender.com/api/";

    // Department-specific URLs
    private static String department = "General"; // Default department

    // Dynamic URL getters that use the current department
    private static String getMedicalRecordsUrl() {
        return SERVER_BASE_URL + department + "/medical-records";
    }

    private static String getAddMedicalRecordUrl() {
        return SERVER_BASE_URL + department + "/medical-records/add";
    }

    private static String getUpdateMedicalRecordUrl(Integer recordId) {
        return SERVER_BASE_URL + department + "/medical-records/" + recordId + "/update";
    }

    private static String getDeleteMedicalRecordUrl(Integer recordId) {
        return SERVER_BASE_URL + department + "/medical-records/" + recordId + "/delete";
    }

    // Set the department for all medical records
    public static void setDepartment(String dept) {
        department = dept != null ? dept.replaceAll("\\s", "_") : "General";
        System.out.println("MedicalRecord department set to: " + department);
    }

    // Medical Record Attributes
    private Integer recordId;
    private String patientName;
    private String patientSurname;
    private String doctorName;
    private String doctorSurname;
    private String nurseName;
    private String nurseSurname;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String recordStatus;
    private LocalDateTime recordDate;

    // Constructors
    public MedicalRecord() {
        //HttpsUtil.setupSSL();
    }

    public MedicalRecord(Integer recordId, String patientName, String patientSurname,
                         String doctorName, String doctorSurname, String nurseName, String nurseSurname,
                         String diagnosis, String treatment, String prescription,
                         String recordStatus, LocalDateTime recordDate) {
        this.recordId = recordId;
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.doctorName = doctorName;
        this.doctorSurname = doctorSurname;
        this.nurseName = nurseName;
        this.nurseSurname = nurseSurname;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
        this.recordStatus = recordStatus;
        this.recordDate = recordDate;

        //HttpsUtil.setupSSL();
    }

    // Legacy constructor for backward compatibility
    public MedicalRecord(Integer recordId, String patientName, String patientSurname,
                         String doctorSurname, String diagnosis, String treatment,
                         String prescription, String recordStatus, LocalDateTime recordDate) {
        this(recordId, patientName, patientSurname, null, doctorSurname, null, null,
                diagnosis, treatment, prescription, recordStatus, recordDate);
    }

    // Getters and Setters
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientSurname() { return patientSurname; }
    public void setPatientSurname(String patientSurname) { this.patientSurname = patientSurname; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDoctorSurname() { return doctorSurname; }
    public void setDoctorSurname(String doctorSurname) { this.doctorSurname = doctorSurname; }

    public String getNurseName() { return nurseName; }
    public void setNurseName(String nurseName) { this.nurseName = nurseName; }

    public String getNurseSurname() { return nurseSurname; }
    public void setNurseSurname(String nurseSurname) { this.nurseSurname = nurseSurname; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public String getRecordStatus() { return recordStatus; }
    public void setRecordStatus(String status) { this.recordStatus = status; }

    public LocalDateTime getRecordDate() { return recordDate; }
    public void setRecordDate(LocalDateTime recordDate) { this.recordDate = recordDate; }

    // Get all medical records
    public static List<MedicalRecord> getAllMedicalRecords() throws IOException {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection
            URL url = new URL(getMedicalRecordsUrl());
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Check if the request was successful
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
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

                String patientName = (String) recordJson.get("patientFirstName");
                String patientSurname = (String) recordJson.get("patientLastName");
                String doctorName = (String) recordJson.get("doctorName");
                String doctorSurname = (String) recordJson.get("doctorSurname");
                String nurseName = (String) recordJson.get("nurseName");
                String nurseSurname = (String) recordJson.get("nurseSurname");
                String diagnosis = (String) recordJson.get("diagnosis");
                String treatment = (String) recordJson.get("treatment");
                String prescription = (String) recordJson.get("prescription");
                String recordStatus = (String) recordJson.get("recordStatus");

                // Parse record date
                LocalDateTime recordDate = null;
                String recordDateStr = (String) recordJson.get("recordDate");
                if (recordDateStr != null && !recordDateStr.isEmpty()) {
                    recordDate = LocalDateTime.parse(recordDateStr);
                }

                // Create and add the medical record to our list
                MedicalRecord medicalRecord = new MedicalRecord(id, patientName, patientSurname,
                        doctorName, doctorSurname, nurseName, nurseSurname,
                        diagnosis, treatment, prescription, recordStatus, recordDate);
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
        URL url = new URL(getAddMedicalRecordUrl());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload - IMPORTANT: Map field names to match what the server expects
            JSONObject recordData = new JSONObject();
            recordData.put("patientFirstName", this.patientName);
            recordData.put("patientLastName", this.patientSurname);
            recordData.put("doctorName", this.doctorName);
            recordData.put("doctorSurname", this.doctorSurname);
            recordData.put("diagnosis", this.diagnosis);
            recordData.put("treatment", this.treatment);
            recordData.put("prescription", this.prescription);
            recordData.put("recordStatus", this.recordStatus);
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

            // Get response code and log detailed error if needed
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_CREATED && responseCode != HttpsURLConnection.HTTP_OK) {
                // Read error response for debugging
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getErrorStream() != null ? connection.getErrorStream() : connection.getInputStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                reader.close();

                System.err.println("Error creating medical record. Response code: " + responseCode);
                System.err.println("Error details: " + errorResponse.toString());
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println("Exception while adding medical record: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            connection.disconnect();
        }
    }

    // Update an existing medical record
    public boolean updateOnServer() throws IOException {
        if (this.recordId == null) {
            throw new IllegalStateException("Cannot update a medical record without an ID");
        }

        URL url = new URL(getUpdateMedicalRecordUrl(this.recordId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload with record ID - Match the field names expected by the server
            JSONObject recordData = new JSONObject();
            recordData.put("patientFirstName", this.patientName);
            recordData.put("patientLastName", this.patientSurname);
            recordData.put("doctorName", this.doctorName);
            recordData.put("doctorSurname", this.doctorSurname);
            recordData.put("diagnosis", this.diagnosis);
            recordData.put("treatment", this.treatment);
            recordData.put("prescription", this.prescription);
            recordData.put("recordStatus", this.recordStatus);
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

            // Get response code and log detailed error if needed
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                // Read error response for debugging
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getErrorStream() != null ? connection.getErrorStream() : connection.getInputStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                reader.close();

                System.err.println("Error updating medical record. Response code: " + responseCode);
                System.err.println("Error details: " + errorResponse.toString());
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println("Exception while updating medical record: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            connection.disconnect();
        }
    }

    // Delete a medical record
    public static boolean deleteMedicalRecord(Integer recordId) throws IOException {
        URL url = new URL(getDeleteMedicalRecordUrl(recordId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("DELETE");

            // Get response code and log detailed error if needed
            int responseCode = connection.getResponseCode();

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                // Read error response for debugging
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        connection.getErrorStream() != null ? connection.getErrorStream() : connection.getInputStream()));
                StringBuilder errorResponse = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    errorResponse.append(line);
                }
                reader.close();

                System.err.println("Error deleting medical record. Response code: " + responseCode);
                System.err.println("Error details: " + errorResponse.toString());
                return false;
            }

            return true;
        } catch (Exception e) {
            System.err.println("Exception while deleting medical record: " + e.getMessage());
            e.printStackTrace();
            throw e;
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
                ", doctorName='" + doctorName + '\'' +
                ", doctorSurname='" + doctorSurname + '\'' +
                ", nurseName='" + nurseName + '\'' +
                ", nurseSurname='" + nurseSurname + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", status='" + recordStatus + '\'' +
                ", recordDate=" + recordDate +
                '}';
    }
}