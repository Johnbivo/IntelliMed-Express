package com.inteliMedExpress.classes.labTests;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LabTest {
    private static final String SERVER_BASE_URL = "https://127.0.0.1:8080/api/";

    private static String department = "General";

    private static String getLabTestsUrl() {
        return SERVER_BASE_URL + department + "/labtests";
    }

    private static String getRequestLabTestUrl() {
        return SERVER_BASE_URL + department + "/labtests/request";
    }

    private static String getAssignDoctorUrl(Integer id) {
        return SERVER_BASE_URL + department + "/labtests/" + id + "/assign-doctor";
    }

    private static String getCompleteLabTestUrl(Integer id) {
        return SERVER_BASE_URL + department + "/labtests/" + id + "/complete";
    }

    private static String getDeleteLabTestUrl(Integer id) {
        return SERVER_BASE_URL + department + "/labtests/" + id;
    }



    // Set the department for all lab tests
    public static void setDepartment(String dept) {
        department = dept != null ? dept : "General";
        System.out.println("LabTest department set to: " + department);
    }

    // Lab Test attributes
    private Integer testId;
    private String testType;
    private Integer patientId;
    private String patientName;
    private String patientSurname;
    private Integer doctorId;
    private String doctorName;
    private String doctorSurname;
    private String doctorSpecialty;
    private LocalDateTime orderDate;
    private LocalDateTime completionDate;
    private String result;
    private String status;
    private String notes;
    private Integer departmentId;
    private String departmentName;
    private Integer requestingDoctorId;
    private String requestingDoctorName;
    private String requestingDoctorSurname;
    private String requestingDoctorSpecialty;
    private Integer requestingDepartmentId;
    private String requestingDepartmentName;

    // Default constructor
    public LabTest() {
        HttpsUtil.setupSSL();
    }

    // Constructor with essential fields
    public LabTest(String testType, Integer patientId, String patientName, String patientSurname,
                   Integer doctorId, String doctorName, String doctorSurname, String departmentName) {
        this.testType = testType;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientSurname = patientSurname;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.doctorSurname = doctorSurname;
        this.departmentName = departmentName;
        this.orderDate = LocalDateTime.now();
        this.status = "Ordered";

        HttpsUtil.setupSSL();
    }

    // Getters and Setters
    public Integer getTestId() {
        return testId;
    }

    public void setTestId(Integer testId) {
        this.testId = testId;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
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

    public String getDoctorSpecialty() {
        return doctorSpecialty;
    }

    public void setDoctorSpecialty(String doctorSpecialty) {
        this.doctorSpecialty = doctorSpecialty;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public Integer getRequestingDoctorId() {
        return requestingDoctorId;
    }

    public void setRequestingDoctorId(Integer requestingDoctorId) {
        this.requestingDoctorId = requestingDoctorId;
    }

    public String getRequestingDoctorName() {
        return requestingDoctorName;
    }

    public void setRequestingDoctorName(String requestingDoctorName) {
        this.requestingDoctorName = requestingDoctorName;
    }

    public String getRequestingDoctorSurname() {
        return requestingDoctorSurname;
    }

    public void setRequestingDoctorSurname(String requestingDoctorSurname) {
        this.requestingDoctorSurname = requestingDoctorSurname;
    }

    public String getRequestingDoctorSpecialty() {
        return requestingDoctorSpecialty;
    }

    public void setRequestingDoctorSpecialty(String requestingDoctorSpecialty) {
        this.requestingDoctorSpecialty = requestingDoctorSpecialty;
    }

    public Integer getRequestingDepartmentId() {
        return requestingDepartmentId;
    }

    public void setRequestingDepartmentId(Integer requestingDepartmentId) {
        this.requestingDepartmentId = requestingDepartmentId;
    }

    public String getRequestingDepartmentName() {
        return requestingDepartmentName;
    }

    public void setRequestingDepartmentName(String requestingDepartmentName) {
        this.requestingDepartmentName = requestingDepartmentName;
    }

    // Helper methods for the requesting workflow
    public void setAsRequestingDoctor(Integer doctorId, String doctorName, String doctorSurname, String specialty, Integer departmentId, String departmentName) {
        this.requestingDoctorId = doctorId;
        this.requestingDoctorName = doctorName;
        this.requestingDoctorSurname = doctorSurname;
        this.requestingDoctorSpecialty = specialty;
        this.requestingDepartmentId = departmentId;
        this.requestingDepartmentName = departmentName;
    }

    // Helper method to get patient full name
    public String getPatientFullName() {
        return this.patientName + " " + this.patientSurname;
    }

    // Helper method to get doctor full name
    public String getDoctorFullName() {
        return this.doctorName + " " + this.doctorSurname;
    }

    // Helper method to get requesting doctor full name
    public String getRequestingDoctorFullName() {
        return this.requestingDoctorName + " " + this.requestingDoctorSurname;
    }

    // Formatter for displaying date and time
    public String getFormattedOrderDate() {
        if (orderDate == null) return "";
        return orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public String getFormattedCompletionDate() {
        if (completionDate == null) return "";
        return completionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    // Helper method to get department ID by name
    private Integer getDepartmentIdByName(String departmentName) {
        // This maps department names to their IDs based on the database
        switch (departmentName) {
            case "Cardiology": return 1;
            case "Pediatrics": return 2;
            case "General": return 3;
            case "Microbiology": return 4;
            case "Pharmacy": return 5;
            case "Radiology": return 6;
            default: return 3; // Default to General department
        }
    }

    private static String getRequestedLabTestsUrl() {
        return SERVER_BASE_URL + department + "/labtests/requested";
    }

    public static List<LabTest> getRequestedLabTests() throws IOException {
        List<LabTest> requestedTests = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(getRequestedLabTestsUrl());
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Reuse the JSON parsing logic from getAllLabTests
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(response.toString());

            for (Object obj : jsonArray) {
                JSONObject labTestJson = (JSONObject) obj;
                // You can refactor the repeated parsing logic into a shared method to reduce duplication
                LabTest test = parseLabTestFromJson(labTestJson); // optional: extract into a helper
                requestedTests.add(test);
            }
        } catch (ParseException e) {
            throw new IOException("Failed to parse requested lab tests", e);
        } finally {
            if (reader != null) reader.close();
            if (connection != null) connection.disconnect();
        }

        return requestedTests;
    }
    private static LabTest parseLabTestFromJson(JSONObject labTestJson) {
        LabTest labTest = new LabTest();

        try {
            // Convert and assign all fields
            Long idLong = (Long) labTestJson.get("testId");
            labTest.setTestId(idLong != null ? idLong.intValue() : null);

            labTest.setTestType((String) labTestJson.get("testType"));

            Long patientIdLong = (Long) labTestJson.get("patientId");
            labTest.setPatientId(patientIdLong != null ? patientIdLong.intValue() : null);
            labTest.setPatientName((String) labTestJson.get("patientName"));
            labTest.setPatientSurname((String) labTestJson.get("patientSurname"));

            Long doctorIdLong = (Long) labTestJson.get("doctorId");
            labTest.setDoctorId(doctorIdLong != null ? doctorIdLong.intValue() : null);
            labTest.setDoctorName((String) labTestJson.get("doctorName"));
            labTest.setDoctorSurname((String) labTestJson.get("doctorSurname"));
            labTest.setDoctorSpecialty((String) labTestJson.get("doctorSpecialty"));

            labTest.setResult((String) labTestJson.get("result"));
            labTest.setStatus((String) labTestJson.get("status"));
            labTest.setNotes((String) labTestJson.get("notes"));

            Long departmentIdLong = (Long) labTestJson.get("departmentId");
            labTest.setDepartmentId(departmentIdLong != null ? departmentIdLong.intValue() : null);
            labTest.setDepartmentName((String) labTestJson.get("departmentName"));

            Long reqDocIdLong = (Long) labTestJson.get("requestingDoctorId");
            labTest.setRequestingDoctorId(reqDocIdLong != null ? reqDocIdLong.intValue() : null);
            labTest.setRequestingDoctorName((String) labTestJson.get("requestingDoctorName"));
            labTest.setRequestingDoctorSurname((String) labTestJson.get("requestingDoctorSurname"));
            labTest.setRequestingDoctorSpecialty((String) labTestJson.get("requestingDoctorSpecialty"));

            Long reqDeptIdLong = (Long) labTestJson.get("requestingDepartmentId");
            labTest.setRequestingDepartmentId(reqDeptIdLong != null ? reqDeptIdLong.intValue() : null);
            labTest.setRequestingDepartmentName((String) labTestJson.get("requestingDepartmentName"));

            // Date parsing
            String orderDateStr = (String) labTestJson.get("orderDate");
            if (orderDateStr != null && !orderDateStr.isEmpty()) {
                try {
                    labTest.setOrderDate(LocalDateTime.parse(orderDateStr));
                } catch (Exception e) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                        labTest.setOrderDate(LocalDateTime.parse(orderDateStr, formatter));
                    } catch (Exception ignored) {}
                }
            }

            String completionDateStr = (String) labTestJson.get("completionDate");
            if (completionDateStr != null && !completionDateStr.isEmpty()) {
                try {
                    labTest.setCompletionDate(LocalDateTime.parse(completionDateStr));
                } catch (Exception e) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                        labTest.setCompletionDate(LocalDateTime.parse(completionDateStr, formatter));
                    } catch (Exception ignored) {}
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing LabTest JSON: " + e.getMessage());
        }

        return labTest;
    }


    // Get all lab tests
    public static List<LabTest> getAllLabTests() throws IOException {
        List<LabTest> labTests = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection using the dynamic URL
            URL url = new URL(getLabTestsUrl());
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

            // Process each lab test in the array
            for (Object obj : jsonArray) {
                JSONObject labTestJson = (JSONObject) obj;

                // Extract lab test data with type conversion
                Long idLong = (Long) labTestJson.get("testId");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                String testType = (String) labTestJson.get("testType");

                Long patientIdLong = (Long) labTestJson.get("patientId");
                Integer patientId = (patientIdLong != null) ? patientIdLong.intValue() : null;

                String patientName = (String) labTestJson.get("patientName");
                String patientSurname = (String) labTestJson.get("patientSurname");

                Long doctorIdLong = (Long) labTestJson.get("doctorId");
                Integer doctorId = (doctorIdLong != null) ? doctorIdLong.intValue() : null;

                String doctorName = (String) labTestJson.get("doctorName");
                String doctorSurname = (String) labTestJson.get("doctorSurname");
                String doctorSpecialty = (String) labTestJson.get("doctorSpecialty");

                String result = (String) labTestJson.get("result");
                String status = (String) labTestJson.get("status");
                String notes = (String) labTestJson.get("notes");

                Long departmentIdLong = (Long) labTestJson.get("departmentId");
                Integer departmentId = (departmentIdLong != null) ? departmentIdLong.intValue() : null;

                String departmentName = (String) labTestJson.get("departmentName");

                Long requestingDoctorIdLong = (Long) labTestJson.get("requestingDoctorId");
                Integer requestingDoctorId = (requestingDoctorIdLong != null) ? requestingDoctorIdLong.intValue() : null;

                String requestingDoctorName = (String) labTestJson.get("requestingDoctorName");
                String requestingDoctorSurname = (String) labTestJson.get("requestingDoctorSurname");
                String requestingDoctorSpecialty = (String) labTestJson.get("requestingDoctorSpecialty");

                Long requestingDepartmentIdLong = (Long) labTestJson.get("requestingDepartmentId");
                Integer requestingDepartmentId = (requestingDepartmentIdLong != null) ? requestingDepartmentIdLong.intValue() : null;

                String requestingDepartmentName = (String) labTestJson.get("requestingDepartmentName");

                // Parse dates
                LocalDateTime orderDate = null;
                String orderDateStr = (String) labTestJson.get("orderDate");
                if (orderDateStr != null && !orderDateStr.isEmpty()) {
                    try {
                        // Try to parse as a full datetime
                        orderDate = LocalDateTime.parse(orderDateStr);
                    } catch (Exception e) {
                        try {
                            // If that fails, try with a formatter
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                            orderDate = LocalDateTime.parse(orderDateStr, formatter);
                        } catch (Exception e2) {
                            System.err.println("Could not parse order date: " + orderDateStr);
                        }
                    }
                }

                LocalDateTime completionDate = null;
                String completionDateStr = (String) labTestJson.get("completionDate");
                if (completionDateStr != null && !completionDateStr.isEmpty()) {
                    try {
                        // Try to parse as a full datetime
                        completionDate = LocalDateTime.parse(completionDateStr);
                    } catch (Exception e) {
                        try {
                            // If that fails, try with a formatter
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd['T'HH:mm:ss]");
                            completionDate = LocalDateTime.parse(completionDateStr, formatter);
                        } catch (Exception e2) {
                            System.err.println("Could not parse completion date: " + completionDateStr);
                        }
                    }
                }

                // Create lab test and set all properties
                LabTest labTest = new LabTest(testType, patientId, patientName, patientSurname,
                        doctorId, doctorName, doctorSurname, departmentName);

                labTest.setTestId(id);
                labTest.setDoctorSpecialty(doctorSpecialty);
                labTest.setOrderDate(orderDate);
                labTest.setCompletionDate(completionDate);
                labTest.setResult(result);
                labTest.setStatus(status);
                labTest.setNotes(notes);
                labTest.setDepartmentId(departmentId);
                labTest.setRequestingDoctorId(requestingDoctorId);
                labTest.setRequestingDoctorName(requestingDoctorName);
                labTest.setRequestingDoctorSurname(requestingDoctorSurname);
                labTest.setRequestingDoctorSpecialty(requestingDoctorSpecialty);
                labTest.setRequestingDepartmentId(requestingDepartmentId);
                labTest.setRequestingDepartmentName(requestingDepartmentName);

                labTests.add(labTest);
            }

        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching lab tests: " + e.getMessage());
            throw new IOException("Failed to fetch lab tests", e);
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

        return labTests;
    }

    // Request a new lab test
    public boolean requestLabTest() throws IOException {
        URL url = new URL(getRequestLabTestUrl());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Get the departmentId for the target department
            Integer targetDepartmentId = getDepartmentIdByName(this.departmentName);

            // Create JSON payload structured according to server expectations
            JSONObject labTestData = new JSONObject();

            // Required fields for the server's createLabTestRequestWithNames method
            labTestData.put("patientId", this.patientId);
            labTestData.put("requestingDoctorId", this.requestingDoctorId);
            labTestData.put("requestingDepartmentId", this.requestingDepartmentId);
            labTestData.put("targetDepartmentId", targetDepartmentId);
            labTestData.put("targetDepartmentName", this.departmentName);
            labTestData.put("testType", this.testType);
            labTestData.put("notes", this.notes);

            // Convert JSON to string and get bytes
            String jsonInputString = labTestData.toJSONString();
            System.out.println("Sending request to: " + url);
            System.out.println("Request payload: " + jsonInputString);

            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code and process response
            int responseCode = connection.getResponseCode();
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            System.out.println("Response code: " + responseCode);
            System.out.println("Response body: " + response.toString());

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                // Parse the response to update this lab test object
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject responseJson = (JSONObject) parser.parse(response.toString());

                    // Update this object with the server response data
                    Long testIdLong = (Long) responseJson.get("testId");
                    if (testIdLong != null) {
                        this.testId = testIdLong.intValue();
                    }

                    // Update status based on server's enum
                    String status = (String) responseJson.get("status");
                    if (status != null) {
                        this.status = status;
                    }

                    // Update other fields from response
                    String result = (String) responseJson.get("result");
                    if (result != null) {
                        this.result = result;
                    }

                    // Update dates if present
                    String orderDateStr = (String) responseJson.get("orderDate");
                    if (orderDateStr != null) {
                        try {
                            this.orderDate = LocalDateTime.parse(orderDateStr);
                        } catch (Exception e) {
                            System.err.println("Could not parse order date: " + orderDateStr);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Could not parse response: " + e.getMessage());
                }
                return true;
            } else {
                System.err.println("Error creating lab test: " + responseCode + " - " + response.toString());
                return false;
            }
        } finally {
            connection.disconnect();
        }
    }

    // Assign a doctor to a lab test
    public boolean assignDoctor(Integer doctorId) throws IOException {
        if (this.testId == null) {
            throw new IllegalStateException("Cannot assign doctor to test without test ID");
        }

        URL url = new URL(getAssignDoctorUrl(this.testId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload with just the doctor ID
            JSONObject payload = new JSONObject();
            payload.put("doctorId", doctorId);

            // Convert JSON to string and get bytes
            String jsonInputString = payload.toJSONString();
            System.out.println("Assigning doctor to test: " + this.testId);
            System.out.println("Request payload: " + jsonInputString);

            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            // Read response, even if there's an error
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            System.out.println("Response code: " + responseCode);
            System.out.println("Response body: " + response.toString());

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Update local state with the doctor ID
                this.doctorId = doctorId;
                this.status = "Ordered"; // Changed from "In Progress" to match database enum
                return true;
            } else {
                System.err.println("Error assigning doctor to test: " + responseCode + " - " + response.toString());
                return false;
            }
        } finally {
            connection.disconnect();
        }
    }

    // Complete a lab test with results
    public boolean completeTest(String result, String notes) throws IOException {
        if (this.testId == null) {
            throw new IllegalStateException("Cannot complete test without test ID");
        }

        URL url = new URL(getCompleteLabTestUrl(this.testId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload with the results
            JSONObject payload = new JSONObject();
            payload.put("result", result);
            payload.put("notes", notes);
            payload.put("completionDate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            payload.put("status", "Completed");

            // Convert JSON to string and get bytes
            String jsonInputString = payload.toJSONString();
            System.out.println("Completing test: " + this.testId);
            System.out.println("Request payload: " + jsonInputString);

            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            // Read response, even if there's an error
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            System.out.println("Response code: " + responseCode);
            System.out.println("Response body: " + response.toString());

            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                // Update local state
                this.result = result;
                this.notes = notes;
                this.completionDate = LocalDateTime.now();
                this.status = "Completed";
                return true;
            } else {
                System.err.println("Error completing test: " + responseCode + " - " + response.toString());
                return false;
            }
        } finally {
            connection.disconnect();
        }
    }

    // Delete a lab test
    public static boolean deleteLabTest(Integer testId) throws IOException {
        URL url = new URL(getDeleteLabTestUrl(testId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Accept", "application/json");

            // Get response code
            int responseCode = connection.getResponseCode();

            // Read response, even if there's an error
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode < 400 ? connection.getInputStream() : connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            System.out.println("Response code: " + responseCode);
            System.out.println("Response body: " + response.toString());

            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public String toString() {
        return "LabTest{" +
                "testId=" + testId +
                ", testType='" + testType + '\'' +
                ", patientName='" + patientName + '\'' +
                ", patientSurname='" + patientSurname + '\'' +
                ", doctorName='" + doctorName + '\'' +
                ", doctorSurname='" + doctorSurname + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}