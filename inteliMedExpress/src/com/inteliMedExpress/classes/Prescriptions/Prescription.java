package com.inteliMedExpress.classes.Prescriptions;

import com.inteliMedExpress.utils.HttpsUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Prescription {

    private static final String SERVER_BASE_URL = "https://springserver-kl8q.onrender.com/api/";

    private static String department = "General";

    private static String getPrescriptionsUrl() {
        return SERVER_BASE_URL + department + "/prescriptions";
    }

    private static String getAddPrescriptionUrl() {
        return SERVER_BASE_URL + department + "/prescriptions/add";
    }

    private static String getUpdatePrescriptionUrl(Integer id) {
        return SERVER_BASE_URL + department + "/prescriptions/" + id + "/update";
    }

    private static String getDeletePrescriptionUrl(Integer id) {
        return SERVER_BASE_URL + department + "/prescriptions/" + id + "/delete";
    }

    // Set the department for all prescriptions
    public static void setDepartment(String dept) {
        department = dept != null ? dept.replaceAll("\\s", "_") : "General";
        System.out.println("Prescription department set to: " + department);
    }

    // Prescription Attributes
    private Integer prescriptionId;
    private Integer patientId;
    private String patientFirstName;
    private String patientLastName;
    private Integer doctorId;
    private String doctorName;
    private String doctorSurname;
    private Integer medicationId;
    private String medicationName;
    private String medicationDescription;
    private String dosage;
    private String duration;
    private LocalDateTime datePrescribed;
    private Integer departmentId;
    private String departmentName;

    // Default constructor
    public Prescription() {
        //HttpsUtil.setupSSL();
    }

    // Parameterized constructor
    public Prescription(Integer prescriptionId, Integer patientId, String patientFirstName, String patientLastName,
                        Integer doctorId, String doctorName, String doctorSurname,
                        Integer medicationId, String medicationName, String medicationDescription,
                        String dosage, String duration, LocalDateTime datePrescribed,
                        Integer departmentId, String departmentName) {
        this.prescriptionId = prescriptionId;
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSurname = doctorSurname;
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.medicationDescription = medicationDescription;
        this.dosage = dosage;
        this.duration = duration;
        this.datePrescribed = datePrescribed;
        this.departmentId = departmentId;
        this.departmentName = departmentName;

        //HttpsUtil.setupSSL();
    }

    // Constructor with minimum required fields
    public Prescription(Integer patientId, String patientFirstName, String patientLastName,
                        Integer doctorId, String doctorName, String doctorSurname,
                        Integer medicationId, String medicationName,
                        String dosage, String duration) {
        this(null, patientId, patientFirstName, patientLastName,
                doctorId, doctorName, doctorSurname,
                medicationId, medicationName, null,
                dosage, duration, LocalDateTime.now(),
                null, null);
    }

    // Getters and Setters
    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorSurname() {
        return doctorSurname;
    }

    public void setDoctorSurname(String doctorSurname) {
        this.doctorSurname = doctorSurname;
    }

    public Integer getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Integer medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getMedicationDescription() {
        return medicationDescription;
    }

    public void setMedicationDescription(String medicationDescription) {
        this.medicationDescription = medicationDescription;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LocalDateTime getDatePrescribed() {
        return datePrescribed;
    }

    public void setDatePrescribed(LocalDateTime datePrescribed) {
        this.datePrescribed = datePrescribed;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    // Helper method to get formatted date
    public String getFormattedDateTime() {
        if (datePrescribed == null) return "Not specified";
        return datePrescribed.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // Get all prescriptions
    public static List<Prescription> getAllPrescriptions() throws IOException {
        List<Prescription> prescriptions = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection using the dynamic URL
            URL url = new URL(getPrescriptionsUrl());
            connection = (HttpsURLConnection) url.openConnection();
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

            // Process each prescription in the array
            for (Object obj : jsonArray) {
                JSONObject prescriptionJson = (JSONObject) obj;

                // Extract prescription data with type conversion
                Long idLong = (Long) prescriptionJson.get("prescriptionId");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                Long patientIdLong = (Long) prescriptionJson.get("patientId");
                Integer patientId = (patientIdLong != null) ? patientIdLong.intValue() : null;

                String patientFirstName = (String) prescriptionJson.get("patientFirstName");
                String patientLastName = (String) prescriptionJson.get("patientLastName");

                Long doctorIdLong = (Long) prescriptionJson.get("doctorId");
                Integer doctorId = (doctorIdLong != null) ? doctorIdLong.intValue() : null;

                String doctorName = (String) prescriptionJson.get("doctorName");
                String doctorSurname = (String) prescriptionJson.get("doctorSurname");

                Long medicationIdLong = (Long) prescriptionJson.get("medicationId");
                Integer medicationId = (medicationIdLong != null) ? medicationIdLong.intValue() : null;

                String medicationName = (String) prescriptionJson.get("medicationName");
                String medicationDescription = (String) prescriptionJson.get("medicationDescription");

                String dosage = (String) prescriptionJson.get("dosage");
                String duration = (String) prescriptionJson.get("duration");

                Long departmentIdLong = (Long) prescriptionJson.get("departmentId");
                Integer departmentId = (departmentIdLong != null) ? departmentIdLong.intValue() : null;

                String departmentName = (String) prescriptionJson.get("departmentName");

                // Parse prescription date
                LocalDateTime datePrescribed = null;
                String datePrescribedStr = (String) prescriptionJson.get("datePrescribed");

                if (datePrescribedStr != null && !datePrescribedStr.isEmpty()) {
                    try {
                        // Try to parse as a full datetime
                        datePrescribed = LocalDateTime.parse(datePrescribedStr);
                    } catch (Exception e) {
                        try {
                            // If that fails, try to parse with a formatter that handles both patterns
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                            datePrescribed = LocalDateTime.parse(datePrescribedStr, formatter);
                        } catch (Exception e2) {
                            try {
                                // If all else fails, extract just the date part
                                String datePortion = datePrescribedStr.split("T")[0];
                                // Convert to LocalDateTime with time at 00:00
                                datePrescribed = LocalDate.parse(datePortion).atStartOfDay();
                            } catch (Exception e3) {
                                // If we still can't parse it, leave as null
                                System.err.println("Could not parse date: " + datePrescribedStr);
                            }
                        }
                    }
                }

                // Create and add the prescription to our list
                Prescription prescription = new Prescription(id, patientId, patientFirstName, patientLastName,
                        doctorId, doctorName, doctorSurname,
                        medicationId, medicationName, medicationDescription,
                        dosage, duration, datePrescribed,
                        departmentId, departmentName);

                prescriptions.add(prescription);
            }

        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching prescriptions: " + e.getMessage());
            throw new IOException("Failed to fetch prescriptions", e);
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

        return prescriptions;
    }

    // Add a new prescription
    public boolean addToServer() throws IOException {
        URL url = new URL(getAddPrescriptionUrl());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            JSONObject prescriptionData = new JSONObject();
            prescriptionData.put("patientId", this.patientId);
            prescriptionData.put("patientFirstName", this.patientFirstName);
            prescriptionData.put("patientLastName", this.patientLastName);
            prescriptionData.put("doctorId", this.doctorId);
            prescriptionData.put("doctorName", this.doctorName);
            prescriptionData.put("doctorSurname", this.doctorSurname);
            prescriptionData.put("medicationId", this.medicationId);
            prescriptionData.put("medicationName", this.medicationName);
            prescriptionData.put("medicationDescription", this.medicationDescription);
            prescriptionData.put("dosage", this.dosage);
            prescriptionData.put("duration", this.duration);
            // Format the LocalDateTime properly for the server
            prescriptionData.put("datePrescribed", this.datePrescribed != null ?
                    this.datePrescribed.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
            prescriptionData.put("departmentId", this.departmentId);
            prescriptionData.put("departmentName", this.departmentName);

            // Convert JSON to string and get bytes
            String jsonInputString = prescriptionData.toJSONString();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            // Read response body even if there's an error
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Log error response
            if (responseCode >= 400) {
                System.err.println("Error response: " + response.toString());
            } else if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                // Try to extract the ID from the response
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject jsonResponse = (JSONObject) parser.parse(response.toString());
                    Long idLong = (Long) jsonResponse.get("prescriptionId");
                    if (idLong != null) {
                        this.prescriptionId = idLong.intValue();
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing ID from response: " + e.getMessage());
                }
            }

            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;
        } finally {
            connection.disconnect();
        }
    }

    // Update an existing prescription
    public boolean updateOnServer() throws IOException {
        if (this.prescriptionId == null) {
            throw new IllegalStateException("Cannot update prescription without ID");
        }

        URL url = new URL(getUpdatePrescriptionUrl(this.prescriptionId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            JSONObject prescriptionData = new JSONObject();
            prescriptionData.put("patientId", this.patientId);
            prescriptionData.put("patientFirstName", this.patientFirstName);
            prescriptionData.put("patientLastName", this.patientLastName);
            prescriptionData.put("doctorId", this.doctorId);
            prescriptionData.put("doctorName", this.doctorName);
            prescriptionData.put("doctorSurname", this.doctorSurname);
            prescriptionData.put("medicationId", this.medicationId);
            prescriptionData.put("medicationName", this.medicationName);
            prescriptionData.put("medicationDescription", this.medicationDescription);
            prescriptionData.put("dosage", this.dosage);
            prescriptionData.put("duration", this.duration);
            // Format the LocalDateTime properly for the server
            prescriptionData.put("datePrescribed", this.datePrescribed != null ?
                    this.datePrescribed.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
            prescriptionData.put("departmentId", this.departmentId);
            prescriptionData.put("departmentName", this.departmentName);

            // Convert JSON to string and get bytes
            String jsonInputString = prescriptionData.toJSONString();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            // Read response body even if there's an error
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Log error response
            if (responseCode >= 400) {
                System.err.println("Error response: " + response.toString());
            }

            return responseCode == HttpURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    // Delete a prescription
    public static boolean deletePrescription(Integer prescriptionId) throws IOException {
        URL url = new URL(getDeletePrescriptionUrl(prescriptionId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Accept", "application/json");

            // Get response code
            int responseCode = connection.getResponseCode();

            // Read response body even if there's an error
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            // Log error response
            if (responseCode >= 400) {
                System.err.println("Error response: " + response.toString());
            }

            return responseCode == HttpURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    // Get prescriptions by patient ID
    public static List<Prescription> getPrescriptionsByPatientId(Integer patientId) throws IOException {
        List<Prescription> prescriptions = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection
            URL url = new URL(getPrescriptionsUrl() + "/patient/" + patientId);
            connection = (HttpsURLConnection) url.openConnection();
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

            // Process each prescription in the array - same parsing logic as getAllPrescriptions
            for (Object obj : jsonArray) {
                JSONObject prescriptionJson = (JSONObject) obj;

                Long idLong = (Long) prescriptionJson.get("prescriptionId");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                Long patientIdLong = (Long) prescriptionJson.get("patientId");
                Integer patId = (patientIdLong != null) ? patientIdLong.intValue() : null;

                String patientFirstName = (String) prescriptionJson.get("patientFirstName");
                String patientLastName = (String) prescriptionJson.get("patientLastName");

                Long doctorIdLong = (Long) prescriptionJson.get("doctorId");
                Integer doctorId = (doctorIdLong != null) ? doctorIdLong.intValue() : null;

                String doctorName = (String) prescriptionJson.get("doctorName");
                String doctorSurname = (String) prescriptionJson.get("doctorSurname");

                Long medicationIdLong = (Long) prescriptionJson.get("medicationId");
                Integer medicationId = (medicationIdLong != null) ? medicationIdLong.intValue() : null;

                String medicationName = (String) prescriptionJson.get("medicationName");
                String medicationDescription = (String) prescriptionJson.get("medicationDescription");

                String dosage = (String) prescriptionJson.get("dosage");
                String duration = (String) prescriptionJson.get("duration");

                Long departmentIdLong = (Long) prescriptionJson.get("departmentId");
                Integer departmentId = (departmentIdLong != null) ? departmentIdLong.intValue() : null;

                String departmentName = (String) prescriptionJson.get("departmentName");

                // Parse prescription date
                LocalDateTime datePrescribed = null;
                String datePrescribedStr = (String) prescriptionJson.get("datePrescribed");

                if (datePrescribedStr != null && !datePrescribedStr.isEmpty()) {
                    try {
                        datePrescribed = LocalDateTime.parse(datePrescribedStr);
                    } catch (Exception e) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                            datePrescribed = LocalDateTime.parse(datePrescribedStr, formatter);
                        } catch (Exception e2) {
                            try {
                                String datePortion = datePrescribedStr.split("T")[0];
                                datePrescribed = LocalDate.parse(datePortion).atStartOfDay();
                            } catch (Exception e3) {
                                System.err.println("Could not parse date: " + datePrescribedStr);
                            }
                        }
                    }
                }

                Prescription prescription = new Prescription(id, patId, patientFirstName, patientLastName,
                        doctorId, doctorName, doctorSurname,
                        medicationId, medicationName, medicationDescription,
                        dosage, duration, datePrescribed,
                        departmentId, departmentName);

                prescriptions.add(prescription);
            }

        } catch (Exception e) {
            System.err.println("Error fetching prescriptions by patient ID: " + e.getMessage());
            throw new IOException("Failed to fetch prescriptions", e);
        } finally {
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

        return prescriptions;
    }

    // Get prescriptions by doctor ID
    public static List<Prescription> getPrescriptionsByDoctorId(Integer doctorId) throws IOException {
        List<Prescription> prescriptions = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection
            URL url = new URL(getPrescriptionsUrl() + "/doctor/" + doctorId);
            connection = (HttpsURLConnection) url.openConnection();
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

            // Process each prescription in the array
            for (Object obj : jsonArray) {
                JSONObject prescriptionJson = (JSONObject) obj;

                Long idLong = (Long) prescriptionJson.get("prescriptionId");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                Long patientIdLong = (Long) prescriptionJson.get("patientId");
                Integer patientId = (patientIdLong != null) ? patientIdLong.intValue() : null;

                String patientFirstName = (String) prescriptionJson.get("patientFirstName");
                String patientLastName = (String) prescriptionJson.get("patientLastName");

                Long doctorIdLong = (Long) prescriptionJson.get("doctorId");
                Integer docId = (doctorIdLong != null) ? doctorIdLong.intValue() : null;

                String doctorName = (String) prescriptionJson.get("doctorName");
                String doctorSurname = (String) prescriptionJson.get("doctorSurname");

                Long medicationIdLong = (Long) prescriptionJson.get("medicationId");
                Integer medicationId = (medicationIdLong != null) ? medicationIdLong.intValue() : null;

                String medicationName = (String) prescriptionJson.get("medicationName");
                String medicationDescription = (String) prescriptionJson.get("medicationDescription");

                String dosage = (String) prescriptionJson.get("dosage");
                String duration = (String) prescriptionJson.get("duration");

                Long departmentIdLong = (Long) prescriptionJson.get("departmentId");
                Integer departmentId = (departmentIdLong != null) ? departmentIdLong.intValue() : null;

                String departmentName = (String) prescriptionJson.get("departmentName");

                // Parse prescription date
                LocalDateTime datePrescribed = null;
                String datePrescribedStr = (String) prescriptionJson.get("datePrescribed");

                if (datePrescribedStr != null && !datePrescribedStr.isEmpty()) {
                    try {
                        datePrescribed = LocalDateTime.parse(datePrescribedStr);
                    } catch (Exception e) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                            datePrescribed = LocalDateTime.parse(datePrescribedStr, formatter);
                        } catch (Exception e2) {
                            try {
                                String datePortion = datePrescribedStr.split("T")[0];
                                datePrescribed = LocalDate.parse(datePortion).atStartOfDay();
                            } catch (Exception e3) {
                                System.err.println("Could not parse date: " + datePrescribedStr);
                            }
                        }
                    }
                }

                Prescription prescription = new Prescription(id, patientId, patientFirstName, patientLastName,
                        docId, doctorName, doctorSurname,
                        medicationId, medicationName, medicationDescription,
                        dosage, duration, datePrescribed,
                        departmentId, departmentName);

                prescriptions.add(prescription);
            }

        } catch (Exception e) {
            System.err.println("Error fetching prescriptions by doctor ID: " + e.getMessage());
            throw new IOException("Failed to fetch prescriptions", e);
        } finally {
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

        return prescriptions;
    }

    // Get prescriptions by medication ID
    public static List<Prescription> getPrescriptionsByMedicationId(Integer medicationId) throws IOException {
        List<Prescription> prescriptions = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection
            URL url = new URL(getPrescriptionsUrl() + "/medication/" + medicationId);
            connection = (HttpsURLConnection) url.openConnection();
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

            // Process each prescription in the array
            for (Object obj : jsonArray) {
                JSONObject prescriptionJson = (JSONObject) obj;

                Long idLong = (Long) prescriptionJson.get("prescriptionId");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                Long patientIdLong = (Long) prescriptionJson.get("patientId");
                Integer patientId = (patientIdLong != null) ? patientIdLong.intValue() : null;

                String patientFirstName = (String) prescriptionJson.get("patientFirstName");
                String patientLastName = (String) prescriptionJson.get("patientLastName");

                Long doctorIdLong = (Long) prescriptionJson.get("doctorId");
                Integer doctorId = (doctorIdLong != null) ? doctorIdLong.intValue() : null;

                String doctorName = (String) prescriptionJson.get("doctorName");
                String doctorSurname = (String) prescriptionJson.get("doctorSurname");

                Long medicationIdLong = (Long) prescriptionJson.get("medicationId");
                Integer medId = (medicationIdLong != null) ? medicationIdLong.intValue() : null;

                String medicationName = (String) prescriptionJson.get("medicationName");
                String medicationDescription = (String) prescriptionJson.get("medicationDescription");

                String dosage = (String) prescriptionJson.get("dosage");
                String duration = (String) prescriptionJson.get("duration");

                Long departmentIdLong = (Long) prescriptionJson.get("departmentId");
                Integer departmentId = (departmentIdLong != null) ? departmentIdLong.intValue() : null;

                String departmentName = (String) prescriptionJson.get("departmentName");

                // Parse prescription date
                LocalDateTime datePrescribed = null;
                String datePrescribedStr = (String) prescriptionJson.get("datePrescribed");

                if (datePrescribedStr != null && !datePrescribedStr.isEmpty()) {
                    try {
                        datePrescribed = LocalDateTime.parse(datePrescribedStr);
                    } catch (Exception e) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                            datePrescribed = LocalDateTime.parse(datePrescribedStr, formatter);
                        } catch (Exception e2) {
                            try {
                                String datePortion = datePrescribedStr.split("T")[0];
                                datePrescribed = LocalDate.parse(datePortion).atStartOfDay();
                            } catch (Exception e3) {
                                System.err.println("Could not parse date: " + datePrescribedStr);
                            }
                        }
                    }
                }

                Prescription prescription = new Prescription(id, patientId, patientFirstName, patientLastName,
                        doctorId, doctorName, doctorSurname,
                        medId, medicationName, medicationDescription,
                        dosage, duration, datePrescribed,
                        departmentId, departmentName);

                prescriptions.add(prescription);
            }

        } catch (Exception e) {
            System.err.println("Error fetching prescriptions by medication ID: " + e.getMessage());
            throw new IOException("Failed to fetch prescriptions", e);
        } finally {
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

        return prescriptions;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "prescriptionId=" + prescriptionId +
                ", patientId=" + patientId +
                ", patientFirstName='" + patientFirstName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                ", doctorId=" + doctorId +
                ", doctorName='" + doctorName + '\'' +
                ", doctorSurname='" + doctorSurname + '\'' +
                ", medicationId=" + medicationId +
                ", medicationName='" + medicationName + '\'' +
                ", medicationDescription='" + medicationDescription + '\'' +
                ", dosage='" + dosage + '\'' +
                ", duration='" + duration + '\'' +
                ", datePrescribed=" + (datePrescribed != null ? datePrescribed.format(DateTimeFormatter.ISO_DATE_TIME) : null) +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}