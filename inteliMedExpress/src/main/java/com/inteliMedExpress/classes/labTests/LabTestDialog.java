package com.inteliMedExpress.classes.labTests;

import com.inteliMedExpress.classes.UIHelper;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.patients.PatientService;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LabTestDialog {

    private static final String[] TEST_TYPES = {
            "Blood Test", "Urine Test", "X-Ray", "CT Scan", "MRI",
            "Ultrasound", "Biopsy"
    };

    private static final String[] DEPARTMENTS = {
            "Radiology", "Microbiology"
    };

    // Show dialog to request a new lab test
    public static LabTest showRequestLabTestDialog(Stage owner, String doctorName, String doctorSurname,
                                                   int doctorId, String doctorSpecialty,
                                                   int departmentId, String departmentName) throws IOException {
        // Create the custom dialog
        Dialog<LabTest> dialog = new Dialog<>();
        dialog.setTitle("Request Lab Test");
        dialog.setHeaderText("Request a new lab test for a patient");
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Apply custom styling to the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                LabTestDialog.class.getResource("/resources/com/inteliMedExpress/css/patient_dialogs.css").toExternalForm());

        // Set the button types
        ButtonType requestButtonType = new ButtonType("Request Test", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(requestButtonType, ButtonType.CANCEL);

        // Add a handler to apply the style class once the dialog is shown
        dialog.setOnShown(event -> {
            Button requestButton = (Button) dialogPane.lookupButton(requestButtonType);
            if (requestButton != null) {
                requestButton.getStyleClass().add("default-button");
            }
        });

        // Create UI components
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.getStyleClass().add("grid-pane");

        // Get all patients for selection
        PatientService patientService = new PatientService();
        List<Patient> patients = patientService.getAllPatients();
        List<Patient> patientsList = new ArrayList<>(patients); // Keep track for later use

        // Patient selection
        ComboBox<String> patientComboBox = new ComboBox<>();
        patientComboBox.setPromptText("Select Patient");
        patientComboBox.setPrefWidth(250);

        // Add patients to the combo box
        for (Patient patient : patients) {
            patientComboBox.getItems().add(patient.getPatientId() + ": " +
                    patient.getName() + " " +
                    patient.getSurname());
        }

        // Test type selection
        ComboBox<String> testTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(TEST_TYPES));
        testTypeComboBox.setPromptText("Select Test Type");
        testTypeComboBox.setPrefWidth(250);

        // Department selection (where the test will be performed)
        ComboBox<String> departmentComboBox = new ComboBox<>(FXCollections.observableArrayList(DEPARTMENTS));
        departmentComboBox.setPromptText("Select Target Department");
        departmentComboBox.setPrefWidth(250);

        // Notes field
        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(3);
        notesArea.setPrefWidth(250);

        // Add fields to grid
        int row = 0;
        grid.add(new Label("Patient:"), 0, row);
        grid.add(patientComboBox, 1, row++);

        grid.add(new Label("Test Type:"), 0, row);
        grid.add(testTypeComboBox, 1, row++);

        grid.add(new Label("Target Department:"), 0, row);
        grid.add(departmentComboBox, 1, row++);

        grid.add(new Label("Notes:"), 0, row);
        grid.add(notesArea, 1, row);

        // Requesting doctor information (display only)
        Separator separator = new Separator();
        grid.add(separator, 0, ++row, 2, 1);

        Label requestingLabel = new Label("Requesting Doctor Information:");
        requestingLabel.setStyle("-fx-font-weight: bold;");
        grid.add(requestingLabel, 0, ++row, 2, 1);

        grid.add(new Label("Doctor:"), 0, ++row);
        grid.add(new Label("Dr. " + doctorName), 1, row);

        grid.add(new Label("Department:"), 0, ++row);
        grid.add(new Label(departmentName), 1, row);

        dialog.getDialogPane().setContent(grid);

        // Set dialog size
        dialogPane.setPrefWidth(450);
        dialogPane.setPrefHeight(550);

        // Request focus on the patient field by default
        patientComboBox.requestFocus();

        // Convert the result to a lab test object when the request button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == requestButtonType) {
                try {
                    String selectedPatient = patientComboBox.getValue();
                    String testType = testTypeComboBox.getValue();
                    String targetDepartment = departmentComboBox.getValue();
                    String notes = notesArea.getText().trim();

                    if (selectedPatient == null || selectedPatient.isEmpty() ||
                            testType == null || targetDepartment == null || targetDepartment.isEmpty()) {
                        UIHelper.showAlert("Missing Information", "Please fill out all required fields.");
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

                    // Create a new lab test object
                    LabTest labTest = new LabTest(
                            testType,
                            patient.getPatientId(),
                            patient.getName(),
                            patient.getSurname(),
                            0, // Will be assigned by the target department
                            "", // Will be assigned by the target department
                            "", // Will be assigned by the target department
                            targetDepartment
                    );

                    // Set the requesting doctor and department information
                    labTest.setAsRequestingDoctor(doctorId, doctorName, doctorSurname, doctorSpecialty, departmentId, departmentName);

                    // Set additional fields
                    labTest.setNotes(notes.isEmpty() ? null : notes);
                    labTest.setOrderDate(LocalDateTime.now());
                    labTest.setStatus("Ordered");

                    return labTest;
                } catch (Exception e) {
                    UIHelper.showAlert("Error", "Error creating lab test: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        // Add app icon if available
        try {
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(LabTestDialog.class.getResourceAsStream("/resources/com/inteliMedExpress/images/logo.png")));
        } catch (Exception e) {
            // If icon loading fails, just continue
            System.err.println("Could not load application icon: " + e.getMessage());
        }

        // Show the dialog and wait for a response
        Optional<LabTest> result = dialog.showAndWait();
        return result.orElse(null);
    }

    // Show dialog to assign a doctor to a test
    public static boolean showAssignDoctorDialog(Stage owner, LabTest labTest, int doctorId, String doctorName, String doctorSurname) {
        // Create the custom dialog
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Assign Doctor to Lab Test");
        dialog.setHeaderText("Assign yourself to perform this lab test");
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Apply custom styling to the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                LabTestDialog.class.getResource("/resources/com/inteliMedExpress/css/patient_dialogs.css").toExternalForm());

        // Set the button types
        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        // Add a handler to apply the style class once the dialog is shown
        dialog.setOnShown(event -> {
            Button assignButton = (Button) dialogPane.lookupButton(assignButtonType);
            if (assignButton != null) {
                assignButton.getStyleClass().add("default-button");
            }
        });

        // Create UI for displaying test details
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("content-box");

        // Display test details
        content.getChildren().addAll(
                createDetailRow("Test ID:", String.valueOf(labTest.getTestId())),
                createDetailRow("Test Type:", labTest.getTestType()),
                createDetailRow("Patient:", labTest.getPatientFullName()),
                createDetailRow("Requested By:", "Dr. " + labTest.getRequestingDoctorSurname() + " (" + labTest.getRequestingDepartmentName() + ")"),
                createDetailRow("Ordered On:", labTest.getOrderDate() != null ?
                        labTest.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A")
        );

        // Add confirmation text
        Separator separator = new Separator();
        Label confirmLabel = new Label("You are about to assign yourself to this test:");
        Label doctorLabel = new Label("Dr. " + doctorName + " " + doctorSurname);
        doctorLabel.setStyle("-fx-font-weight: bold;");

        content.getChildren().addAll(separator, confirmLabel, doctorLabel);

        dialog.getDialogPane().setContent(content);

        // Set dialog size
        dialogPane.setPrefWidth(400);
        dialogPane.setPrefHeight(350);

        // Convert the result to a boolean when the assign button is clicked
        dialog.setResultConverter(dialogButton -> dialogButton == assignButtonType);

        // Add app icon if available
        try {
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(LabTestDialog.class.getResourceAsStream("/resources/com/inteliMedExpress/images/logo.png")));
        } catch (Exception e) {
            // If icon loading fails, just continue
            System.err.println("Could not load application icon: " + e.getMessage());
        }

        // Show the dialog and wait for a response
        return dialog.showAndWait().orElse(false);
    }

    // Show dialog to complete a lab test with results
    public static LabTest showCompleteLabTestDialog(Stage owner, LabTest labTest) {
        // Create the custom dialog
        Dialog<LabTest> dialog = new Dialog<>();
        dialog.setTitle("Complete Lab Test");
        dialog.setHeaderText("Enter test results for Test ID: " + labTest.getTestId());
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Apply custom styling to the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                LabTestDialog.class.getResource("/resources/com/inteliMedExpress/css/patient_dialogs.css").toExternalForm());

        // Set the button types
        ButtonType completeButtonType = new ButtonType("Complete Test", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(completeButtonType, ButtonType.CANCEL);

        // Add a handler to apply the style class once the dialog is shown
        dialog.setOnShown(event -> {
            Button completeButton = (Button) dialogPane.lookupButton(completeButtonType);
            if (completeButton != null) {
                completeButton.getStyleClass().add("default-button");
            }
        });

        // Create UI components
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));
        grid.getStyleClass().add("grid-pane");

        // Test details display
        int row = 0;
        grid.add(new Label("Patient:"), 0, row);
        grid.add(new Label(labTest.getPatientFullName()), 1, row++);

        grid.add(new Label("Test Type:"), 0, row);
        grid.add(new Label(labTest.getTestType()), 1, row++);

        grid.add(new Label("Requested By:"), 0, row);
        grid.add(new Label("Dr. " + labTest.getRequestingDoctorSurname()), 1, row++);

        grid.add(new Label("Order Date:"), 0, row);
        grid.add(new Label(labTest.getOrderDate() != null ?
                labTest.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A"), 1, row++);

        // Separator
        Separator separator = new Separator();
        grid.add(separator, 0, row++, 2, 1);

        // Result fields
        Label resultLabel = new Label("Test Results:");
        resultLabel.setStyle("-fx-font-weight: bold;");
        grid.add(resultLabel, 0, row++, 2, 1);

        TextArea resultArea = new TextArea();
        resultArea.setPrefRowCount(5);
        resultArea.setPrefWidth(400);
        grid.add(resultArea, 0, row++, 2, 1);

        grid.add(new Label("Additional Notes:"), 0, row++, 2, 1);

        TextArea notesArea = new TextArea(labTest.getNotes());
        notesArea.setPrefRowCount(3);
        grid.add(notesArea, 0, row, 2, 1);

        dialog.getDialogPane().setContent(grid);

        // Set dialog size
        dialogPane.setPrefWidth(500);
        dialogPane.setPrefHeight(500);

        // Request focus on the results field by default
        resultArea.requestFocus();

        // Convert the result to an updated lab test object when the complete button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == completeButtonType) {
                String result = resultArea.getText().trim();
                String notes = notesArea.getText().trim();

                if (result.isEmpty()) {
                    UIHelper.showAlert("Missing Information", "Please enter test results.");
                    return null;
                }

                // Update the lab test with results
                labTest.setResult(result);
                labTest.setNotes(notes.isEmpty() ? null : notes);
                labTest.setCompletionDate(LocalDateTime.now());
                labTest.setStatus("Completed");

                return labTest;
            }
            return null;
        });

        // Add app icon if available
        try {
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(LabTestDialog.class.getResourceAsStream("/resources/com/inteliMedExpress/images/logo.png")));
        } catch (Exception e) {
            // If icon loading fails, just continue
            System.err.println("Could not load application icon: " + e.getMessage());
        }

        // Show the dialog and wait for a response
        Optional<LabTest> result = dialog.showAndWait();
        return result.orElse(null);
    }

    // Show lab test details dialog for viewing
    public static void showLabTestDetailsDialog(Stage owner, LabTest labTest) {
        // Create the custom dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Lab Test Details");
        dialog.setHeaderText("Test ID: " + labTest.getTestId() + " - " + labTest.getTestType());
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Apply custom styling to the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                LabTestDialog.class.getResource("/resources/com/inteliMedExpress/css/patient_dialogs.css").toExternalForm());

        // Set the button types
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Create UI for displaying test details
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.getStyleClass().add("content-box");

        // Patient information section
        Label patientSectionLabel = new Label("Patient Information");
        patientSectionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        content.getChildren().add(patientSectionLabel);

        content.getChildren().addAll(
                createDetailRow("Name:", labTest.getPatientFullName()),
                createDetailRow("Patient ID:", String.valueOf(labTest.getPatientId()))
        );

        content.getChildren().add(new Separator());

        // Test information section
        Label testSectionLabel = new Label("Test Information");
        testSectionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        content.getChildren().add(testSectionLabel);

        content.getChildren().addAll(
                createDetailRow("Test ID:", String.valueOf(labTest.getTestId())),
                createDetailRow("Test Type:", labTest.getTestType()),
                createDetailRow("Department:", labTest.getDepartmentName()),
                createDetailRow("Order Date:", labTest.getOrderDate() != null ?
                        labTest.getOrderDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm")) : "N/A")
        );

        if (labTest.getCompletionDate() != null) {
            content.getChildren().add(createDetailRow("Completion Date:",
                    labTest.getCompletionDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm"))));
        }

        // Requesting information
        content.getChildren().add(new Separator());
        Label requestingLabel = new Label("Requesting Information");
        requestingLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        content.getChildren().add(requestingLabel);

        content.getChildren().addAll(
                createDetailRow("Requesting Doctor:", "Dr. " + labTest.getRequestingDoctorName() + " " + labTest.getRequestingDoctorSurname()),
                createDetailRow("Requesting Department:", labTest.getRequestingDepartmentName())
        );

        // Performing doctor info if available
        if (labTest.getDoctorName() != null && !labTest.getDoctorName().isEmpty()) {
            content.getChildren().add(createDetailRow("Performing Doctor:", "Dr. " + labTest.getDoctorName() + " " + labTest.getDoctorSurname()));
        }

        // Results section if available
        if (labTest.getResult() != null && !labTest.getResult().isEmpty()) {
            content.getChildren().add(new Separator());
            Label resultLabel = new Label("Test Results");
            resultLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            content.getChildren().add(resultLabel);

            TextArea resultArea = new TextArea(labTest.getResult());
            resultArea.setEditable(false);
            resultArea.setWrapText(true);
            resultArea.setPrefHeight(100);
            resultArea.setStyle("-fx-control-inner-background: #f8f8f8;");
            content.getChildren().add(resultArea);
        }

        // Notes section if available
        if (labTest.getNotes() != null && !labTest.getNotes().isEmpty()) {
            content.getChildren().add(new Separator());
            Label notesLabel = new Label("Notes");
            notesLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            content.getChildren().add(notesLabel);

            TextArea notesArea = new TextArea(labTest.getNotes());
            notesArea.setEditable(false);
            notesArea.setWrapText(true);
            notesArea.setPrefHeight(80);
            notesArea.setStyle("-fx-control-inner-background: #f8f8f8;");
            content.getChildren().add(notesArea);
        }

        // Status indicator
        HBox statusBox = new HBox(10);
        statusBox.setAlignment(Pos.CENTER_LEFT);
        Label statusLabel = new Label("Status:");
        statusLabel.setStyle("-fx-font-weight: bold;");

        Label statusValue = new Label(labTest.getStatus());
        // Color-coded status
        String statusColor;
        switch (labTest.getStatus().toLowerCase()) {
            case "completed":
                statusColor = "forestgreen";
                break;
            case "pending":
            case "ordered":
            case "in progress":
                statusColor = "darkorange";
                break;
            case "cancelled":
                statusColor = "crimson";
                break;
            default:
                statusColor = "black";
        }
        statusValue.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold; " +
                "-fx-background-color: #f0f0f0; -fx-padding: 3 8; -fx-background-radius: 3;");

        statusBox.getChildren().addAll(statusLabel, statusValue);
        content.getChildren().add(statusBox);

        // Add all to a ScrollPane for better viewing
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        scrollPane.setPrefWidth(500);

        dialog.getDialogPane().setContent(scrollPane);

        // Set dialog size
        dialogPane.setPrefWidth(550);
        dialogPane.setPrefHeight(600);

        // Add app icon if available
        try {
            Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(LabTestDialog.class.getResourceAsStream("/resources/com/inteliMedExpress/images/logo.png")));
        } catch (Exception e) {
            // If icon loading fails, just continue
            System.err.println("Could not load application icon: " + e.getMessage());
        }

        dialog.showAndWait();
    }

    // Show confirmation dialog for deleting a lab test
    public static boolean showDeleteConfirmationDialog(Stage owner, LabTest labTest) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Lab Test ID: " + labTest.getTestId());
        alert.setContentText("Are you sure you want to delete this lab test for " +
                labTest.getPatientFullName() + "?\nThis action cannot be undone.");
        alert.initOwner(owner);

        // Add custom styling
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                LabTestDialog.class.getResource("/resources/com/inteliMedExpress/css/patient_dialogs.css").toExternalForm());

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

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }

    // Helper method to create detail rows in dialogs
    private static HBox createDetailRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");
        labelNode.setMinWidth(120);

        Label valueNode = new Label(value != null && !value.isEmpty() ? value : "N/A");
        valueNode.setWrapText(true);

        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }

    // Helper method for showing alerts
    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}