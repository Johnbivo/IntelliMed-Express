package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.*;
import com.inteliMedExpress.classes.appointments.Appointment;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.patients.PatientDialog;
import com.inteliMedExpress.classes.patients.PatientService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContentDisplay;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class GeneralMedicineDoctorController {

    @FXML
    private Label doctorName;
    @FXML
    private Label general_medicine_title;
    @FXML
    private ToolBar toolbar;

    // Patient Table
    @FXML
    private TableView<Patient> patientsTableView;
    @FXML
    private TableColumn<Patient, Integer> patientIdColumn;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, String> surnameColumn;
    @FXML
    private TableColumn<Patient, String> emailColumn;
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    @FXML
    private TableColumn<Patient, String> addressColumn;
    @FXML
    private TableColumn<Patient, Integer> ageColumn;
    @FXML
    private TableColumn<Patient, String> genderColumn;
    @FXML
    private TableColumn<Patient, LocalDate> birthDateColumn;
    @FXML
    private TableColumn<Patient, String> statusColumn;

    private ObservableList<Patient> patientsList = FXCollections.observableArrayList();

    // Toolbar
    @FXML
    private Button logout_button;
    @FXML
    private Button appointments_button;
    @FXML
    private Button patients_button;
    @FXML
    private Button medical_records_button;
    @FXML
    private Button AI_diagnostics_button;
    @FXML
    private Button lab_tests_button;
    @FXML
    private Button hospital_beds_button;

    private String loggedInDoctorName;

    // Patient Buttons
    @FXML
    private Button add_patient_button;
    @FXML
    private Button update_patient_button;
    @FXML
    private Button delete_patient_button;
    @FXML
    private Button refresh_button;

    private ImageView staticRefreshIcon;
    private ImageView animatedRefreshIcon;

    private PatientService patientService;
    private NavigationManager navigationManager;

    // Appointments Table
    @FXML
    private TableView<Appointment> appointmentsViewTable;
    @FXML
    private TableColumn<Appointment, Integer> appointmentID_column;
    @FXML
    private TableColumn<Appointment, String> patient_name_column;
    @FXML
    private TableColumn<Appointment, String> patient_surname_column;
    @FXML
    private TableColumn<Appointment, String> doctor_surname_column;
    @FXML
    private TableColumn<Appointment, String> nurse_surname_column;
    @FXML
    private TableColumn<Appointment, LocalDate> appointment_date_column;
    @FXML
    private TableColumn<Appointment, String> appointment_status_column;
    @FXML
    private TableColumn<Appointment, String> appointment_notes_column;

    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    // Appointment buttons
    @FXML
    private Button create_appointment_button;
    @FXML
    private Button modify_appointment_button;
    @FXML
    private Button delete_appointment_button;
    @FXML
    private Button refresh_appointment_button;

    private ImageView staticRefreshIconAppointment;
    private ImageView animatedRefreshIconAppointment;

    @FXML
    public void initialize() {
        try {
            // Initial setup - doctor greeting
            updateDoctorGreeting();

            // Create required services
            patientService = new PatientService();

            // Setup patient table
            setupPatientTable();

            // Initialize refresh buttons (if files exist)
            try {
                initializeRefreshButtons();
            } catch (Exception e) {
                System.err.println("Warning: Could not initialize refresh buttons: " + e.getMessage());
                // Continue initialization even if refresh buttons fail
            }

            // Setup appointment table
            setupAppointmentTable();

            // Setup navigation buttons
            setupNavigationButtons();

            // Initially show appointments view
            handleNavigation(appointments_button);
        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupPatientTable() {
        // Configure patient table
        patientsTableView.setEditable(false);
        TableView.TableViewSelectionModel<Patient> selectionModel = patientsTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        // Configure patient table columns
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Date Formatter
        birthDateColumn.setCellFactory(column -> {
            return new TableCell<Patient, LocalDate>() {
                @Override
                protected void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    if (empty || date == null) {
                        setText(null);
                    } else {
                        setText(date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
                    }
                }
            };
        });

        patientsTableView.setItems(patientsList);

        // Setup patient action buttons
        add_patient_button.setOnAction(event -> handleAddPatient());
        update_patient_button.setOnAction(event -> handleUpdatePatient());
        delete_patient_button.setOnAction(event -> handleDeletePatient());
    }

    private void setupAppointmentTable() {
        // Configure appointment table
        TableView.TableViewSelectionModel<Appointment> selectionModel = appointmentsViewTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        // Configure appointment table columns
        appointmentID_column.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        patient_name_column.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patient_surname_column.setCellValueFactory(new PropertyValueFactory<>("patientSurname"));
        doctor_surname_column.setCellValueFactory(new PropertyValueFactory<>("doctorSurname"));
        nurse_surname_column.setCellValueFactory(new PropertyValueFactory<>("nurseSurname"));
        appointment_date_column.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        appointment_status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        appointment_notes_column.setCellValueFactory(new PropertyValueFactory<>("notes"));

        appointmentsViewTable.setItems(appointmentList);

        // Setup appointment action buttons - add event handlers
        create_appointment_button.setOnAction(event -> handleCreateAppointment());
        modify_appointment_button.setOnAction(event -> handleModifyAppointment());
        delete_appointment_button.setOnAction(event -> handleDeleteAppointment());
    }

    private void setupNavigationButtons() {
        // Setup navigation button actions
        appointments_button.setOnAction(event -> handleNavigation(appointments_button));
        patients_button.setOnAction(event -> handleNavigation(patients_button));
        medical_records_button.setOnAction(event -> handleNavigation(medical_records_button));
        AI_diagnostics_button.setOnAction(event -> handleNavigation(AI_diagnostics_button));
        lab_tests_button.setOnAction(event -> handleNavigation(lab_tests_button));
        hospital_beds_button.setOnAction(event -> handleNavigation(hospital_beds_button));

        // Create navigation manager
        List<Button> navButtons = Arrays.asList(
                appointments_button,
                patients_button,
                medical_records_button,
                AI_diagnostics_button,
                lab_tests_button,
                hospital_beds_button
        );

        try {
            navigationManager = new NavigationManager(navButtons);
        } catch (Exception e) {
            System.err.println("Warning: Could not initialize navigation manager: " + e.getMessage());
            // Fall back to simple navigation without style management
        }
    }

    private void handleNavigation(Button selectedButton) {
        try {
            // Use NavigationManager to handle button styling if available
            if (navigationManager != null) {
                navigationManager.selectButton(selectedButton);
            } else {
                // Fallback styling if NavigationManager is unavailable
                resetAllButtons();
                selectedButton.setStyle("-fx-background-color: #e6f2ff; -fx-text-fill: #0052cc; -fx-font-weight: bold;");
            }

            // Hide all components first
            disablePatientComponents();
            disableAppointmentComponents();

            // Show the appropriate view based on button
            if (selectedButton == patients_button) {
                enablePatientComponents();
                loadPatientsFromServer();
            } else if (selectedButton == appointments_button) {
                enableAppointmentComponents();
                loadAllAppointments();
            }
            // Other navigation options would be handled here
        } catch (Exception e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void resetAllButtons() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: #1e67a8; -fx-font-weight: normal;";
        appointments_button.setStyle(defaultStyle);
        patients_button.setStyle(defaultStyle);
        medical_records_button.setStyle(defaultStyle);
        AI_diagnostics_button.setStyle(defaultStyle);
        lab_tests_button.setStyle(defaultStyle);
        hospital_beds_button.setStyle(defaultStyle);
    }

    public void setDoctorName(String doctorName) {
        this.loggedInDoctorName = doctorName;
        updateDoctorGreeting();
    }

    private void updateDoctorGreeting() {
        if (loggedInDoctorName != null && !loggedInDoctorName.isEmpty()) {
            doctorName.setText("Welcome, Dr. " + loggedInDoctorName);
        } else {
            doctorName.setText("Welcome, Doctor");
        }
    }

    private void initializeRefreshButton() {
        try {
            // Load static icon for normal state
            staticRefreshIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-static.png")));
            staticRefreshIcon.setFitHeight(20);
            staticRefreshIcon.setFitWidth(20);

            // Load animated GIF for active state
            animatedRefreshIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-animated.gif")));
            animatedRefreshIcon.setFitHeight(20);
            animatedRefreshIcon.setFitWidth(20);

            // Set initial icon as static
            refresh_button.setGraphic(staticRefreshIcon);
            refresh_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load refresh icons: " + e.getMessage());
            // Continue without icons
        }

        // Set action for refresh button
        refresh_button.setOnAction(event -> {
            refresh_button.setGraphic(animatedRefreshIcon != null ? animatedRefreshIcon : null);
            refresh_button.setDisable(true);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                loadPatientsFromServer();
                refresh_button.setGraphic(staticRefreshIcon != null ? staticRefreshIcon : null);
                refresh_button.setDisable(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    private void initializeRefreshButtons() {
        // Initialize patient refresh button
        initializeRefreshButton();

        // Initialize appointment refresh button using same resources
        try {
            // Reuse the same icon resources for appointments
            staticRefreshIconAppointment = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-static.png")));
            staticRefreshIconAppointment.setFitHeight(20);
            staticRefreshIconAppointment.setFitWidth(20);

            animatedRefreshIconAppointment = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-animated.gif")));
            animatedRefreshIconAppointment.setFitHeight(20);
            animatedRefreshIconAppointment.setFitWidth(20);

            refresh_appointment_button.setGraphic(staticRefreshIconAppointment);
            refresh_appointment_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_appointment_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load appointment refresh icons: " + e.getMessage());
            // Continue without icons
        }

        // Set action for appointment refresh button
        refresh_appointment_button.setOnAction(event -> {
            refresh_appointment_button.setGraphic(animatedRefreshIconAppointment != null ? animatedRefreshIconAppointment : null);
            refresh_appointment_button.setDisable(true);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                loadAllAppointments();
                refresh_appointment_button.setGraphic(staticRefreshIconAppointment != null ? staticRefreshIconAppointment : null);
                refresh_appointment_button.setDisable(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }

    // Patients dashboard
    private void disablePatientComponents(){
        patientsTableView.setVisible(false);
        add_patient_button.setVisible(false);
        update_patient_button.setVisible(false);
        delete_patient_button.setVisible(false);
        refresh_button.setVisible(false);
    }

    private void enablePatientComponents(){
        patientsTableView.setVisible(true);
        add_patient_button.setVisible(true);
        update_patient_button.setVisible(true);
        delete_patient_button.setVisible(true);
        refresh_button.setVisible(true);
    }

    // Appointments dashboard
    private void disableAppointmentComponents(){
        appointmentsViewTable.setVisible(false);
        create_appointment_button.setVisible(false);
        modify_appointment_button.setVisible(false);
        delete_appointment_button.setVisible(false);
        refresh_appointment_button.setVisible(false);
    }

    private void enableAppointmentComponents(){
        appointmentsViewTable.setVisible(true);
        create_appointment_button.setVisible(true);
        modify_appointment_button.setVisible(true);
        delete_appointment_button.setVisible(true);
        refresh_appointment_button.setVisible(true);
    }

    @FXML
    public void logout(ActionEvent event) {
        try {
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/inteliMedExpress/resources/fxml/login.fxml"));
            Parent loginRoot = loader.load();

            // Create a new scene with the login form
            Scene loginScene = new Scene(loginRoot);

            // Get the current stage from the event source
            Stage currentStage = (Stage) logout_button.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(loginScene);
            currentStage.setTitle("InteliMedExpress - Login");
            currentStage.centerOnScreen();

            System.out.println("Navigation to login form successful");
        } catch (IOException e) {
            UIHelper.showAlert("Navigation Error", "Could not load login form: " + e.getMessage());
            System.err.println("Error navigating to login form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadPatientsFromServer() {
        // Create a background thread to fetch data
        new Thread(() -> {
            try {
                // Use the service to get patients
                List<Patient> serverPatients = patientService.getAllPatients();

                // Update UI on the JavaFX application thread
                Platform.runLater(() -> {
                    patientsList.clear();
                    patientsList.addAll(serverPatients);
                });
            } catch (IOException e) {
                // Handle error on JavaFX thread
                Platform.runLater(() -> {
                    UIHelper.showAlert("Connection Error", "Failed to fetch patients from server: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleAddPatient() {
        try {
            Stage stage = (Stage) add_patient_button.getScene().getWindow();
            Patient newPatient = PatientDialog.showAddPatientDialog(stage);

            if (newPatient != null) {
                boolean success = patientService.addPatient(newPatient);
                if (success) {
                    UIHelper.showAlert("Success", "Patient added successfully!");
                    loadPatientsFromServer(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to add patient to the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error adding patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdatePatient() {
        Patient selectedPatient = patientsTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            UIHelper.showAlert("Selection Required", "Please select a patient to update.");
            return;
        }

        try {
            Stage stage = (Stage) update_patient_button.getScene().getWindow();
            Patient updatedPatient = PatientDialog.showUpdatePatientDialog(stage, selectedPatient);

            if (updatedPatient != null) {
                boolean success = patientService.updatePatient(updatedPatient);
                if (success) {
                    UIHelper.showAlert("Success", "Patient updated successfully!");
                    loadPatientsFromServer(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to update patient on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error updating patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePatient() {
        Patient selectedPatient = patientsTableView.getSelectionModel().getSelectedItem();

        if (selectedPatient == null) {
            UIHelper.showAlert("Selection Required", "Please select a patient to delete.");
            return;
        }

        try {
            Stage stage = (Stage) delete_patient_button.getScene().getWindow();
            boolean confirmed = PatientDialog.showDeleteConfirmationDialog(stage, selectedPatient);

            if (confirmed) {
                boolean success = patientService.deletePatient(selectedPatient.getPatientId());
                if (success) {
                    UIHelper.showAlert("Success", "Patient deleted successfully!");
                    loadPatientsFromServer(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to delete patient from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting patient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllAppointments() {
        // Create a background thread to fetch data
        new Thread(() -> {
            try {
                // Use the static method from Appointment class
                List<Appointment> serverAppointments = Appointment.getAllAppointments();

                // Update UI on the JavaFX application thread
                Platform.runLater(() -> {
                    appointmentList.clear();
                    appointmentList.addAll(serverAppointments);
                });
            } catch (IOException e) {
                // Handle error on JavaFX thread
                Platform.runLater(() -> {
                    UIHelper.showAlert("Connection Error", "Failed to fetch appointments from server: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // Add stub methods for appointment handlers
    private void handleCreateAppointment() {
        // Placeholder for appointment creation logic
        UIHelper.showAlert("Feature Coming Soon", "Create appointment functionality is not yet implemented.");
    }

    private void handleModifyAppointment() {
        // Placeholder for appointment modification logic
        Appointment selectedAppointment = appointmentsViewTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            UIHelper.showAlert("Selection Required", "Please select an appointment to modify.");
            return;
        }

        UIHelper.showAlert("Feature Coming Soon", "Modify appointment functionality is not yet implemented.");
    }

    private void handleDeleteAppointment() {
        // Placeholder for appointment deletion logic
        Appointment selectedAppointment = appointmentsViewTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            UIHelper.showAlert("Selection Required", "Please select an appointment to delete.");
            return;
        }

        UIHelper.showAlert("Feature Coming Soon", "Delete appointment functionality is not yet implemented.");
    }
}