package com.inteliMedExpress.classes.medicalRecords;

import com.inteliMedExpress.classes.UIHelper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MedicalRecordsDialog {

    public static MedicalRecord showAddMedicalRecordDialog(Stage parentStage) {
        return showMedicalRecordDialog(parentStage, null, "Add New Medical Record");
    }

    public static MedicalRecord showUpdateMedicalRecordDialog(Stage parentStage, MedicalRecord medicalRecord) {
        return showMedicalRecordDialog(parentStage, medicalRecord, "Update Medical Record");
    }

    public static boolean showDeleteConfirmationDialog(Stage parentStage, MedicalRecord medicalRecord) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(parentStage);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Medical Record");
        alert.setContentText("Are you sure you want to delete medical record for: " +
                medicalRecord.getPatientName() + " " + medicalRecord.getPatientSurname() +
                " (Record ID: " + medicalRecord.getRecordId() + ")?");

        // Apply custom styling to the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                MedicalRecordsDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
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

    private static MedicalRecord showMedicalRecordDialog(Stage parentStage, MedicalRecord existingRecord, String title) {
        // Create the custom dialog
        Dialog<MedicalRecord> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle(title);
        dialog.setHeaderText(existingRecord == null ? "Enter Medical Record Details" : "Update Medical Record Details");

        // Apply custom styling to the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                MedicalRecordsDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());

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

        // Create text fields for medical record information
        TextField patientNameField = new TextField();
        TextField patientSurnameField = new TextField();
        TextField doctorSurnameField = new TextField();
        TextArea diagnosisArea = new TextArea();
        diagnosisArea.setPrefRowCount(3);
        TextArea treatmentArea = new TextArea();
        treatmentArea.setPrefRowCount(3);
        TextArea prescriptionArea = new TextArea();
        prescriptionArea.setPrefRowCount(3);
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Active", "Completed", "Pending", "Canceled");
        statusComboBox.setPromptText("Select Status");
        DatePicker recordDatePicker = new DatePicker();
        recordDatePicker.setValue(LocalDate.now()); // Default to today

        // Set preferred width for consistent form field sizes
        patientNameField.setPrefWidth(250);
        patientSurnameField.setPrefWidth(250);
        doctorSurnameField.setPrefWidth(250);
        diagnosisArea.setPrefWidth(250);
        treatmentArea.setPrefWidth(250);
        prescriptionArea.setPrefWidth(250);
        statusComboBox.setPrefWidth(250);
        recordDatePicker.setPrefWidth(250);

        // Populate fields if updating
        if (existingRecord != null) {
            patientNameField.setText(existingRecord.getPatientName());
            patientSurnameField.setText(existingRecord.getPatientSurname());
            doctorSurnameField.setText(existingRecord.getDoctorSurname());
            diagnosisArea.setText(existingRecord.getDiagnosis());
            treatmentArea.setText(existingRecord.getTreatment());
            prescriptionArea.setText(existingRecord.getPrescription());
            statusComboBox.setValue(existingRecord.getStatus());
            recordDatePicker.setValue(existingRecord.getRecordDate());
        }

        // Add labels and fields to the grid
        grid.add(new Label("Patient Name:"), 0, 0);
        grid.add(patientNameField, 1, 0);
        grid.add(new Label("Patient Surname:"), 0, 1);
        grid.add(patientSurnameField, 1, 1);
        grid.add(new Label("Doctor Surname:"), 0, 2);
        grid.add(doctorSurnameField, 1, 2);
        grid.add(new Label("Diagnosis:"), 0, 3);
        grid.add(diagnosisArea, 1, 3);
        grid.add(new Label("Treatment:"), 0, 4);
        grid.add(treatmentArea, 1, 4);
        grid.add(new Label("Prescription:"), 0, 5);
        grid.add(prescriptionArea, 1, 5);
        grid.add(new Label("Status:"), 0, 6);
        grid.add(statusComboBox, 1, 6);
        grid.add(new Label("Record Date:"), 0, 7);
        grid.add(recordDatePicker, 1, 7);

        dialog.getDialogPane().setContent(grid);

        // Add some spacing to improve layout
        dialogPane.setPrefWidth(500);
        dialogPane.setPrefHeight(650);

        // Request focus on the patient name field by default
        patientNameField.requestFocus();

        // Convert the result to a MedicalRecord object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String patientName = patientNameField.getText().trim();
                    String patientSurname = patientSurnameField.getText().trim();
                    String doctorSurname = doctorSurnameField.getText().trim();
                    String diagnosis = diagnosisArea.getText().trim();
                    String treatment = treatmentArea.getText().trim();
                    String prescription = prescriptionArea.getText().trim();
                    String status = statusComboBox.getValue();
                    LocalDate recordDate = recordDatePicker.getValue();

                    // Validate required fields
                    if (patientName.isEmpty() || patientSurname.isEmpty() || doctorSurname.isEmpty() ||
                            diagnosis.isEmpty() || status == null || recordDate == null) {
                        UIHelper.showAlert("Validation Error",
                                "Patient name, patient surname, doctor surname, diagnosis, status, and record date are required.");
                        return null;
                    }

                    // Create a new MedicalRecord object or update existing
                    MedicalRecord medicalRecord;
                    if (existingRecord != null) {
                        medicalRecord = existingRecord;
                        medicalRecord.setPatientName(patientName);
                        medicalRecord.setPatientSurname(patientSurname);
                        medicalRecord.setDoctorSurname(doctorSurname);
                        medicalRecord.setDiagnosis(diagnosis);
                        medicalRecord.setTreatment(treatment);
                        medicalRecord.setPrescription(prescription);
                        medicalRecord.setStatus(status);
                        medicalRecord.setRecordDate(recordDate);
                    } else {
                        medicalRecord = new MedicalRecord(null, patientName, patientSurname,
                                doctorSurname, diagnosis, treatment, prescription, status, recordDate);
                    }

                    return medicalRecord;
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