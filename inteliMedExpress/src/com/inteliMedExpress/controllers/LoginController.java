package com.inteliMedExpress.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import org.json.simple.JSONObject;

import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class LoginController {

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

    private static final String LOGIN_API_URL = "http://localhost:7777/api/login";



    public void login(ActionEvent event) {
        String username = username_textfield.getText();
        String password = password_textfield.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both the username and password fields.");
            return;
        }

        try{
            boolean loginSuccess = sendLoginRequest(username,password);
                if(loginSuccess){
                    showAlert("Success", "Login successful.");
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



}
