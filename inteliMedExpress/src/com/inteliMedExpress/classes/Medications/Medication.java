package com.inteliMedExpress.classes.Medications;

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Medication {

    private static final String SERVER_BASE_URL = "https://springserver-kl8q.onrender.com/api/";

    private static String department = "Pharmacology";

    private static String getMedicationsUrl() {
        return SERVER_BASE_URL + department + "/medications";
    }

    private static String getAddMedicationUrl() {
        return SERVER_BASE_URL + department + "/medications/add";
    }

    private static String getUpdateMedicationUrl(Integer id) {
        return SERVER_BASE_URL + department + "/medications/" + id + "/update";
    }

    private static String getDeleteMedicationUrl(Integer id) {
        return SERVER_BASE_URL + department + "/medications/" + id + "/delete";
    }

    // Set the department for all medications
    public static void setDepartment(String dept) {
        department = dept != null ? dept.replaceAll("\\s", "_") : "Pharmacy";
        System.out.println("Medication department set to: " + department);
    }

    // Medication Attributes
    private Integer medicationId;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private LocalDate expirationDate;
    private Integer departmentId;
    private String departmentName;

    // Default constructor
    public Medication() {
        //HttpsUtil.setupSSL();
    }

    // Parameterized constructor
    public Medication(Integer medicationId, String name, String description, Double price,
                      Integer stockQuantity, LocalDate expirationDate,
                      Integer departmentId, String departmentName) {
        this.medicationId = medicationId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.expirationDate = expirationDate;
        this.departmentId = departmentId;
        this.departmentName = departmentName;

        //HttpsUtil.setupSSL();
    }

    // Constructor with minimum required fields
    public Medication(String name, String description, Double price, Integer stockQuantity, LocalDate expirationDate) {
        this(null, name, description, price, stockQuantity, expirationDate, 5, "Pharmacy");
    }

    // Getters and Setters
    public Integer getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Integer medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationName() {
        return name;
    }

    public void setMedicationName(String name) {
        this.name = name;
    }

    // Alternative getter for name field
    public String getName() {
        return name;
    }

    // Alternative setter for name field
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
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

    public String getFormattedExpirationDate() {
        if (expirationDate == null) return "Not specified";
        return expirationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static List<Medication> getAllMedications() throws IOException {
        List<Medication> medications = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(getMedicationsUrl());
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

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

            // Process each medication in the array
            for (Object obj : jsonArray) {
                JSONObject medicationJson = (JSONObject) obj;

                // Extract medication data with type conversion
                Long idLong = (Long) medicationJson.get("medicationId");
                Integer id = (idLong != null) ? idLong.intValue() : null;

                String name = (String) medicationJson.get("name");
                String description = (String) medicationJson.get("description");

                Object priceObj = medicationJson.get("price");
                Double price = null;
                if (priceObj instanceof Double) {
                    price = (Double) priceObj;
                } else if (priceObj instanceof Long) {
                    price = ((Long) priceObj).doubleValue();
                } else if (priceObj instanceof String) {
                    price = Double.parseDouble((String) priceObj);
                }

                Long stockQuantityLong = (Long) medicationJson.get("stockQuantity");
                Integer stockQuantity = (stockQuantityLong != null) ? stockQuantityLong.intValue() : null;

                Long departmentIdLong = (Long) medicationJson.get("departmentId");
                Integer departmentId = (departmentIdLong != null) ? departmentIdLong.intValue() : null;

                String departmentName = (String) medicationJson.get("departmentName");

                // Parse expiration date
                LocalDate expirationDate = null;
                String expirationDateStr = (String) medicationJson.get("expirationDate");

                if (expirationDateStr != null && !expirationDateStr.isEmpty()) {
                    try {
                        expirationDate = LocalDate.parse(expirationDateStr);
                    } catch (Exception e) {
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            expirationDate = LocalDate.parse(expirationDateStr, formatter);
                        } catch (Exception e2) {
                            System.err.println("Could not parse date: " + expirationDateStr);
                        }
                    }
                }

                // Create and add the medication to our list
                Medication medication = new Medication(id, name, description, price,
                        stockQuantity, expirationDate,
                        departmentId, departmentName);

                medications.add(medication);
            }

        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching medications: " + e.getMessage());
            throw new IOException("Failed to fetch medications", e);
        } finally {
            // Clean up resources
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.err.println("Error closing stream: " + e.getMessage());
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return medications;
    }
    public boolean addToServer() throws IOException {
        URL url = new URL(getAddMedicationUrl());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            JSONObject medicationData = new JSONObject();
            medicationData.put("name", this.name);
            medicationData.put("description", this.description);
            medicationData.put("price", this.price);
            medicationData.put("stockQuantity", this.stockQuantity);
            // Format the LocalDate properly for the server
            medicationData.put("expirationDate", this.expirationDate != null ?
                    this.expirationDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : null);
            medicationData.put("departmentId", this.departmentId);
            medicationData.put("departmentName", this.departmentName);

            // Convert JSON to string and get bytes
            String jsonInputString = medicationData.toJSONString();
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
                    Long idLong = (Long) jsonResponse.get("medicationId");
                    if (idLong != null) {
                        this.medicationId = idLong.intValue();
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

    // Update an existing medication - only available for pharmacy staff
    public boolean updateOnServer() throws IOException {
        if (this.medicationId == null) {
            throw new IllegalStateException("Cannot update medication without ID");
        }

        URL url = new URL(getUpdateMedicationUrl(this.medicationId));
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            JSONObject medicationData = new JSONObject();
            medicationData.put("name", this.name);
            medicationData.put("description", this.description);
            medicationData.put("price", this.price);
            medicationData.put("stockQuantity", this.stockQuantity);
            // Format the LocalDate properly for the server
            medicationData.put("expirationDate", this.expirationDate != null ?
                    this.expirationDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : null);
            medicationData.put("departmentId", this.departmentId);
            medicationData.put("departmentName", this.departmentName);

            // Convert JSON to string and get bytes
            String jsonInputString = medicationData.toJSONString();
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

    // Delete a medication - only available for pharmacy staff
    public static boolean deleteMedication(Integer medicationId) throws IOException {
        URL url = new URL(getDeleteMedicationUrl(medicationId));
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

    // Update stock quantity - a specialized method for pharmacists
    public boolean updateStock(int newQuantity) throws IOException {
        if (this.medicationId == null) {
            throw new IllegalStateException("Cannot update stock without medication ID");
        }

        URL url = new URL(SERVER_BASE_URL + department + "/medications/" + this.medicationId + "/update-stock");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        try {
            // Set up the HTTP request
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");

            // Create JSON payload
            JSONObject stockData = new JSONObject();
            stockData.put("quantityChange", newQuantity);

            // Convert JSON to string and get bytes
            String jsonInputString = stockData.toJSONString();
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

            // Set content length
            connection.setRequestProperty("Content-Length", String.valueOf(input.length));

            // Write JSON data to output stream
            try (OutputStream outputStream = connection.getOutputStream()) {
                outputStream.write(input, 0, input.length);
            }

            // Get response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Update the stock quantity locally
                this.stockQuantity = newQuantity;
                return true;
            }

            // Read error response if needed
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            System.err.println("Error response: " + response.toString());

            return false;
        } finally {
            connection.disconnect();
        }
    }

    @Override
    public String toString() {
        return "Medication{" +
                "medicationId=" + medicationId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", expirationDate=" + (expirationDate != null ? expirationDate.format(DateTimeFormatter.ISO_DATE) : null) +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}