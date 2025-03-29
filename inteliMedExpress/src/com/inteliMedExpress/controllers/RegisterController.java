package com.inteliMedExpress.controllers;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {
    private static final String CLASS_NAME = RegisterController.class.getSimpleName();

    @FXML
    private TextField username_textfield;
    @FXML
    private TextField password_textfield;
    @FXML
    private TextField email_textfield;
    @FXML
    private TextField phone_textfield;
    @FXML
    private TextField address_textfield;
    @FXML
    private TextField age_textfield;
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



    private static final String LOGIN_API_URL = "http://localhost:7777/api/register";


    @Override
    public void initialize(URL url, ResourceBundle rb) {


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

        String username = username_textfield.getText();
        String password = password_textfield.getText();
        String email = email_textfield.getText();
        String phone = phone_textfield.getText();
        String address = address_textfield.getText();
        String age = age_textfield.getText();
        String gender = gender_dropdown.getValue();
        String profession = profession_dropdown.getValue();
        String department = department_specialty.getValue();






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
            showAlert("Navigation Error", "Could not load login form: " + e.getMessage());
            System.err.println("Error navigating to login form: " + e.getMessage());
            e.printStackTrace();
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



}