package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.utils.AppLogger;
import com.inteliMedExpress.utils.HttpsUtil;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.awt.desktop.AppForegroundListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {
    private static final String CLASS_NAME = RegisterController.class.getSimpleName();

    @FXML
    private TextField username_textfield;
    @FXML
    private PasswordField password_textfield;
    @FXML
    private TextField email_textfield;
    @FXML
    private TextField phone_textfield;
    @FXML
    private TextField address_textfield;
    @FXML
    private TextField age_textfield;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;


    @FXML
    private Hyperlink back_to_login_hype;

    @FXML
    private Button register_button;

    @FXML
    private ChoiceBox<String> gender_dropdown;
    @FXML
    private ChoiceBox<String> profession_dropdown;
    @FXML
    private ChoiceBox<String> department_specialty;



    private static final String REGISTER_API_URL = "http://127.0.0.1:8080/api/auth/register";


    @Override
    public void initialize(URL url, ResourceBundle rb) {

       // HttpsUtil.setupSSL();

        ObservableList<String> genders = FXCollections.observableArrayList("Male", "Female");
        gender_dropdown.setItems(genders);
        gender_dropdown.setValue("Select Gender");


        ObservableList<String> professions = FXCollections.observableArrayList("Doctor", "Nurse");
        profession_dropdown.setItems(professions);
        profession_dropdown.setValue("Select Profession");


        ObservableList<String> departments = FXCollections.observableArrayList("Cardiology", "Pediatrics", "General Medicine", "Microbiology", "Pharmacology", "Radiology");
        department_specialty.setItems(departments);
        department_specialty.setValue("Select Department");

        profession_dropdown.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (newValue != null) {
                    if (newValue.equals("Nurse")) {
                        // Hide department_specialty when "Nurse" is selected
                        department_specialty.setVisible(false);
                    } else {
                        // Show department_specialty for other professions (Doctor)
                        department_specialty.setVisible(true);
                    }
                }
            }
        });

    }


    public void register(ActionEvent event) throws IOException {
        // I setup all strings because its easier with javafx to get the entries.
        String username = username_textfield.getText();
        String password = password_textfield.getText();
        String email = email_textfield.getText();
        String phone = phone_textfield.getText();
        String address = address_textfield.getText();
        String age = age_textfield.getText();
        String gender = gender_dropdown.getValue();
        String profession = profession_dropdown.getValue();
        String department = department_specialty.getValue();
        String firstname = firstName.getText();
        String lastname = lastName.getText();



        //Conversion to numbers
        int ageNumber = Integer.parseInt(age_textfield.getText());



        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                address.isEmpty() || age.isEmpty() || gender.equals("Select Gender") || profession.equals("Select Profession")) {
            UIHelper.showAlert("Error", "Please fill all the required fields");
            return;
        }

        // Additional validation for Doctors
        if (profession.equals("Doctor") && department.equals("Select Department")) {
            UIHelper.showAlert("Error", "Please select a department/specialty");
            return;
        }

        // Log registration attempt
        AppLogger.info(CLASS_NAME, "Registering user: " + username);


        try {
            boolean registerSuccess = sendRegisterRequest(username, password,firstname, lastname, email, phone, address,
                    ageNumber, gender, profession, department);
            if(registerSuccess) {
                UIHelper.showAlert("Success", "Registration successful.");
                AppLogger.info(CLASS_NAME, username + " successfully registered.");

                // Navigate to login screen
                back_to_login(event);
            } else {
                UIHelper.showAlert("Error", "Registration failed");
            }
        } catch (IOException e) {
            UIHelper.showAlert("Registration Error", e.getMessage());
            System.out.println("Registration Error " + e);
        }
    }







    private boolean sendRegisterRequest(String username, String password, String firstname,String lastName,
                                     String email, String phone, String address,
                                     int ageNumber, String gender, String profession,
                                     String department) throws IOException {

/*
        if (!HttpsUtil.isSSLInitialized()){
            HttpsUtil.setupSSL();
        }
*/

        URL url = new URL(REGISTER_API_URL);

        AppLogger.info(CLASS_NAME, "Sending register request to " + url.toString());
        // Cast to HttpsURLConnection for HTTPS
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Sets request method
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Enables input/output streams
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        // Create JSON payload
        JSONObject registerData = new JSONObject();
        registerData.put("username", username);
        registerData.put("password", password);
        registerData.put("firstname", firstname);
        registerData.put("lastname", lastName);
        registerData.put("email", email);
        registerData.put("phone", phone);
        registerData.put("address", address);
        registerData.put("age", ageNumber);
        registerData.put("gender", gender);
        registerData.put("profession", profession);
        registerData.put("department", department);


        // Convert JSON to string and get bytes
        String jsonInputString = registerData.toString();
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








    public void back_to_login(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/login.fxml"));
            Parent loginRoot = loader.load();


            Scene loginScene = new Scene(loginRoot);


            Stage currentStage = (Stage) back_to_login_hype.getScene().getWindow();


            currentStage.setScene(loginScene);
            currentStage.setTitle("InteliMedExpress - Login");
            currentStage.centerOnScreen();

            System.out.println("Navigation to login form successful");
        } catch (IOException e) {
            UIHelper.showAlert("Navigation Error", "Could not load login form: " + e.getMessage());
            System.err.println("Error navigating to login form: " + e.getMessage());
            e.printStackTrace();
        }

    }






}