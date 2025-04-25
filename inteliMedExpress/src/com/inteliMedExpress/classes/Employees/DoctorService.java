package com.inteliMedExpress.classes.Employees;

import com.inteliMedExpress.utils.AppLogger;
import com.inteliMedExpress.utils.HttpsUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class DoctorService {
    // Server URLs
    private static final String SERVER_BASE_URL = "https://springserver-kl8q.onrender.com/api/";
    private static String department = "General"; // Default department

    // Dynamic URL getter that uses the current department
    private static String getDoctorsUrl() {
        return SERVER_BASE_URL + department + "/doctors";
    }

    // Set the department for all doctor operations
    public static void setDepartment(String dept) {
        department = dept != null ? dept.replaceAll("\\s", "_") : "General";
        System.out.println("Doctor service department set to: " + department);
    }

    // Constructor
    public DoctorService() {
        //HttpsUtil.setupSSL();
    }

    // Constructor with department parameter
    public DoctorService(String dept) {
        setDepartment(dept);
        //HttpsUtil.setupSSL();
    }

    // Get all doctors
    public List<Doctor> getAllDoctors() throws IOException {
        List<Doctor> doctors = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection
            URL url = new URL(getDoctorsUrl());
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

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(response.toString());

            for (Object obj : jsonArray) {
                JSONObject doctorJson = (JSONObject) obj;

                Long idLong = (Long) doctorJson.get("doctorId");
                Integer doctorId = (idLong != null) ? idLong.intValue() : null;
                String doctorName = (String) doctorJson.get("doctorName");
                String doctorSurname = (String) doctorJson.get("doctorSurname");
                String doctorPhone = (String) doctorJson.get("doctorPhone");
                String doctorEmail = (String) doctorJson.get("doctorEmail");
                String doctorAddress = (String) doctorJson.get("doctorAddress");

                Long ageLong = (Long) doctorJson.get("doctorAge");
                Integer doctorAge = (ageLong != null) ? ageLong.intValue() : null;

                String gender = (String) doctorJson.get("gender");
                String doctorSpecialty = (String) doctorJson.get("doctorSpecialty");

                Long deptIdLong = (Long) doctorJson.get("departmentId");
                Integer departmentId = (deptIdLong != null) ? deptIdLong.intValue() : null;

                String departmentName = (String) doctorJson.get("departmentName");

                LocalDateTime createdAt = null;
                String createdAtStr = (String) doctorJson.get("createdAt");
                if (createdAtStr != null && !createdAtStr.isEmpty()) {
                    try {
                        createdAt = LocalDateTime.parse(createdAtStr);
                    } catch (DateTimeParseException e) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                            createdAt = LocalDateTime.parse(createdAtStr, formatter);
                        } catch (Exception e2) {
                            System.err.println("Error parsing date: " + createdAtStr);
                        }
                    }
                }

                // Create and add the doctor to the list
                Doctor doctor = new Doctor(doctorId, doctorName, doctorSurname, doctorPhone,
                        doctorEmail, doctorAddress, doctorAge, gender, doctorSpecialty,
                        departmentId, departmentName, createdAt);
                doctors.add(doctor);
            }

        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching doctors: " + e.getMessage());
            throw new IOException("Failed to fetch doctors", e);
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

        return doctors;
    }

    // Get a specific doctor by ID
    public Doctor getDoctorById(Integer doctorId) throws IOException {
        List<Doctor> doctors = getAllDoctors();

        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId().equals(doctorId)) {
                return doctor;
            }
        }

        return null; // Doctor not found
    }
}