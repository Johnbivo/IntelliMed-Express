package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.Employees.NurseService;
import com.inteliMedExpress.classes.appointments.Appointment;
import com.inteliMedExpress.classes.medicalRecords.MedicalRecord;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.utils.AppLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginController {
    private static final String CLASS_NAME = LoginController.class.getSimpleName();

    // Enum for available departments
    public enum Department {
        CARDIOLOGY("Cardiology"),
        PEDIATRICS("Pediatrics"),
        GENERAL_MEDICINE("General"),
        MICROBIOLOGY("Microbiology"),
        PHARMACOLOGY("Pharmacy"),  // Changed to match server's expected value
        RADIOLOGY("Radiology");

        private final String displayName;

        Department(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        // Convert from string representation to enum
        public static Department fromString(String department) {
            if (department == null) return GENERAL_MEDICINE;

            // Handle Pharmacy/Pharmacology special case
            if (department.equalsIgnoreCase("Pharmacy") ||
                    department.equalsIgnoreCase("Pharmacology")) {
                return PHARMACOLOGY;
            }

            // Remove spaces and convert to uppercase for comparison
            String normalizedDept = department.replaceAll("\\s", "_").toUpperCase();

            try {
                return valueOf(normalizedDept);
            } catch (IllegalArgumentException e) {
                // Default to General Medicine if not found
                return GENERAL_MEDICINE;
            }
        }
    }

    @FXML
    private TextField username_textfield;

    @FXML
    private TextField password_textfield;

    @FXML
    private Button login_button;

    @FXML
    private Hyperlink forgot_password_hyper;

    @FXML
    private Hyperlink register_hyper;

    @FXML
    private Hyperlink credits;

    private static final String LOGIN_API_URL = "https://springserver-kl8q.onrender.com/api/auth/login";

    public void initialize() {
        AppLogger.initialize();
        AppLogger.info(CLASS_NAME, "LoginController initialized");
    }

    public void login(ActionEvent event) {
        String username = username_textfield.getText();
        String password = password_textfield.getText();

        if (username.isEmpty() || password.isEmpty()) {
            UIHelper.showAlert("Error", "Please enter both the username and password fields.");
            return;
        }
        AppLogger.info(CLASS_NAME, "Login attempt for user: " + username);

        try {
            JSONObject response = sendLoginRequest(username, password);
            if (response != null) {
                UIHelper.showAlert("Success", "Login successful.");
                AppLogger.info(CLASS_NAME, username + " successfully logged in.");

                // Extract data from response
                String name = (String) response.get("name");
                String surname = (String) response.get("surname");
                String department = (String) response.get("department");
                String profession = (String) response.get("profession");

                // Log the received department for debugging
                AppLogger.info(CLASS_NAME, "Department received from server: " + department);

                // Set the department for all API calls
                Department dept = Department.fromString(department);

                // Update department settings in all relevant classes
                Appointment.setDepartment(dept.getDisplayName());

                // Only set these if you have implemented these methods
                try {
                    MedicalRecord.setDepartment(dept.getDisplayName());
                    Patient.setDepartment(dept.getDisplayName());
                    NurseService.setDepartment(dept.getDisplayName());
                } catch (Exception e) {
                    AppLogger.warning(CLASS_NAME, "Could not set department for all classes: " + e.getMessage());
                }

                AppLogger.info(CLASS_NAME, "Department set to: " + dept.getDisplayName());

                // Navigate to appropriate page based on department and profession
                navigateBasedOnDepartment(name, surname, department, profession);
            } else {
                UIHelper.showAlert("Error", "Login failed");
            }
        } catch (IOException e) {
            UIHelper.showAlert("Login Error", e.getMessage());
            System.out.println("Login Error" + e);
        }
    }

    private void navigateBasedOnDepartment(String name, String surname, String department, String profession) throws IOException {
        // Determine which FXML to load based on department
        String fxmlPath;
        String title = "InteliMedExpress";
        boolean isDoctorRole = "DOCTOR".equalsIgnoreCase(profession);
        String roleText = isDoctorRole ? "Doctor" : "Nurse";

        // Log which department we're processing
        AppLogger.info(CLASS_NAME, "Processing navigation for department: " + department);

        // Check department and load appropriate view
        if ("Microbiology".equalsIgnoreCase(department)) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/microbiology.fxml";
            title += " - Microbiology " + roleText;
            AppLogger.info(CLASS_NAME, "Directing to Microbiology interface");
        }
        else if ("Radiology".equalsIgnoreCase(department)) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/radiology.fxml";
            title += " - Radiology " + roleText;
            AppLogger.info(CLASS_NAME, "Directing to Radiology interface");
        }
        else if ("Pharmacy".equalsIgnoreCase(department) || "Pharmacology".equalsIgnoreCase(department)) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/pharmacology.fxml";
            title += " - Pharmacology " + roleText;
            AppLogger.info(CLASS_NAME, "Directing to Pharmacology interface");
        }
        else if ("Cardiology".equalsIgnoreCase(department)) {
            fxmlPath = "/resources/com/inteliMedExpress/fxml/GeneralMedicineDoctor.fxml";
            title += " - Cardiology " + roleText;
            AppLogger.info(CLASS_NAME, "Directing to Cardiology interface");
        }
        else if ("Pediatrics".equalsIgnoreCase(department)) {
            fxmlPath = "/resources/com/inteliMedExpress/fxml/GeneralMedicineDoctor.fxml";
            title += " - Pediatrics " + roleText;
            AppLogger.info(CLASS_NAME, "Directing to Pediatrics interface");
        }
        else if ("General".equalsIgnoreCase(department) || "General Medicine".equalsIgnoreCase(department)) {
            fxmlPath = "/resources/com/inteliMedExpress/fxml/GeneralMedicineDoctor.fxml";
            title += " - General Medicine " + roleText;
            AppLogger.info(CLASS_NAME, "Directing to General Medicine interface");
        }
        else {
            // Default case
            AppLogger.warning(CLASS_NAME, "Unknown department '" + department + "', defaulting to General Medicine");
            fxmlPath = "/resources/com/inteliMedExpress/fxml/GeneralMedicineDoctor.fxml";
            title += " - " + department + " " + roleText;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent dashboardRoot = loader.load();

            // Handle controller setup
            if ("Microbiology".equalsIgnoreCase(department)) {
                MicrobiologyController controller = loader.getController();
                controller.setDoctorName(name + " " + surname);
                AppLogger.info(CLASS_NAME, "Loaded Microbiology controller for " + name + " " + surname);
            }
            else if ("Radiology".equalsIgnoreCase(department)) {
                RadiologyController controller = loader.getController();
                controller.setDoctorName(name + " " + surname);
                AppLogger.info(CLASS_NAME, "Loaded Radiology controller for " + name + " " + surname);
            }
            else if ("Pharmacy".equalsIgnoreCase(department) || "Pharmacology".equalsIgnoreCase(department)) {
                PharmacologyController controller = loader.getController();
                controller.setDoctorName(name + " " + surname);
                AppLogger.info(CLASS_NAME, "Loaded Pharmacology controller for " + name + " " + surname);
            }
            else {
                // General controller for Cardiology, Pediatrics, General Medicine, and default
                GeneralMedicineDoctorController controller = loader.getController();
                controller.setDoctorName(name + " " + surname);
                controller.setDepartment(department);
                AppLogger.info(CLASS_NAME, "Loaded General controller with department " + department);
            }

            // Create and set the scene
            Scene dashboardScene = new Scene(dashboardRoot);
            Stage currentStage = (Stage) login_button.getScene().getWindow();
            currentStage.setScene(dashboardScene);
            currentStage.setTitle(title);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            AppLogger.error(CLASS_NAME, "Error loading FXML: " + e.getMessage());
            UIHelper.showAlert("Navigation Error", "Could not load department view: " + e.getMessage());
            throw e; // Rethrow to handle in calling method
        }
    }

    private JSONObject sendLoginRequest(String username, String password) throws IOException {
        URL url = new URL(LOGIN_API_URL);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        JSONObject loginData = new JSONObject();
        loginData.put("username", username);
        loginData.put("password", password);

        String jsonInputString = loginData.toString();
        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);

        connection.setRequestProperty("Content-Length", String.valueOf(input.length));

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Log the raw response for debugging
                AppLogger.info(CLASS_NAME, "Server response: " + response.toString());

                // Parse the JSON response
                JSONParser parser = new JSONParser();
                try {
                    return (JSONObject) parser.parse(response.toString());
                } catch (org.json.simple.parser.ParseException e) {
                    throw new IOException("Error parsing response: " + e.getMessage());
                }
            }
        } else {
            // Log error response
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream() != null ?
                            connection.getErrorStream() :
                            connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                AppLogger.error(CLASS_NAME, "Login failed with code " + responseCode +
                        " and response: " + response.toString());
            } catch (Exception e) {
                AppLogger.error(CLASS_NAME, "Failed to read error response: " + e.getMessage());
            }
            return null;
        }
    }

    public void register(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/com/inteliMedExpress/fxml/register.fxml"));
            Parent registerRoot = loader.load();
            Scene registerScene = new Scene(registerRoot);
            Stage currentStage = (Stage) register_hyper.getScene().getWindow();

            currentStage.setScene(registerScene);
            currentStage.setTitle("InteliMedExpress - Registration");
            currentStage.centerOnScreen();

            System.out.println("Navigation to registration form successful");
        } catch (IOException e) {
            UIHelper.showAlert("Navigation Error", "Could not load registration form: " + e.getMessage());
            System.err.println("Error navigating to registration form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showCredits(ActionEvent event) {
        UIHelper.showAlert("Credits", "Icons by icons8.com - https://icons8.com");
    }
}