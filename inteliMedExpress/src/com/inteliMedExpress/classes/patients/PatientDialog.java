package com.inteliMedExpress.classes.patients;

import com.inteliMedExpress.classes.UIHelper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PatientDialog {

    public static Patient showAddPatientDialog(Stage parentStage) {
        return showPatientDialog(parentStage, null, "Add New Patient");
    }

    public static Patient showUpdatePatientDialog(Stage parentStage, Patient patient) {
        return showPatientDialog(parentStage, patient, "Update Patient");
    }

    public static boolean showDeleteConfirmationDialog(Stage parentStage, Patient patient) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(parentStage);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Patient Record");
        alert.setContentText("Are you sure you want to delete patient: " +
                patient.getName() + " " + patient.getSurname() + " (ID: " + patient.getPatientId() + ")?");

        // Apply custom styling to the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                PatientDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        dialogPane.getStyleClass().add("confirmation");

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Apply custom class to the Yes button manually after the dialog is created
        // Wait for the dialog to be shown, then add CSS class to the button
        alert.setOnShown(event -> {
            Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonTypeYes);
            if (yesButton != null) {
                yesButton.getStyleClass().add("yes-button");
            }
        });

        return alert.showAndWait().orElse(buttonTypeNo) == buttonTypeYes;
    }

    private static Patient showPatientDialog(Stage parentStage, Patient existingPatient, String title) {
        // Create the custom dialog
        Dialog<Patient> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle(title);
        dialog.setHeaderText(existingPatient == null ? "Enter Patient Details" : "Update Patient Details");

        // Apply custom styling to the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                PatientDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Add a handler to apply the style class once the dialog is shown
        dialog.setOnShown(event -> {
            Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
            if (saveButton != null) {
                saveButton.getStyleClass().add("default-button");
            }
        });

        // Create the grid for form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.getStyleClass().add("grid-pane");

        // Create text fields for patient information
        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();
        TextField ageField = new TextField();
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female");
        genderComboBox.setPromptText("Select Gender");
        DatePicker birthDatePicker = new DatePicker();
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Active", "Inactive", "Pending");
        statusComboBox.setPromptText("Select Status");

        // Set preferred width for consistent form field sizes
        nameField.setPrefWidth(250);
        surnameField.setPrefWidth(250);
        emailField.setPrefWidth(250);
        phoneField.setPrefWidth(250);
        addressField.setPrefWidth(250);
        ageField.setPrefWidth(250);
        genderComboBox.setPrefWidth(250);
        birthDatePicker.setPrefWidth(250);
        statusComboBox.setPrefWidth(250);

        // Populate fields if updating
        if (existingPatient != null) {
            nameField.setText(existingPatient.getName());
            surnameField.setText(existingPatient.getSurname());
            emailField.setText(existingPatient.getEmail());
            phoneField.setText(existingPatient.getPhone());
            addressField.setText(existingPatient.getAddress());
            ageField.setText(existingPatient.getAge() != null ? existingPatient.getAge().toString() : "");
            genderComboBox.setValue(existingPatient.getGender());
            birthDatePicker.setValue(existingPatient.getBirthDate());
            statusComboBox.setValue(existingPatient.getStatus());
        }

        // Add labels and fields to the grid
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Surname:"), 0, 1);
        grid.add(surnameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);
        grid.add(new Label("Age:"), 0, 5);
        grid.add(ageField, 1, 5);
        grid.add(new Label("Gender:"), 0, 6);
        grid.add(genderComboBox, 1, 6);
        grid.add(new Label("Birth Date:"), 0, 7);
        grid.add(birthDatePicker, 1, 7);
        grid.add(new Label("Status:"), 0, 8);
        grid.add(statusComboBox, 1, 8);

        dialog.getDialogPane().setContent(grid);

        // Add some spacing to improve layout
        dialogPane.setPrefWidth(450);
        dialogPane.setPrefHeight(600);

        // Request focus on the name field by default
        nameField.requestFocus();

        // Convert the result to a Patient object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText().trim();
                    String surname = surnameField.getText().trim();
                    String email = emailField.getText().trim();
                    String phone = phoneField.getText().trim();
                    String address = addressField.getText().trim();
                    String ageText = ageField.getText().trim();
                    String gender = genderComboBox.getValue();
                    LocalDate birthDate = birthDatePicker.getValue();
                    String status = statusComboBox.getValue();

                    // Validate required fields
                    if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                            address.isEmpty() || ageText.isEmpty() || gender == null || birthDate == null || status == null) {
                        UIHelper.showAlert("Validation Error", "All fields are required.");
                        return null;
                    }

                    // Parse age
                    Integer age;
                    try {
                        age = Integer.parseInt(ageText);
                        if (age <= 0 || age > 150) {
                            UIHelper.showAlert("Validation Error", "Age must be between 1 and 150.");
                            return null;
                        }
                    } catch (NumberFormatException e) {
                        UIHelper.showAlert("Validation Error", "Age must be a valid number.");
                        return null;
                    }

                    // Create a new Patient object or update existing
                    Patient patient;
                    if (existingPatient != null) {
                        patient = existingPatient;
                        patient.setName(name);
                        patient.setSurname(surname);
                        patient.setEmail(email);
                        patient.setPhone(phone);
                        patient.setAddress(address);
                        patient.setAge(age);
                        patient.setGender(gender);
                        patient.setBirthDate(birthDate);
                        patient.setStatus(status);
                    } else {
                        patient = new Patient(null, name, surname, email, phone, address, age, gender, birthDate, status);
                    }

                    return patient;
                } catch (Exception e) {
                    UIHelper.showAlert("Input Error", "Please check your inputs: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }
}