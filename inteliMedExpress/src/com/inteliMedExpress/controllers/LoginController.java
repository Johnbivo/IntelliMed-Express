package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.Employees.NurseService;
import com.inteliMedExpress.classes.appointments.Appointment;
import com.inteliMedExpress.classes.medicalRecords.MedicalRecord;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.utils.AppLogger;
import com.inteliMedExpress.utils.HttpsUtil;
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
        PHARMACOLOGY("Pharmacology"),
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

        //Setting up the certificate verification
        //HttpsUtil.setupSSL();
    }

    // function that gets triggered by the login button
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
                navigateBasedOnDepartment(name, surname, dept.getDisplayName(), profession);
            } else {
                UIHelper.showAlert("Error", "Login failed");
            }
        } catch (IOException e) {
            UIHelper.showAlert("Login Error", e.getMessage());
            System.out.println("Login Error" + e);
        }
    }

    private void navigateBasedOnDepartment(String name, String surname, String department, String profession) throws IOException {
        // FXML path - set default to empty
        String fxmlPath = "";

        // Determine which controller class to use based on department
        Class<?> controllerClass = null;

        if (department == null) {
            return;
        }

        // Set the appropriate FXML path and controller class based on department
        if (department.equals("General")) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/GeneralMedicineDoctor.fxml";
            controllerClass = GeneralMedicineDoctorController.class;
        } else if (department.equals("Microbiology")) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/MicrobiologyDoctor.fxml";
            controllerClass = MicrobiologyDoctorController.class;
        } else if (department.equals("Pharmacology")) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/PharmacologyDoctor.fxml";
            controllerClass = PharmacologyDoctorController.class; // You need to create this controller class
        } else if (department.equals("Pediatrics")) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/PediatricsDoctor.fxml";
            controllerClass = PediatricsDoctorController.class;
        } else if (department.equals("Radiology")) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/RadiologyDoctor.fxml";
            controllerClass = RadiologyDoctorController.class; // You need to create this controller class
        } else if (department.equals("Cardiology")) {
            fxmlPath = "/com/inteliMedExpress/resources/fxml/CardiologyDoctor.fxml";
            controllerClass = CardiologyDoctorController.class; // You need to create this controller class
        }

        // Set the window title based on department and role
        String title = "InteliMedExpress";
        boolean isDoctorRole = "DOCTOR".equalsIgnoreCase(profession);

        // Format department for display (replace underscores with spaces)
        String displayDepartment = department.replace("_", " ");
        title += " - " + displayDepartment + " " + (isDoctorRole ? "Doctor" : "Nurse");

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent dashboardRoot = loader.load();

            // Get the controller and set the user name
            Object controller = loader.getController();

            // Check controller type and call setDoctorName if it's a subclass of GeneralMedicineDoctorController
            // or has a setDoctorName method
            if (controller instanceof GeneralMedicineDoctorController) {
                ((GeneralMedicineDoctorController) controller).setDoctorName(name + " " + surname);
            } else {
                // For other controller types, try to call setDoctorName using reflection
                try {
                    java.lang.reflect.Method setDoctorNameMethod = controller.getClass().getMethod("setDoctorName", String.class);
                    setDoctorNameMethod.invoke(controller, name + " " + surname);
                } catch (Exception e) {
                    AppLogger.warning(CLASS_NAME, "Controller doesn't have setDoctorName method: " + e.getMessage());
                }
            }

            // Log which department was loaded
            AppLogger.info(CLASS_NAME, "Loaded view for " + displayDepartment + " with role " + profession);

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

        // Cast to HttpsURLConnection for HTTPS
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        // Sets request method
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Enables input/output streams
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Create JSON payload
        JSONObject loginData = new JSONObject();
        loginData.put("username", username);
        loginData.put("password", password);

        // Convert JSON to string and get bytes
        String jsonInputString = loginData.toString();
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
            // Read the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Parse the JSON response
            JSONParser parser = new JSONParser();
            try {
                return (JSONObject) parser.parse(response.toString());
            } catch (org.json.simple.parser.ParseException e) {
                throw new IOException("Error parsing response: " + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public void register(ActionEvent event) {
        try {
            // Load the register.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/register.fxml"));
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
        UIHelper.showAlert("Credits", "Icons by icons8.com - https:/icon8.com");
    }
}