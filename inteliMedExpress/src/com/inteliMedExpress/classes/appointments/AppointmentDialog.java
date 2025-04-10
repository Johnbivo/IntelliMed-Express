package com.inteliMedExpress.classes.appointments;

import com.inteliMedExpress.classes.UIHelper;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AppointmentDialog {

    public static Appointment showAddAppointmentDialog(Stage parentStage) {
        return showAppointmentDialog(parentStage, null, "Add New Appointment");
    }

    public static Appointment showUpdateAppointmentDialog(Stage parentStage, Appointment appointment) {
        return showAppointmentDialog(parentStage, appointment, "Update Appointment");
    }

    public static boolean showDeleteConfirmationDialog(Stage parentStage, Appointment appointment) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initOwner(parentStage);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Appointment Record");

        // Format the date-time for display
        String formattedDateTime = appointment.getAppointmentDate() != null ?
                appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
                "unknown date";

        alert.setContentText("Are you sure you want to delete appointment for: " +
                appointment.getPatientName() + " " + appointment.getPatientSurname() +
                " on " + formattedDateTime + " (ID: " + appointment.getAppointmentId() + ")?");

        // Apply custom styling to the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                AppointmentDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
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

    private static Appointment showAppointmentDialog(Stage parentStage, Appointment existingAppointment, String title) {
        // Create the custom dialog
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle(title);
        dialog.setHeaderText(existingAppointment == null ? "Enter Appointment Details" : "Update Appointment Details");

        // Apply custom styling to the dialog
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                AppointmentDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());

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

        // Create text fields for appointment information
        TextField patientNameField = new TextField();
        TextField patientSurnameField = new TextField();
        TextField doctorSurnameField = new TextField();
        TextField nurseSurnameField = new TextField();

        // Date picker for appointment date
        DatePicker appointmentDatePicker = new DatePicker();

        // Time picker components (hours and minutes)
        ComboBox<String> hourPicker = new ComboBox<>();
        for (int i = 0; i < 24; i++) {
            hourPicker.getItems().add(String.format("%02d", i));
        }

        ComboBox<String> minutePicker = new ComboBox<>();
        for (int i = 0; i < 60; i += 15) { // 15-minute intervals
            minutePicker.getItems().add(String.format("%02d", i));
        }

        // Appointment status
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Scheduled", "Confirmed", "Completed", "Canceled", "No-Show");
        statusComboBox.setPromptText("Select Status");
        TextArea notesArea = new TextArea();
        notesArea.setPrefRowCount(3);

        // Set preferred width for consistent form field sizes
        patientNameField.setPrefWidth(250);
        patientSurnameField.setPrefWidth(250);
        doctorSurnameField.setPrefWidth(250);
        nurseSurnameField.setPrefWidth(250);
        appointmentDatePicker.setPrefWidth(250);
        hourPicker.setPrefWidth(60);
        minutePicker.setPrefWidth(60);
        statusComboBox.setPrefWidth(250);
        notesArea.setPrefWidth(250);

        // Populate fields if updating
        if (existingAppointment != null) {
            patientNameField.setText(existingAppointment.getPatientName());
            patientSurnameField.setText(existingAppointment.getPatientSurname());
            doctorSurnameField.setText(existingAppointment.getDoctorSurname());
            nurseSurnameField.setText(existingAppointment.getNurseSurname());

            // Handle the date and time
            if (existingAppointment.getAppointmentDate() != null) {
                // Set the date
                appointmentDatePicker.setValue(existingAppointment.getAppointmentDate().toLocalDate());

                // Set the time
                LocalTime time = existingAppointment.getAppointmentDate().toLocalTime();
                hourPicker.setValue(String.format("%02d", time.getHour()));
                minutePicker.setValue(String.format("%02d", (time.getMinute() / 15) * 15)); // Round to nearest 15 min
            } else {
                // Default values
                appointmentDatePicker.setValue(LocalDate.now());
                hourPicker.setValue("09"); // 9 AM default
                minutePicker.setValue("00"); // 0 minutes default
            }

            statusComboBox.setValue(existingAppointment.getStatus());
            notesArea.setText(existingAppointment.getNotes());
        } else {
            // Default values for new appointment
            appointmentDatePicker.setValue(LocalDate.now());
            hourPicker.setValue("09"); // 9 AM default
            minutePicker.setValue("00"); // 0 minutes default
        }

        // Create time picker layout
        HBox timeBox = new HBox(10);
        timeBox.getChildren().addAll(hourPicker, new Label(":"), minutePicker);

        // Add labels and fields to the grid
        grid.add(new Label("Patient Name:"), 0, 0);
        grid.add(patientNameField, 1, 0);
        grid.add(new Label("Patient Surname:"), 0, 1);
        grid.add(patientSurnameField, 1, 1);
        grid.add(new Label("Doctor Surname:"), 0, 2);
        grid.add(doctorSurnameField, 1, 2);
        grid.add(new Label("Nurse Surname:"), 0, 3);
        grid.add(nurseSurnameField, 1, 3);
        grid.add(new Label("Appointment Date:"), 0, 4);
        grid.add(appointmentDatePicker, 1, 4);
        grid.add(new Label("Appointment Time:"), 0, 5);
        grid.add(timeBox, 1, 5);
        grid.add(new Label("Status:"), 0, 6);
        grid.add(statusComboBox, 1, 6);
        grid.add(new Label("Notes:"), 0, 7);
        grid.add(notesArea, 1, 7);

        dialog.getDialogPane().setContent(grid);

        // Add some spacing to improve layout
        dialogPane.setPrefWidth(450);
        dialogPane.setPrefHeight(600); // Increased height to accommodate the time picker

        // Request focus on the patient name field by default
        patientNameField.requestFocus();

        // Convert the result to an Appointment object when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String patientName = patientNameField.getText().trim();
                    String patientSurname = patientSurnameField.getText().trim();
                    String doctorSurname = doctorSurnameField.getText().trim();
                    String nurseSurname = nurseSurnameField.getText().trim();
                    LocalDate appointmentDate = appointmentDatePicker.getValue();

                    // Get time values
                    int hour = Integer.parseInt(hourPicker.getValue());
                    int minute = Integer.parseInt(minutePicker.getValue());

                    // Combine date and time
                    LocalDateTime appointmentDateTime = LocalDateTime.of(
                            appointmentDate,
                            LocalTime.of(hour, minute)
                    );

                    String status = statusComboBox.getValue();
                    String notes = notesArea.getText().trim();

                    // Validate required fields
                    if (patientName.isEmpty() || patientSurname.isEmpty() || doctorSurname.isEmpty() ||
                            appointmentDate == null || status == null) {
                        UIHelper.showAlert("Validation Error", "Patient name, patient surname, doctor surname, appointment date, and status are required.");
                        return null;
                    }

                    // Create a new Appointment object or update existing
                    Appointment appointment;
                    if (existingAppointment != null) {
                        appointment = existingAppointment;
                        appointment.setPatientName(patientName);
                        appointment.setPatientSurname(patientSurname);
                        appointment.setDoctorSurname(doctorSurname);
                        appointment.setNurseSurname(nurseSurname);
                        appointment.setAppointmentDate(appointmentDateTime);
                        appointment.setStatus(status);
                        appointment.setNotes(notes);
                    } else {
                        appointment = new Appointment(null, patientName, patientSurname,
                                doctorSurname, nurseSurname, appointmentDateTime, status, notes);
                    }

                    return appointment;
                } catch (Exception e) {
                    UIHelper.showAlert("Input Error", "Please check your inputs: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }





    // Method to display the appointment details dialog
    public static void showAppointmentDetailsDialog(Appointment appointment) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Appointment Details");
        dialog.setHeaderText("Appointment ID: " + appointment.getAppointmentId());

        // Format the appointment details
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append("Patient: ").append(appointment.getPatientName()).append(" ")
                .append(appointment.getPatientSurname()).append("\n\n");
        contentBuilder.append("Doctor: Dr. ").append(appointment.getDoctorSurname()).append("\n\n");
        contentBuilder.append("Nurse: ").append(appointment.getNurseSurname()).append("\n\n");
        contentBuilder.append("Date: ").append(
                appointment.getAppointmentDate() != null ?
                        appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) :
                        "N/A").append("\n\n");
        contentBuilder.append("Status: ").append(appointment.getStatus()).append("\n\n");
        contentBuilder.append("Notes:\n").append(appointment.getNotes());

        // Create a text area to show the details
        TextArea textArea = new TextArea(contentBuilder.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(500);
        textArea.setPrefHeight(400);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStyleClass().add("appointment-details-dialog");
        dialogPane.getStylesheets().add(
                AppointmentDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm()
        );

        dialogPane.setContent(textArea);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        // Add app icon
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new javafx.scene.image.Image(AppointmentDialog.class.getResourceAsStream("/com/inteliMedExpress/resources/images/logo.png")));

        dialog.showAndWait();
    }


}