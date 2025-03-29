package com.inteliMedExpress.controllers;


import com.inteliMedExpress.utils.AppLogger;
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

    private static final String LOGIN_API_URL = "http://localhost:8080/api/login";




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
                    showAlert("Error", "Login failed");

                }


        } catch (IOException e) {
            showAlert("Login Error", e.getMessage());
            System.out.println("Login Error" + e);
        }


    }

    private String getDoctorName(String username) {
        // This should be implemented to get the actual doctor name from your system
        // For example, from a database query or from the login API response


        return username;
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
        if (title.toLowerCase().contains("error")) {
            alert = new Alert(Alert.AlertType.ERROR);
        } else if (title.toLowerCase().contains("warning")) {
            alert = new Alert(Alert.AlertType.WARNING);
        }

        alert.setTitle(title);
        alert.setHeaderText(title); // Keep the header but style it minimally
        alert.setContentText(message);

        // Get the DialogPane
        DialogPane dialogPane = alert.getDialogPane();

        // Apply minimal styling to the dialog
        dialogPane.setStyle(
                "-fx-background-color: #f0f8ff;" +
                        "-fx-background-radius: 8;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 6, 0, 0, 3);"
        );

        // Style the header - make it more subtle
        Label headerLabel = (Label) dialogPane.lookup(".header-panel .label");
        if (headerLabel != null) {
            headerLabel.setStyle(
                    "-fx-text-fill: #1e67a8;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 16px;"
            );
        }

        // Make header panel more subtle
        Region headerPanel = (Region) dialogPane.lookup(".header-panel");
        if (headerPanel != null) {
            String headerColor = "#f0f8ff"; // Same as background for minimalism

            // Just a slight border at bottom to separate
            headerPanel.setStyle(
                    "-fx-background-color: " + headerColor + ";" +
                            "-fx-border-color: #a9d6e5;" +
                            "-fx-border-width: 0 0 1 0;" + // Only bottom border
                            "-fx-background-radius: 8 8 0 0;"
            );
        }

        // Style the content label
        Label contentLabel = (Label) dialogPane.lookup(".content.label");
        if (contentLabel != null) {
            contentLabel.setStyle(
                    "-fx-text-fill: #2c7bb6;" +
                            "-fx-font-size: 14px;" +
                            "-fx-padding: 10 10 10 10;"
            );
        }

        // Style the buttons
        for (ButtonType buttonType : alert.getDialogPane().getButtonTypes()) {
            Button button = (Button) dialogPane.lookupButton(buttonType);

            // Minimalist button style
            String baseStyle =
                    "-fx-background-color: white;" +
                            "-fx-text-fill: #1e67a8;" +
                            "-fx-border-color: #a9d6e5;" +
                            "-fx-border-width: 1px;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-padding: 6 14 6 14;";

            button.setStyle(baseStyle);

            // Hover effect
            button.setOnMouseEntered(e ->
                    button.setStyle(
                            "-fx-background-color: #f5faff;" +
                                    "-fx-text-fill: #0953a0;" +
                                    "-fx-border-color: #2986cc;" +
                                    "-fx-border-width: 1px;" +
                                    "-fx-border-radius: 4;" +
                                    "-fx-background-radius: 4;" +
                                    "-fx-padding: 6 14 6 14;" +
                                    "-fx-cursor: hand;"
                    )
            );

            // Reset on exit
            button.setOnMouseExited(e -> button.setStyle(baseStyle));
        }

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

        // Load your application's icon - update the path to match your icon location
        Image appIcon = new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/logo.png"));
        stage.getIcons().add(appIcon);



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
