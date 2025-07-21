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

public class NurseService {
    // Server URLs
    private static final String SERVER_BASE_URL = "https://springserver-kl8q.onrender.com/api/";
    private static String department = "General"; // Default department

    // Dynamic URL getter that uses the current department
    private static String getNursesUrl() {
        return SERVER_BASE_URL + department + "/nurses";
    }

    // Set the department for all nurse operations
    public static void setDepartment(String dept) {
        department = dept != null ? dept.replaceAll("\\s", "_") : "General";
        System.out.println("Nurse service department set to: " + department);
    }

    // Constructor
    public NurseService() {
        //HttpsUtil.setupSSL();
    }

    // Constructor with department parameter
    public NurseService(String dept) {
        setDepartment(dept);
        //HttpsUtil.setupSSL();
    }

    // Get all nurses
    public List<Nurse> getAllNurses() throws IOException {
        List<Nurse> nurses = new ArrayList<>();
        HttpsURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Set up the connection
            URL url = new URL(getNursesUrl());
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
                JSONObject nurseJson = (JSONObject) obj;

                Long idLong = (Long) nurseJson.get("nurseId");
                Integer nurseId = (idLong != null) ? idLong.intValue() : null;
                String nurseName = (String) nurseJson.get("nurseName");
                String nurseSurname = (String) nurseJson.get("nurseSurname");
                String nursePhone = (String) nurseJson.get("nursePhone");
                String nurseEmail = (String) nurseJson.get("nurseEmail");
                String nurseAddress = (String) nurseJson.get("nurseAddress");

                Long ageLong = (Long) nurseJson.get("nurseAge");
                Integer nurseAge = (ageLong != null) ? ageLong.intValue() : null;

                String gender = (String) nurseJson.get("gender");
                String nurseSpecialty = (String) nurseJson.get("nurseSpecialty");

                Long deptIdLong = (Long) nurseJson.get("departmentId");
                Integer departmentId = (deptIdLong != null) ? deptIdLong.intValue() : null;

                String departmentName = (String) nurseJson.get("departmentName");


                LocalDateTime createdAt = null;
                String createdAtStr = (String) nurseJson.get("createdAt");
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

                // Create and add the nurse to the list
                Nurse nurse = new Nurse(nurseId, nurseName, nurseSurname, nursePhone,
                        nurseEmail, nurseAddress, nurseAge, gender, nurseSpecialty,
                        departmentId, departmentName, createdAt);
                nurses.add(nurse);
            }

        } catch (ParseException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            throw new IOException("Failed to parse server response", e);
        } catch (Exception e) {
            System.err.println("Error fetching nurses: " + e.getMessage());
            throw new IOException("Failed to fetch nurses", e);
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {

                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return nurses;
    }


    public Nurse getNurseById(Integer nurseId) throws IOException {
        List<Nurse> nurses = getAllNurses();

        for (Nurse nurse : nurses) {
            if (nurse.getNurseId().equals(nurseId)) {
                return nurse;
            }
        }

        return null; // Nurse not found
    }
}