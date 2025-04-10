package com.inteliMedExpress.classes.patients;

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

public class Patient {
    // Server URLs
    private static final String SERVER_BASE_URL = "https://127.0.0.1:8080/api/General";
    private static final String GET_PATIENTS_URL = SERVER_BASE_URL + "/patients";
    private static final String ADD_PATIENT_URL = SERVER_BASE_URL + "/patients/add";
    private static final String UPDATE_PATIENT_URL = SERVER_BASE_URL + "/patients/update";
    private static final String DELETE_PATIENT_URL = SERVER_BASE_URL + "/patients/delete";

    // Patient Attributes
    private Integer patientId;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String address;
    private Integer age;
    private String gender;
    private LocalDate birthDate;
    private String status;

    // Constructors
    public Patient() {
       HttpsUtil.setupSSL();


    }

    public Patient(Integer patientId, String name, String surname, String email,
                   String phone, String address, Integer age, String gender,
                   LocalDate birthDate, String status) {
        this.patientId = patientId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.birthDate = birthDate;
        this.status = status;

        HttpsUtil.setupSSL();
/*
        if (!HttpsUtil.isSSLInitialized()) {
            HttpsUtil.setupSSL();
        }
        */

    }

    // Getters and Setters
    public Integer getPatientId() { return patientId; }
    public void setPatientId(Integer patientId) { this.patientId = patientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Get all patients
    public static List<Patient> getAllPatients() throws IOException {
        List<Patient> patients = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
/*
            if (!HttpsUtil.isSSLInitialized()) {
                HttpsUtil.setupSSL();
            }
            /*
 */
            // Set up the connection
            URL url = new URL(GET_PATIENTS_URL);
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

            // Process each patient in the array
            for (Object obj : jsonArray) {
                JSONObject patientJson = (JSONObject) obj;

                // Extract patient data with type conversion
                Long idLong = (Long) patientJson.get("id");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                String name = (String) patientJson.get("firstName");
                String surname = (String) patientJson.get("lastName");
                String email = (String) patientJson.get("email");
                String phone = (String) patientJson.get("phoneNumber");
                String address = (String) patientJson.get("address");

                Long ageLong = (Long) patientJson.get("age");
                Integer age = (ageLong != null) ? ageLong.intValue() : null;

                String gender = (String) patientJson.get("gender");
                String status = (String) patientJson.get("status");

                // Parse birth date
                LocalDate birthDate = null;
                String birthDateStr = (String) patientJson.get("dateOfBirth");
                if (birthDateStr != null && !birthDateStr.isEmpty()) {
                    birthDate = LocalDate.parse(birthDateStr);
                }

                // Create and add the patient to our list
                Patient patient = new Patient(id, name, surname, email, phone,
                        address, age, gender, birthDate, status);
                patients.add(patient);
            }

        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching patients: " + e.getMessage());
            throw new IOException("Failed to fetch patients", e);
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

        return patients;
    }

    // Add a new patient
    public boolean addToServer() throws IOException {
        URL url = new URL(ADD_PATIENT_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
    /*
            if (!HttpsUtil.isSSLInitialized()) {
                HttpsUtil.setupSSL();
            }

     */
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            JSONObject patientData = new JSONObject();
            patientData.put("firstName", this.name);
            patientData.put("lastName", this.surname);
            patientData.put("email", this.email);
            patientData.put("phoneNumber", this.phone);
            patientData.put("address", this.address);
            patientData.put("age", this.age);
            patientData.put("gender", this.gender);
            patientData.put("dateOfBirth", this.birthDate != null ? this.birthDate.toString() : null);
            patientData.put("status", this.status);

            // Convert JSON to string and get bytes
            String jsonInputString = patientData.toJSONString();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpsURLConnection.HTTP_CREATED;
        } finally {
            connection.disconnect();
        }
    }

    // Update an existing patient
    public boolean updateOnServer() throws IOException {
        URL url = new URL(UPDATE_PATIENT_URL + "/" + this.patientId);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            /*
            if (!HttpsUtil.isSSLInitialized()) {
                HttpsUtil.setupSSL();
            }

 */
            // Set up the HTTP request
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload with patient ID
            JSONObject patientData = new JSONObject();
            patientData.put("id", this.patientId);
            patientData.put("firstName", this.name);
            patientData.put("lastName", this.surname);
            patientData.put("email", this.email);
            patientData.put("phoneNumber", this.phone);
            patientData.put("address", this.address);
            patientData.put("age", this.age);
            patientData.put("gender", this.gender);
            patientData.put("dateOfBirth", this.birthDate != null ? this.birthDate.toString() : null);
            patientData.put("status", this.status);

            // Convert JSON to string and get bytes
            String jsonInputString = patientData.toJSONString();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();
            return responseCode == HttpsURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    // Delete a patient
    public static boolean deletePatient(Integer patientId) throws IOException {
        URL url = new URL(DELETE_PATIENT_URL + "/" + patientId);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            /*
            if (!HttpsUtil.isSSLInitialized()) {
                HttpsUtil.setupSSL();
            }

             */
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
        return "Patient{" +
                "patientId=" + patientId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}