package com.inteliMedExpress.classes.medicalRecords;

import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.patients.PatientService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

        // Create dropdown for patients
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

        // Create other form fields
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

        // Date picker for record date
        DatePicker recordDatePicker = new DatePicker();

        // Time picker components (hours and minutes)
        ComboBox<String> hourPicker = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            hourPicker.getItems().add(String.format("%02d", i));
        }

        ComboBox<String> minutePicker = new ComboBox<>();
        for (int i = 0; i < 60; i += 15) { // 15-minute intervals
            minutePicker.getItems().add(String.format("%02d", i));
        }

        // Set preferred width for consistent form field sizes
        patientComboBox.setPrefWidth(250);
        doctorSurnameField.setPrefWidth(250);
        diagnosisArea.setPrefWidth(250);
        treatmentArea.setPrefWidth(250);
        prescriptionArea.setPrefWidth(250);
        statusComboBox.setPrefWidth(250);
        recordDatePicker.setPrefWidth(250);
        hourPicker.setPrefWidth(80);
        minutePicker.setPrefWidth(80);

        // Populate fields if updating
        if (existingRecord != null) {
            // For patient selection, find the matching patient in the dropdown
            if (existingRecord.getPatientName() != null && existingRecord.getPatientSurname() != null) {
                for (Patient patient : patientsList) {
                    if (patient.getName().equals(existingRecord.getPatientName()) &&
                            patient.getSurname().equals(existingRecord.getPatientSurname())) {
                        patientComboBox.setValue(patient.getPatientId() + ": " +
                                patient.getName() + " " +
                                patient.getSurname());
                        break;
                    }
                }
            }

            doctorSurnameField.setText(existingRecord.getDoctorSurname());
            diagnosisArea.setText(existingRecord.getDiagnosis());
            treatmentArea.setText(existingRecord.getTreatment());
            prescriptionArea.setText(existingRecord.getPrescription());
            statusComboBox.setValue(existingRecord.getRecordStatus());

            // Handle the date and time
            if (existingRecord.getRecordDate() != null) {
                recordDatePicker.setValue(existingRecord.getRecordDate().toLocalDate());
                LocalTime time = existingRecord.getRecordDate().toLocalTime();
                hourPicker.setValue(String.format("%02d", time.getHour()));
                minutePicker.setValue(String.format("%02d", (time.getMinute() / 15) * 15)); // Round to nearest 15 min
            } else {
                recordDatePicker.setValue(LocalDate.now());
                hourPicker.setValue("09"); // 9 AM default
                minutePicker.setValue("00"); // 0 minutes default
            }
        } else {
            // Default values for new record
            recordDatePicker.setValue(LocalDate.now());
            hourPicker.setValue("09"); // 9 AM default
            minutePicker.setValue("00"); // 0 minutes default
        }

        // Create time picker layout
        HBox timeBox = new HBox(10);
        Label timeSeparator = new Label(":");
        timeSeparator.getStyleClass().add("time-separator-label");
        timeBox.getChildren().addAll(hourPicker, timeSeparator, minutePicker);

        // Add labels and fields to the grid
        grid.add(new Label("Patient:"), 0, 0);
        grid.add(patientComboBox, 1, 0);
        grid.add(new Label("Doctor Surname:"), 0, 1);
        grid.add(doctorSurnameField, 1, 1);
        grid.add(new Label("Diagnosis:"), 0, 2);
        grid.add(diagnosisArea, 1, 2);
        grid.add(new Label("Treatment:"), 0, 3);
        grid.add(treatmentArea, 1, 3);
        grid.add(new Label("Prescription:"), 0, 4);
        grid.add(prescriptionArea, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusComboBox, 1, 5);
        grid.add(new Label("Record Date:"), 0, 6);
        grid.add(recordDatePicker, 1, 6);
        grid.add(new Label("Record Time:"), 0, 7);
        grid.add(timeBox, 1, 7);

        dialog.getDialogPane().setContent(grid);

        // Add some spacing to improve layout
        dialogPane.setPrefWidth(450);
        dialogPane.setPrefHeight(650); // Increased height to accommodate the time picker

        // Request focus on the patient selector by default
        patientComboBox.requestFocus();

        // Convert the result to a MedicalRecord object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String selectedPatient = patientComboBox.getValue();
                    if (selectedPatient == null || selectedPatient.isEmpty()) {
                        UIHelper.showAlert("Validation Error", "Please select a patient.");
                        return null;
                    }

                    // Extract patient ID from selection (format: "ID: Name Surname")
                    int patientId = Integer.parseInt(selectedPatient.split(":")[0].trim());

                    // Find the patient in our list
                    Patient patient = patientsList.stream()
                            .filter(p -> p.getPatientId() == patientId)
                            .findFirst()
                            .orElse(null);

                    if (patient == null) {
                        UIHelper.showAlert("Error", "Selected patient not found.");
                        return null;
                    }

                    String patientName = patient.getName();
                    String patientSurname = patient.getSurname();
                    String doctorSurname = doctorSurnameField.getText().trim();
                    String diagnosis = diagnosisArea.getText().trim();
                    String treatment = treatmentArea.getText().trim();
                    String prescription = prescriptionArea.getText().trim();
                    String recordStatus = statusComboBox.getValue();
                    LocalDate recordDate = recordDatePicker.getValue();

                    // Get time values
                    int hour = Integer.parseInt(hourPicker.getValue());
                    int minute = Integer.parseInt(minutePicker.getValue());

                    // Combine date and time
                    LocalDateTime recordDateTime = LocalDateTime.of(recordDate, LocalTime.of(hour, minute));

                    // Validate required fields
                    if (doctorSurname.isEmpty() || diagnosis.isEmpty() ||
                            recordStatus == null || recordDate == null) {
                        UIHelper.showAlert("Validation Error",
                                "Doctor surname, diagnosis, status, and record date are required.");
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
                        medicalRecord.setRecordStatus(recordStatus);
                        medicalRecord.setRecordDate(recordDateTime);
                    } else {
                        medicalRecord = new MedicalRecord(null, patientName, patientSurname,
                                doctorSurname, diagnosis, treatment, prescription, recordStatus, recordDateTime);
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