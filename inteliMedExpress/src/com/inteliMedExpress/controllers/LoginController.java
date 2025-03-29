package com.inteliMedExpress.controllers;


import com.inteliMedExpress.utils.AppLogger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONObject;


import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class LoginController {
    private static final String CLASS_NAME = LoginController.class.getSimpleName();
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

    private static final String LOGIN_API_URL = "http://localhost:5000/api/login";




    public void initialize(){
        AppLogger.initialize();
        AppLogger.info(CLASS_NAME, "LoginController initialized");
    }


    // function that gets triggered by the login button
    public void login(ActionEvent event) {
        String username = username_textfield.getText();
        String password = password_textfield.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both the username and password fields.");
            return;
        }
        AppLogger.info(CLASS_NAME, "Login attempt for user: " + username);

        try{
            boolean loginSuccess = sendLoginRequest(username,password);
                if(loginSuccess){
                    showAlert("Success", "Login successful.");
                    AppLogger.info(CLASS_NAME, username + " successfully logged in.");
                }
                else{
                    showAlert("Error", "Login failed");

                }


        } catch (IOException e) {
            showAlert("Login Error", e.getMessage());
            System.out.println("Login Error" + e);
        }


    }




    private boolean sendLoginRequest(String username, String password) throws IOException {
        URL url = new URL(LOGIN_API_URL);

        AppLogger.info(CLASS_NAME, "Sending login request to " + url.toString());
        //Opens the connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //Sets request method
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);

        //Enables input/output streams
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
        try(OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(input,0,input.length);

        }
        // Get response code
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return true;
        }else {
            return false;
        }



    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public void register(ActionEvent event) {

        try {
            // Load the register.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/register.fxml"));
            Parent registerRoot = loader.load();

            // Create a new scene with the register form
            Scene registerScene = new Scene(registerRoot);

            // Get the current stage from the event source
            Stage currentStage = (Stage) register_hyper.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(registerScene);
            currentStage.setTitle("InteliMedExpress - Registration");
            currentStage.centerOnScreen();

            System.out.println("Navigation to registration form successful");
        } catch (IOException e) {
            showAlert("Navigation Error", "Could not load registration form: " + e.getMessage());
            System.err.println("Error navigating to registration form: " + e.getMessage());
            e.printStackTrace();
        }

    }






}
