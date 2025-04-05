package com.inteliMedExpress.controllers;


import com.inteliMedExpress.utils.AppLogger;
import com.inteliMedExpress.utils.HttpsUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import com.inteliMedExpress.classes.UIHelper;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;


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

    @FXML
    private Hyperlink credits;

    private static final String LOGIN_API_URL = "https://127.0.0.1:8080/api/auth/login";




    public void initialize(){
        AppLogger.initialize();
        AppLogger.info(CLASS_NAME, "LoginController initialized");



        //Setting up the certificate verification
        HttpsUtil.setupSSL();
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

        try{
            boolean loginSuccess = sendLoginRequest(username,password);
                if(loginSuccess){
                    UIHelper.showAlert("Success", "Login successful.");
                    AppLogger.info(CLASS_NAME, username + " successfully logged in.");

                    String doctorName = getDoctorName(username);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/GeneralMedicineDoctor.fxml"));
                    Parent dashboardRoot = loader.load();


                    GeneralMedicineDoctorController controller = loader.getController();
                    controller.setDoctorName(doctorName);

                    // Create new scene
                    Scene dashboardScene = new Scene(dashboardRoot);

                    // Get current stage and set new scene
                    Stage currentStage = (Stage) login_button.getScene().getWindow();
                    currentStage.setScene(dashboardScene);
                    currentStage.setTitle("InteliMedExpress - General Medicine Doctor");
                    currentStage.centerOnScreen();
                }
                else{
                    UIHelper.showAlert("Error", "Login failed");

                }


        } catch (IOException e) {
            UIHelper.showAlert("Login Error", e.getMessage());
            System.out.println("Login Error" + e);
        }


    }

    private String getDoctorName(String username) {

        return username;
    }




    private boolean sendLoginRequest(String username, String password) throws IOException {

        if (!HttpsUtil.isSSLInitialized()){
            HttpsUtil.setupSSL();
        }


        URL url = new URL(LOGIN_API_URL);

        AppLogger.info(CLASS_NAME, "Sending login request to " + url.toString());
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
        return responseCode == HttpURLConnection.HTTP_OK;

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
