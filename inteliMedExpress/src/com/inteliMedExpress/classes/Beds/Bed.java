package com.inteliMedExpress.classes.Beds;

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
import java.util.ArrayList;
import java.util.List;

public class Bed {
    // Server URLs
    private static final String SERVER_BASE_URL = "https://127.0.0.1:8080/api/";

    // Department-specific URLs
    private static String department = "General"; // Default department

    // Dynamic URL getters that use the current department
    private static String getBedsUrl() {
        return SERVER_BASE_URL + department + "/beds";
    }

    private static String getAssignBedUrl() {
        return SERVER_BASE_URL + department + "/beds/assign";
    }

    private static String getDischargeBedUrl() {
        return SERVER_BASE_URL + department + "/beds/discharge";
    }

    // Set the department for all beds
    public static void setDepartment(String dept) {
        department = dept != null ? dept.replaceAll("\\s", "_") : "General";
        System.out.println("Bed department set to: " + department);
    }

    // Bed Attributes
    private Integer bedId;
    private Integer roomNumber;
    private String status; // OCCUPIED, AVAILABLE
    private Integer patientId;
    private String patientFirstName;
    private String patientLastName;

    // Constructors
    public Bed() {
        HttpsUtil.setupSSL();
    }

    public Bed(Integer bedId, Integer roomNumber, String status, Integer patientId,
               String patientFirstName, String patientLastName) {
        this.bedId = bedId;
        this.roomNumber = roomNumber;
        this.status = status;
        this.patientId = patientId;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;

        HttpsUtil.setupSSL();
    }

    // Getters and Setters
    public Integer getBedId() { return bedId; }
    public void setBedId(Integer bedId) { this.bedId = bedId; }

    public Integer getRoomNumber() { return roomNumber; }
    public void setRoomNumber(Integer roomNumber) { this.roomNumber = roomNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getPatientFirstName() { return patientFirstName; }
    public void setPatientFirstName(String patientFirstName) { this.patientFirstName = patientFirstName; }

    public String getPatientLastName() { return patientLastName; }
    public void setPatientLastName(String patientLastName) { this.patientLastName = patientLastName; }

    // Get patient's full name or empty string if no patient
    public String getPatientFullName() {
        if (patientFirstName == null || patientFirstName.isEmpty()) {
            return "";
        }
        return patientFirstName + " " + patientLastName;
    }

    // Get all beds
    public static List<Bed> getAllBeds() throws IOException {
        List<Bed> beds = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection
            URL url = new URL(getBedsUrl());
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

            // Process each bed in the array
            for (Object obj : jsonArray) {
                JSONObject bedJson = (JSONObject) obj;

                // Extract bed data with type conversion
                Long bedIdLong = (Long) bedJson.get("bedId");
                Integer bedId = (bedIdLong != null) ? bedIdLong.intValue() : null;

                Long roomNumberLong = (Long) bedJson.get("roomNumber");
                Integer roomNumber = (roomNumberLong != null) ? roomNumberLong.intValue() : null;

                // Handle status conversion - it might be a complex object in JSON
                String status;
                Object statusObj = bedJson.get("status");
                if (statusObj instanceof String) {
                    status = (String) statusObj;
                } else if (statusObj instanceof JSONObject) {
                    // If it's a JSON object, try to extract the value
                    status = (String) ((JSONObject) statusObj).get("value");
                } else {
                    // Default fallback
                    status = "Unknown";
                }

                // Make sure status matches what your UI expects (Available or Occupied)
                if (status != null && !status.equalsIgnoreCase("Available") && !status.equalsIgnoreCase("Occupied")) {
                    // Convert any other value to one of the expected values
                    System.out.println("Converting status from '" + status + "' to standardized format");
                    status = status.toLowerCase().contains("avail") ? "Available" : "Occupied";
                }

                Long patientIdLong = (Long) bedJson.get("patientId");
                Integer patientId = (patientIdLong != null) ? patientIdLong.intValue() : null;

                // These fields might need to be fetched from patient data
                String patientFirstName = (String) bedJson.get("patientFirstName");
                String patientLastName = (String) bedJson.get("patientLastName");

                // Create and add the bed to our list
                Bed bed = new Bed(bedId, roomNumber, status, patientId, patientFirstName, patientLastName);
                beds.add(bed);
            }


        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching beds: " + e.getMessage());
            throw new IOException("Failed to fetch beds", e);
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

        return beds;
    }

    // Assign a patient to a bed
    public boolean assignPatient() throws IOException {
        // The endpoint should be /api/{department}/beds/{id}/assign-patient/{patientId}
        URL url = new URL(getBedsUrl() + "/" + this.bedId + "/assign-patient/" + this.patientId);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request - server expects POST
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // No need for request body as parameters are in URL

            // Get response code
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED;
        } finally {
            connection.disconnect();
        }
    }

    // Discharge a patient from a bed
    public boolean dischargePatient() throws IOException {
        // The endpoint should be /api/{department}/beds/{id}/release-patient
        URL url = new URL(getBedsUrl() + "/" + this.bedId + "/release-patient");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request - server expects POST for release
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // No need for request body as parameters are in URL

            // Get response code
            int responseCode = connection.getResponseCode();
            return responseCode == HttpsURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public String toString() {
        return "Bed{" +
                "bedId=" + bedId +
                ", roomNumber=" + roomNumber +
                ", status='" + status + '\'' +
                ", patient='" + getPatientFullName() + '\'' +
                '}';
    }
}