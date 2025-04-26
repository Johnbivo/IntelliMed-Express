package com.inteliMedExpress.classes.Prescriptions;

import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.classes.Medications.Medication;
import com.inteliMedExpress.classes.Medications.MedicationService;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.patients.PatientService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDialog {

    public static Prescription showAddPrescriptionDialog(Stage parentStage) {
        return showPrescriptionDialog(parentStage, null, "Add New Prescription");
    }

    public static Prescription showUpdatePrescriptionDialog(Stage parentStage, Prescription prescription) {
        return showPrescriptionDialog(parentStage, prescription, "Update Prescription");
    }

    public static boolean showDeleteConfirmationDialog(Stage parentStage, Prescription prescription) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(parentStage);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Prescription Record");

        String formattedDateTime = prescription.getDatePrescribed() != null ?
                prescription.getDatePrescribed().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
                "unknown date";

        alert.setContentText("Are you sure you want to delete prescription for: " +
                prescription.getPatientFirstName() + " " + prescription.getPatientLastName() +
                " on " + formattedDateTime + " (ID: " + prescription.getPrescriptionId() + ")?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                PrescriptionDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        dialogPane.getStyleClass().add("confirmation");

        ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        alert.setOnShown(event -> {
            Button yesButton = (Button) alert.getDialogPane().lookupButton(buttonTypeYes);
            if (yesButton != null) {
                yesButton.getStyleClass().add("yes-button");
            }
        });

        return alert.showAndWait().orElse(buttonTypeNo) == buttonTypeYes;
    }

    private static Prescription showPrescriptionDialog(Stage parentStage, Prescription existingPrescription, String title) {

        Dialog<Prescription> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle(title);
        dialog.setHeaderText(existingPrescription == null ? "Enter Prescription Details" : "Update Prescription Details");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                PrescriptionDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());


        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setOnShown(event -> {
            Button saveButton = (Button) dialogPane.lookupButton(saveButtonType);
            if (saveButton != null) {
                saveButton.getStyleClass().add("default-button");
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.getStyleClass().add("grid-pane");

        ComboBox<String> patientComboBox = new ComboBox<>();
        patientComboBox.setPrefWidth(250);
        patientComboBox.setPromptText("Select Patient");


        final List<Patient> patientsList = new ArrayList<>();
        final List<Medication> medicationsList = new ArrayList<>();


        PatientService patientService = new PatientService();
        try {
            List<Patient> patients = patientService.getAllPatients();
            patientsList.addAll(patients);

            for (Patient patient : patients) {
                patientComboBox.getItems().add(patient.getPatientId() + ": " +
                        patient.getName() + " " +
                        patient.getSurname());
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Could not load patients: " + e.getMessage());
        }
        ComboBox<String> medicationComboBox = new ComboBox<>();
        medicationComboBox.setPrefWidth(250);
        medicationComboBox.setPromptText("Select Medication");

        MedicationService medicationService = new MedicationService();
        try {
            List<Medication> medications = medicationService.getAllMedications();
            medicationsList.addAll(medications);

            for (Medication medication : medications) {
                medicationComboBox.getItems().add(medication.getMedicationId() + ": " +
                        medication.getMedicationName());
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Could not load medications: " + e.getMessage());
        }

        TextField dosageField = new TextField();
        TextField durationField = new TextField();

        TextArea descriptionArea = new TextArea();
        descriptionArea.setPrefRowCount(3);

        patientComboBox.setPrefWidth(250);
        medicationComboBox.setPrefWidth(250);
        dosageField.setPrefWidth(250);
        durationField.setPrefWidth(250);
        descriptionArea.setPrefWidth(250);

        if (existingPrescription != null) {

            if (existingPrescription.getPatientFirstName() != null && existingPrescription.getPatientLastName() != null) {
                String patientToSelect = existingPrescription.getPatientId() + ": " +
                        existingPrescription.getPatientFirstName() + " " +
                        existingPrescription.getPatientLastName();
                patientComboBox.setValue(patientToSelect);
            }

            if (existingPrescription.getMedicationName() != null) {
                String medicationToSelect = existingPrescription.getMedicationId() + ": " +
                        existingPrescription.getMedicationName();
                medicationComboBox.setValue(medicationToSelect);
            }

            dosageField.setText(existingPrescription.getDosage());
            durationField.setText(existingPrescription.getDuration());
            descriptionArea.setText(existingPrescription.getMedicationDescription());
        }
        int rowIndex = 0;

        grid.add(new Label("Patient:"), 0, rowIndex);
        grid.add(patientComboBox, 1, rowIndex++);

        grid.add(new Label("Medication:"), 0, rowIndex);
        grid.add(medicationComboBox, 1, rowIndex++);

        grid.add(new Label("Dosage:"), 0, rowIndex);
        grid.add(dosageField, 1, rowIndex++);

        grid.add(new Label("Duration:"), 0, rowIndex);
        grid.add(durationField, 1, rowIndex++);

        grid.add(new Label("Medication Notes:"), 0, rowIndex);
        grid.add(descriptionArea, 1, rowIndex++);

        dialog.getDialogPane().setContent(grid);
        dialogPane.setPrefWidth(500);
        dialogPane.setPrefHeight(500);

        patientComboBox.requestFocus();

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String selectedPatient = patientComboBox.getValue();
                    if (selectedPatient == null || selectedPatient.isEmpty()) {
                        UIHelper.showAlert("Validation Error", "Please select a patient.");
                        return null;
                    }

                    String selectedMedication = medicationComboBox.getValue();
                    if (selectedMedication == null || selectedMedication.isEmpty()) {
                        UIHelper.showAlert("Validation Error", "Please select a medication.");
                        return null;
                    }

                    int patientId = Integer.parseInt(selectedPatient.split(":")[0].trim());

                    Patient patient = patientsList.stream()
                            .filter(p -> p.getPatientId() == patientId)
                            .findFirst()
                            .orElse(null);

                    if (patient == null) {
                        UIHelper.showAlert("Error", "Selected patient not found.");
                        return null;
                    }

                    String patientFirstName = patient.getName();
                    String patientLastName = patient.getSurname();

                    int medicationId = Integer.parseInt(selectedMedication.split(":")[0].trim());

                    Medication medication = medicationsList.stream()
                            .filter(m -> m.getMedicationId() == medicationId)
                            .findFirst()
                            .orElse(null);

                    if (medication == null) {
                        UIHelper.showAlert("Error", "Selected medication not found.");
                        return null;
                    }

                    String medicationName = medication.getMedicationName();
                    String medicationDescription = descriptionArea.getText().trim();
                    String dosage = dosageField.getText().trim();
                    String duration = durationField.getText().trim();

                    if (dosage.isEmpty() || duration.isEmpty()) {
                        UIHelper.showAlert("Validation Error", "Dosage and duration are required.");
                        return null;
                    }

                    Prescription prescription;
                    if (existingPrescription != null) {
                        prescription = existingPrescription;
                        prescription.setPatientId(patientId);
                        prescription.setPatientFirstName(patientFirstName);
                        prescription.setPatientLastName(patientLastName);
                        prescription.setMedicationId(medicationId);
                        prescription.setMedicationName(medicationName);
                        prescription.setMedicationDescription(medicationDescription);
                        prescription.setDosage(dosage);
                        prescription.setDuration(duration);
                        if (prescription.getDatePrescribed() == null) {
                            prescription.setDatePrescribed(LocalDateTime.now());
                        }
                    } else {

                        prescription = new Prescription();
                        prescription.setPatientId(patientId);
                        prescription.setPatientFirstName(patientFirstName);
                        prescription.setPatientLastName(patientLastName);
                        prescription.setMedicationId(medicationId);
                        prescription.setMedicationName(medicationName);
                        prescription.setMedicationDescription(medicationDescription);
                        prescription.setDosage(dosage);
                        prescription.setDuration(duration);
                        prescription.setDatePrescribed(LocalDateTime.now());
                    }

                    return prescription;
                } catch (Exception e) {
                    UIHelper.showAlert("Input Error", "Please check your inputs: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }


    public static void showPrescriptionDetailsDialog(Prescription prescription) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Prescription Details");
        dialog.setHeaderText("Prescription ID: " + prescription.getPrescriptionId());


        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("Patient: ").append(prescription.getPatientFirstName()).append(" ")
                .append(prescription.getPatientLastName()).append("\n\n");


        contentBuilder.append("Medication: ").append(prescription.getMedicationName()).append("\n\n");
        contentBuilder.append("Description: ").append(prescription.getMedicationDescription()).append("\n\n");
        contentBuilder.append("Dosage: ").append(prescription.getDosage()).append("\n\n");
        contentBuilder.append("Duration: ").append(prescription.getDuration()).append("\n\n");

        contentBuilder.append("Prescribed by: Dr. ").append(prescription.getDoctorName()).append(" ")
                .append(prescription.getDoctorSurname()).append("\n\n");

        contentBuilder.append("Date Prescribed: ").append(
                prescription.getDatePrescribed() != null ?
                        prescription.getDatePrescribed().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
                        "Not specified").append("\n\n");


        contentBuilder.append("Department: ").append(prescription.getDepartmentName());

        TextArea textArea = new TextArea(contentBuilder.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(400);

        VBox content = new VBox(10);
        content.getChildren().add(textArea);
        content.setPadding(new Insets(10));

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().add("prescription-details-dialog");
        dialogPane.getStylesheets().add(
                PrescriptionDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm()
        );

        dialogPane.setContent(content);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new javafx.scene.image.Image(PrescriptionDialog.class.getResourceAsStream("/com/inteliMedExpress/resources/images/logo.png")));
        dialog.showAndWait();
    }
}