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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {


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
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}