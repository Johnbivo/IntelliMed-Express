package com.inteliMedExpress.classes.Beds;

import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.patients.PatientService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BedDialog {

    public static Bed showAssignPatientDialog(Stage parentStage, Bed bed) {
        // Create the custom dialog
        Dialog<Bed> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle("Assign Patient to Bed");
        dialog.setHeaderText("Assign Patient to Bed #" + bed.getBedId() + " (Room " + bed.getRoomNumber() + ")");

        // Apply custom styling to the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                BedDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());

        // Set the button types
        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        // Create the grid for form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.getStyleClass().add("grid-pane");

        // Create a combobox for patient selection
        ComboBox<String> patientComboBox = new ComboBox<>();
        patientComboBox.setPrefWidth(250);
        patientComboBox.setPromptText("Select Patient");

        // Initialize a list for patients outside the try-catch so it's accessible to the result converter
        final List<Patient> patientsList = new ArrayList<>();

        // Load patients
        PatientService patientService = new PatientService();
        try {
            List<Patient> patients = patientService.getAllPatients();
            // Store the patients in our accessible list
            patientsList.addAll(patients);

            // Add patients to the combo box
            for (Patient patient : patients) {
                patientComboBox.getItems().add(patient.getPatientId() + ": " +
                        patient.getName() + " " +
                        patient.getSurname());
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Could not load patients: " + e.getMessage());
        }

        // Add components to the grid
        grid.add(new Label("Patient:"), 0, 0);
        grid.add(patientComboBox, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the patient combobox
        patientComboBox.requestFocus();

        // Convert the result when the assign button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                try {
                    String selectedPatient = patientComboBox.getValue();

                    if (selectedPatient == null || selectedPatient.isEmpty()) {
                        UIHelper.showAlert("Error", "Please select a patient.");
                        return null;
                    }

                    // Extract patient ID from selection (format: "ID: Name Surname")
                    int patientId = Integer.parseInt(selectedPatient.split(":")[0].trim());

                    // Find the patient in our list
                    Patient patient = patientsList.stream()
                            .filter(p -> p.getPatientId() == patientId)
                            .findFirst()
                            .orElse(null);

                    if (patient != null) {
                        // Update bed with patient information
                        bed.setPatientId(patient.getPatientId());
                        bed.setPatientName(patient.getName());
                        bed.setPatientSurname(patient.getSurname());
                        bed.setStatus("Occupied");
                        return bed;
                    } else {
                        UIHelper.showAlert("Error", "Selected patient not found.");
                        return null;
                    }
                } catch (Exception e) {
                    UIHelper.showAlert("Error", "Error assigning patient: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    public static boolean showDischargeConfirmationDialog(Stage parentStage, Bed bed) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(parentStage);
        alert.setTitle("Confirm Discharge");
        alert.setHeaderText("Discharge Patient from Bed");
        alert.setContentText("Are you sure you want to discharge patient: " +
                bed.getPatientName() + " " + bed.getPatientSurname() + " from Bed #" + bed.getBedId() + "?");

        // Apply custom styling to the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                BedDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        dialogPane.getStyleClass().add("confirmation");

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Apply custom class to the Yes button manually after the dialog is created
        alert.setOnShown(event -> {
            Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonTypeYes);
            if (yesButton != null) {
                yesButton.getStyleClass().add("yes-button");
            }
        });

        return alert.showAndWait().orElse(buttonTypeNo) == buttonTypeYes;
    }
}