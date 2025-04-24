package com.inteliMedExpress.classes.appointments;

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

public class Appointment {

    private static final String SERVER_BASE_URL = "https://springserver-kl8q.onrender.com/api/";

    private static String department = "General_Medicine";

    private static String getAppointmentsUrl() {
        return SERVER_BASE_URL + department + "/appointments";
    }

    private static String getAddAppointmentUrl() {
        return SERVER_BASE_URL + department + "/appointments/add";
    }

    private static String getUpdateAppointmentUrl(Integer id) {
        return SERVER_BASE_URL + department + "/appointments/" + id + "/update";
    }

    private static String getDeleteAppointmentUrl(Integer id) {
        return SERVER_BASE_URL + department + "/appointments/" + id + "/delete";
    }

    // Set the department for all appointments
    public static void setDepartment(String dept) {
        department = dept != null ? dept.replaceAll("\\s", "_") : "General_Medicine";
        System.out.println("Appointment department set to: " + department);
    }

    // Appointment Attributes
    private Integer appointmentId;
    private String patientFirstName; // Changed from patientName
    private String patientLastName;  // Changed from patientSurname
    private String doctorName;
    private String doctorSurname;
    private String nurseName;
    private String nurseSurname;
    private LocalDateTime appointmentDate;
    private String status;
    private String notes;
    private String createdByType; // Added field
    private String creatorName;   // Added field
    private String creatorSurname; // Added field

    // Default constructor
    public Appointment() {
        //HttpsUtil.setupSSL();
    }

    // Parameterized constructor with LocalDateTime
    public Appointment(Integer appointmentId, String patientFirstName, String patientLastName,
                       String doctorName, String doctorSurname, String nurseName, String nurseSurname,
                       LocalDateTime appointmentDate, String status, String notes) {
        this.appointmentId = appointmentId;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.doctorName = doctorName;
        this.doctorSurname = doctorSurname;
        this.nurseName = nurseName;
        this.nurseSurname = nurseSurname;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.notes = notes;
        this.createdByType = "Doctor"; // Default to Doctor
        this.creatorName = doctorName; // Default to the same doctor
        this.creatorSurname = doctorSurname;

       // HttpsUtil.setupSSL();
    }

    // Parameterized constructor with LocalDate
    public Appointment(Integer appointmentId, String patientFirstName, String patientLastName,
                       String doctorName, String doctorSurname, String nurseName, String nurseSurname,
                       LocalDate appointmentDate, String status, String notes) {
        this(appointmentId, patientFirstName, patientLastName, doctorName, doctorSurname, nurseName, nurseSurname,
                appointmentDate != null ? appointmentDate.atStartOfDay() : null,
                status, notes);
    }

    // Backward compatibility constructors
    public Appointment(Integer appointmentId, String patientFirstName, String patientLastName,
                       String doctorSurname, String nurseSurname, LocalDateTime appointmentDate,
                       String status, String notes) {
        this(appointmentId, patientFirstName, patientLastName, null, doctorSurname, null, nurseSurname,
                appointmentDate, status, notes);
    }

    public Appointment(Integer appointmentId, String patientFirstName, String patientLastName,
                       String doctorSurname, String nurseSurname, LocalDate appointmentDate,
                       String status, String notes) {
        this(appointmentId, patientFirstName, patientLastName, null, doctorSurname, null, nurseSurname,
                appointmentDate != null ? appointmentDate.atStartOfDay() : null,
                status, notes);
    }

    // Getters and Setters
    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    // Get patientName for backward compatibility
    public String getPatientName() {
        return patientFirstName;
    }

    // Set patientName for backward compatibility
    public void setPatientName(String patientName) {
        this.patientFirstName = patientName;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    // Get patientSurname for backward compatibility
    public String getPatientSurname() {
        return patientLastName;
    }

    // Set patientSurname for backward compatibility
    public void setPatientSurname(String patientSurname) {
        this.patientLastName = patientSurname;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
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

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getNurseSurname() {
        return nurseSurname;
    }

    public void setNurseSurname(String nurseSurname) {
        this.nurseSurname = nurseSurname;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    // For backward compatibility
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate != null ? appointmentDate.atStartOfDay() : null;
    }

    // Helper method to get just the date part if needed
    public LocalDate getAppointmentDateOnly() {
        return appointmentDate != null ? appointmentDate.toLocalDate() : null;
    }

    // Formatter for displaying date and time
    public String getFormattedDateTime() {
        if (appointmentDate == null) return "";
        return appointmentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedByType() {
        return createdByType;
    }

    public void setCreatedByType(String createdByType) {
        this.createdByType = createdByType;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorSurname() {
        return creatorSurname;
    }

    public void setCreatorSurname(String creatorSurname) {
        this.creatorSurname = creatorSurname;
    }

    // Get all appointments
    public static List<Appointment> getAllAppointments() throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection using the dynamic URL
            URL url = new URL(getAppointmentsUrl());
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

            // Process each appointment in the array
            for (Object obj : jsonArray) {
                JSONObject appointmentJson = (JSONObject) obj;

                // Extract appointment data with type conversion
                Long idLong = (Long) appointmentJson.get("appointmentId");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                String patientFirstName = (String) appointmentJson.get("patientFirstName");
                String patientLastName = (String) appointmentJson.get("patientLastName");
                String doctorName = (String) appointmentJson.get("doctorName");
                String doctorSurname = (String) appointmentJson.get("doctorSurname");
                String nurseName = (String) appointmentJson.get("nurseName");
                String nurseSurname = (String) appointmentJson.get("nurseSurname");
                String status = (String) appointmentJson.get("status");
                String notes = (String) appointmentJson.get("notes");
                String createdByType = (String) appointmentJson.get("createdByType");
                String creatorName = (String) appointmentJson.get("creatorName");
                String creatorSurname = (String) appointmentJson.get("creatorSurname");

                // Parse appointment date - UPDATED TO HANDLE DATETIME
                LocalDateTime appointmentDateTime = null;
                String appointmentDateStr = (String) appointmentJson.get("appointmentDate");

                if (appointmentDateStr != null && !appointmentDateStr.isEmpty()) {
                    try {
                        // Try to parse as a full datetime (2025-03-20T13:45:00)
                        appointmentDateTime = LocalDateTime.parse(appointmentDateStr);
                    } catch (Exception e) {
                        try {
                            // If that fails, try to parse with a formatter that handles both patterns
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                            appointmentDateTime = LocalDateTime.parse(appointmentDateStr, formatter);
                        } catch (Exception e2) {
                            // If all else fails, extract just the date part
                            String datePortion = appointmentDateStr.split("T")[0];
                            // Convert to LocalDateTime with time at 00:00
                            appointmentDateTime = LocalDate.parse(datePortion).atStartOfDay();
                        }
                    }
                }

                // Create and add the appointment to our list
                Appointment appointment = new Appointment(id, patientFirstName, patientLastName,
                        doctorName, doctorSurname, nurseName, nurseSurname,
                        appointmentDateTime, status, notes);

                // Set creator info
                if (createdByType != null) {
                    appointment.setCreatedByType(createdByType);
                }
                if (creatorName != null) {
                    appointment.setCreatorName(creatorName);
                }
                if (creatorSurname != null) {
                    appointment.setCreatorSurname(creatorSurname);
                }

                appointments.add(appointment);
            }

        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching appointments: " + e.getMessage());
            throw new IOException("Failed to fetch appointments", e);
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

        return appointments;
    }

    // Add a new appointment
    public boolean addToServer() throws IOException {
        URL url = new URL(getAddAppointmentUrl());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload with the correct field names
            JSONObject appointmentData = new JSONObject();
            appointmentData.put("patientFirstName", this.patientFirstName);
            appointmentData.put("patientLastName", this.patientLastName);
            appointmentData.put("doctorName", this.doctorName);
            appointmentData.put("doctorSurname", this.doctorSurname);
            appointmentData.put("nurseName", this.nurseName);
            appointmentData.put("nurseSurname", this.nurseSurname);
            // Format the LocalDateTime properly for the server
            appointmentData.put("appointmentDate", this.appointmentDate != null ?
                    this.appointmentDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
            appointmentData.put("status", this.status);
            appointmentData.put("notes", this.notes);

            // Add creator information
            appointmentData.put("createdByType", this.createdByType != null ? this.createdByType : "Doctor");
            appointmentData.put("creatorName", this.creatorName != null ? this.creatorName : this.doctorName);
            appointmentData.put("creatorSurname", this.creatorSurname != null ? this.creatorSurname : this.doctorSurname);

            // Convert JSON to string and get bytes
            String jsonInputString = appointmentData.toJSONString();
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

            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;
        } finally {
            connection.disconnect();
        }
    }

    // Update an existing appointment
    public boolean updateOnServer() throws IOException {
        if (this.appointmentId == null) {
            throw new IllegalStateException("Cannot update appointment without ID");
        }

        URL url = new URL(getUpdateAppointmentUrl(this.appointmentId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload with the correct field names
            JSONObject appointmentData = new JSONObject();
            appointmentData.put("patientFirstName", this.patientFirstName);
            appointmentData.put("patientLastName", this.patientLastName);
            appointmentData.put("doctorName", this.doctorName);
            appointmentData.put("doctorSurname", this.doctorSurname);
            appointmentData.put("nurseName", this.nurseName);
            appointmentData.put("nurseSurname", this.nurseSurname);
            // Format the LocalDateTime properly for the server
            appointmentData.put("appointmentDate", this.appointmentDate != null ?
                    this.appointmentDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
            appointmentData.put("status", this.status);
            appointmentData.put("notes", this.notes);

            // Convert JSON to string and get bytes
            String jsonInputString = appointmentData.toJSONString();
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

    // Delete an appointment
    public static boolean deleteAppointment(Integer appointmentId) throws IOException {
        URL url = new URL(getDeleteAppointmentUrl(appointmentId));
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

    // Cancel an appointment
    public boolean cancelAppointment() throws IOException {
        if (this.appointmentId == null) {
            throw new IllegalStateException("Cannot cancel appointment without ID");
        }

        URL url = new URL(SERVER_BASE_URL + department + "/appointments/" + this.appointmentId + "/cancel");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");

            // Get response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Update status locally
                this.status = "Cancelled";
                return true;
            }

            // Read error response if needed
            if (responseCode >= 400) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
                System.err.println("Error response: " + response.toString());
            }

            return false;
        } finally {
            connection.disconnect();
        }
    }

    // Complete an appointment
    public boolean completeAppointment(String additionalNotes) throws IOException {
        if (this.appointmentId == null) {
            throw new IllegalStateException("Cannot complete appointment without ID");
        }

        URL url = new URL(SERVER_BASE_URL + department + "/appointments/" + this.appointmentId + "/complete");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload if additional notes are provided
            if (additionalNotes != null && !additionalNotes.isEmpty()) {
                JSONObject notesData = new JSONObject();
                notesData.put("additionalNotes", additionalNotes);

                // Convert JSON to string and get bytes
                String jsonInputString = notesData.toJSONString();
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

                // Set content length
                connection.setRequestProperty("Content-Length", String.valueOf(input.length));

                // Write JSON data to output stream
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(input, 0, input.length);
                }
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Update status locally
                this.status = "Completed";

                // Add the additional notes to our local notes if provided
                if (additionalNotes != null && !additionalNotes.isEmpty()) {
                    if (this.notes != null && !this.notes.isEmpty()) {
                        this.notes = this.notes + "\n--- Completion Notes ---\n" + additionalNotes;
                    } else {
                        this.notes = "--- Completion Notes ---\n" + additionalNotes;
                    }
                }

                return true;
            }

            // Read error response if needed
            if (responseCode >= 400) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }
                System.err.println("Error response: " + response.toString());
            }

            return false;
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patientFirstName='" + patientFirstName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", doctorSurname='" + doctorSurname + '\'' +
                ", nurseName='" + nurseName + '\'' +
                ", nurseSurname='" + nurseSurname + '\'' +
                ", appointmentDate=" + (appointmentDate != null ? appointmentDate.format(DateTimeFormatter.ISO_DATE_TIME) : null) +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}