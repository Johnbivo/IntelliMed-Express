package com.inteliMedExpress.classes;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patient {



    //Server urls
    private static final String SERVER_BASE_URL = "http://localhost:8080/api";
    private static final String GET_PATIENTS_URL = SERVER_BASE_URL + "/patients";
    private static final String ADD_PATIENT_URL = SERVER_BASE_URL + "/patients/add";
    private static final String UPDATE_PATIENT_URL = SERVER_BASE_URL + "/patients/update";
    private static final String DELETE_PATIENT_URL = SERVER_BASE_URL + "/patients/delete";


    //Patient Attributes
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



    //Constructors
    public Patient() {}

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
    }


    //Getters Setters
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


    //Get all patients
    public static List<Patient> getAllPatients() throws IOException {
        // This would be implemented to make an HTTP GET request to your server
        // and parse the JSON response into a list of Patient objects
        // For now, we'll return dummy data
        return new ArrayList<>();
    }


    // Add a new patient
    public boolean addToServer() throws IOException {
        URL url = new URL(ADD_PATIENT_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set up the HTTP request
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Create JSON payload
        JSONObject patientData = new JSONObject();
        patientData.put("name", this.name);
        patientData.put("surname", this.surname);
        patientData.put("email", this.email);
        patientData.put("phone", this.phone);
        patientData.put("address", this.address);
        patientData.put("age", this.age);
        patientData.put("gender", this.gender);
        patientData.put("birthDate", this.birthDate != null ? this.birthDate.toString() : null);
        patientData.put("status", this.status);

        // Convert JSON to string and get bytes
        String jsonInputString = patientData.toString();
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
    }



    // Update an existing patient
    public boolean updateOnServer() throws IOException {
        // Similar to addToServer but using PUT method and including the patientId
        URL url = new URL(UPDATE_PATIENT_URL + "/" + this.patientId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set up the HTTP request
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Create JSON payload with patient ID
        JSONObject patientData = new JSONObject();
        patientData.put("patientId", this.patientId);
        patientData.put("name", this.name);
        patientData.put("surname", this.surname);
        patientData.put("email", this.email);
        patientData.put("phone", this.phone);
        patientData.put("address", this.address);
        patientData.put("age", this.age);
        patientData.put("gender", this.gender);
        patientData.put("birthDate", this.birthDate != null ? this.birthDate.toString() : null);
        patientData.put("status", this.status);

        // Convert JSON to string and get bytes
        String jsonInputString = patientData.toString();
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
    }

    // Delete a patient
    public static boolean deletePatient(Integer patientId) throws IOException {
        URL url = new URL(DELETE_PATIENT_URL + "/" + patientId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set up the HTTP request
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);

        // Get response code
        int responseCode = connection.getResponseCode();
        return responseCode == HttpURLConnection.HTTP_OK;
    }



}




