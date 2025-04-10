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
    // Server URLs
    private static final String SERVER_BASE_URL = "https://127.0.0.1:8080/api/General";
    private static final String GET_APPOINTMENTS_URL = SERVER_BASE_URL + "/appointments";
    private static final String ADD_APPOINTMENT_URL = SERVER_BASE_URL + "/appointments/add";
    private static final String UPDATE_APPOINTMENT_URL = SERVER_BASE_URL + "/appointments/update";
    private static final String DELETE_APPOINTMENT_URL = SERVER_BASE_URL + "/appointments/delete";

    // Appointment Attributes
    private Integer appointmentId;
    private String patientName;
    private String patientSurname;
    private String doctorSurname;
    private String nurseSurname;
    private LocalDateTime appointmentDate; // Changed from LocalDate to LocalDateTime
    private String status;
    private String notes;

    // Default constructor
    public Appointment() {
        HttpsUtil.setupSSL();
    }

    // Parameterized constructor with LocalDateTime
    public Appointment(Integer appointmentId, String patientName, String patientSurname,
                       String doctorSurname, String nurseSurname, LocalDateTime appointmentDate,
                       String status, String notes) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.doctorSurname = doctorSurname;
        this.nurseSurname = nurseSurname;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.notes = notes;

        HttpsUtil.setupSSL();
    }

    // Alternate constructor with LocalDate (for backward compatibility)
    public Appointment(Integer appointmentId, String patientName, String patientSurname,
                       String doctorSurname, String nurseSurname, LocalDate appointmentDate,
                       String status, String notes) {
        this(appointmentId, patientName, patientSurname, doctorSurname, nurseSurname,
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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSurname() {
        return patientSurname;
    }

    public void setPatientSurname(String patientSurname) {
        this.patientSurname = patientSurname;
    }

    public String getDoctorSurname() {
        return doctorSurname;
    }

    public void setDoctorSurname(String doctorSurname) {
        this.doctorSurname = doctorSurname;
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

    // Get all appointments
    public static List<Appointment> getAllAppointments() throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection
            URL url = new URL(GET_APPOINTMENTS_URL);
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

                String patientName = (String) appointmentJson.get("patientName");
                String patientSurname = (String) appointmentJson.get("patientSurname");
                String doctorSurname = (String) appointmentJson.get("doctorSurname");
                String nurseSurname = (String) appointmentJson.get("nurseSurname");
                String status = (String) appointmentJson.get("status");
                String notes = (String) appointmentJson.get("notes");

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
                Appointment appointment = new Appointment(id, patientName, patientSurname,
                        doctorSurname, nurseSurname, appointmentDateTime, status, notes);
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
        URL url = new URL(ADD_APPOINTMENT_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            JSONObject appointmentData = new JSONObject();
            appointmentData.put("patientName", this.patientName);
            appointmentData.put("patientSurname", this.patientSurname);
            appointmentData.put("doctorSurname", this.doctorSurname);
            appointmentData.put("nurseSurname", this.nurseSurname);
            // Format the LocalDateTime properly for the server
            appointmentData.put("appointmentDate", this.appointmentDate != null ?
                    this.appointmentDate.format(DateTimeFormatter.ISO_DATE_TIME) : null);
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
            return responseCode == HttpURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    // Update an existing appointment
    public boolean updateOnServer() throws IOException {
        URL url = new URL(UPDATE_APPOINTMENT_URL + "/" + this.appointmentId);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload with appointment ID
            JSONObject appointmentData = new JSONObject();
            appointmentData.put("appointmentId", this.appointmentId);
            appointmentData.put("patientName", this.patientName);
            appointmentData.put("patientSurname", this.patientSurname);
            appointmentData.put("doctorSurname", this.doctorSurname);
            appointmentData.put("nurseSurname", this.nurseSurname);
            // Format the LocalDateTime properly for the server
            appointmentData.put("appointmentDate", this.appointmentDate != null ?
                    this.appointmentDate.format(DateTimeFormatter.ISO_DATE_TIME) : null);
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
            return responseCode == HttpURLConnection.HTTP_OK;
        } finally {
            connection.disconnect();
        }
    }

    // Delete an appointment
    public static boolean deleteAppointment(Integer appointmentId) throws IOException {
        URL url = new URL(DELETE_APPOINTMENT_URL + "/" + appointmentId);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
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
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", patientName='" + patientName + '\'' +
                ", patientSurname='" + patientSurname + '\'' +
                ", doctorSurname='" + doctorSurname + '\'' +
                ", nurseSurname='" + nurseSurname + '\'' +
                ", appointmentDate=" + (appointmentDate != null ? appointmentDate.format(DateTimeFormatter.ISO_DATE_TIME) : null) +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}