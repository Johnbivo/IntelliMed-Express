package com.inteliMedExpress.controllers;

import com.inteliMedExpress.classes.*;
import com.inteliMedExpress.classes.appointments.Appointment;
import com.inteliMedExpress.classes.appointments.AppointmentDialog;
import com.inteliMedExpress.classes.medicalRecords.MedicalRecord;
import com.inteliMedExpress.classes.medicalRecords.MedicalRecordsDialog;
import com.inteliMedExpress.classes.medicalRecords.MedicalRecordsService;
import com.inteliMedExpress.classes.patients.Patient;
import com.inteliMedExpress.classes.patients.PatientDialog;
import com.inteliMedExpress.classes.patients.PatientService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContentDisplay;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class GeneralMedicineDoctorController {

    @FXML
    private Label doctorName;


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





    //Medical Records Table
    @FXML
    private TableView<MedicalRecord> MedicalRecordsTable;
    @FXML
    private TableColumn<MedicalRecord, Integer> MedicalRecordID;
    @FXML
    private TableColumn<MedicalRecord, String> medical_record_name;
    @FXML
    private TableColumn<MedicalRecord, String> medical_record_surname;
    @FXML
    private TableColumn<MedicalRecord, String> medical_record_doc_surname;
    @FXML
    private TableColumn<MedicalRecord, String> record_diagnosis;
    @FXML
    private TableColumn<MedicalRecord, String> record_treatment;
    @FXML
    private TableColumn<MedicalRecord, String> record_prescription;
    @FXML
    private TableColumn<MedicalRecord, String> record_status;
    @FXML
    private TableColumn<MedicalRecord, LocalDate> record_date;

    private ObservableList<MedicalRecord> medicalRecordsList = FXCollections.observableArrayList();


    private MedicalRecordsService medicalRecordsService;
    //Medical Records Buttons



    private ImageView staticRefreshIconRecords;
    private ImageView animatedRefreshIconRecords;




    @FXML
    private Button refresh_records_button;


    @FXML
    private Button add_record_button;
    @FXML
    private Button modify_record_button;
    @FXML
    private Button delete_record_button;
    @FXML
    private Button view_medical_history_button;

















    @FXML
    public void initialize() {
        try {
            // Initial setup - doctor greeting
            updateDoctorGreeting();

            // Create required services
            patientService = new PatientService();
            medicalRecordsService = new MedicalRecordsService();

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


            //Setup medical records table
            setupMedicalRecordTable();

            // Initially show appointments view
            handleNavigation(appointments_button);
        } catch (Exception e) {
            System.err.println("Initialization error: " + e.getMessage());
            e.printStackTrace();
        }
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




    //Setup the navigation buttons

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
        }
    }



    // Refreshing navigation buttons
    private void resetAllButtons() {
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: #1e67a8; -fx-font-weight: normal;";
        appointments_button.setStyle(defaultStyle);
        patients_button.setStyle(defaultStyle);
        medical_records_button.setStyle(defaultStyle);
        AI_diagnostics_button.setStyle(defaultStyle);
        lab_tests_button.setStyle(defaultStyle);
        hospital_beds_button.setStyle(defaultStyle);
    }

    //Getting the doctor name for a greeting

    public void setDoctorName(String doctorName) {
        this.loggedInDoctorName = doctorName;
        updateDoctorGreeting();
    }


    // Setting the text for greeting
    private void updateDoctorGreeting() {
        if (loggedInDoctorName != null && !loggedInDoctorName.isEmpty()) {
            doctorName.setText("Welcome, Dr. " + loggedInDoctorName);
        } else {
            doctorName.setText("Welcome, Doctor");
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
            disableMedicalRecordComponents(); // Make sure to add this line

            // Show the appropriate view based on button
            if (selectedButton == patients_button) {
                enablePatientComponents();
                loadPatientsFromServer();
            } else if (selectedButton == appointments_button) {
                enableAppointmentComponents();
                loadAllAppointments();
            } else if (selectedButton == medical_records_button) {
                enableMedicalRecordComponents();
                loadAllMedicalRecords();
            }
            // Other navigation options would be handled here
        } catch (Exception e) {
            System.err.println("Navigation error: " + e.getMessage());
            e.printStackTrace();
        }
    }




    //Setup of tables


    private void setupPatientTable() {
        // Configure patient table
        patientsTableView.setEditable(false);
        TableView.TableViewSelectionModel<Patient> selectionModel = patientsTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        // Configure patient table columns
        patientIdColumn.setCellValueFactory(new PropertyValueFactory<>("PatientID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("PatientName"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("PatientSurname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("PatientEmail"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("PatientPhone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("PatientAddress"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("PatientAge"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<>("DateOfBirth"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("PatientStatus"));

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
        appointmentID_column.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        patient_name_column.setCellValueFactory(new PropertyValueFactory<>("PatientName"));
        patient_surname_column.setCellValueFactory(new PropertyValueFactory<>("PatientSurname"));
        doctor_surname_column.setCellValueFactory(new PropertyValueFactory<>("DoctorSurname"));
        nurse_surname_column.setCellValueFactory(new PropertyValueFactory<>("NurseSurname"));
        appointment_date_column.setCellValueFactory(new PropertyValueFactory<>("AppointmentDate"));
        appointment_status_column.setCellValueFactory(new PropertyValueFactory<>("Status"));
        appointment_notes_column.setCellValueFactory(new PropertyValueFactory<>("Notes"));

        appointmentsViewTable.setItems(appointmentList);

        // Setup appointment action buttons - add event handlers
        create_appointment_button.setOnAction(event -> handleCreateAppointment());
        modify_appointment_button.setOnAction(event -> handleModifyAppointment());
        delete_appointment_button.setOnAction(event -> handleDeleteAppointment());
    }



    private void setupMedicalRecordTable() {

        // Configure MedicalRecord table
        TableView.TableViewSelectionModel<MedicalRecord> selectionModel = MedicalRecordsTable.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);


        //Configure medicalRecords table columns
        MedicalRecordID.setCellValueFactory(new PropertyValueFactory<>("RecordID"));
        medical_record_name.setCellValueFactory(new PropertyValueFactory<>("PatientName"));
        medical_record_surname.setCellValueFactory(new PropertyValueFactory<>("PatientSurname"));
        medical_record_doc_surname.setCellValueFactory(new PropertyValueFactory<>("DoctorSurname"));
        record_diagnosis.setCellValueFactory(new PropertyValueFactory<>("Diagnosis"));
        record_treatment.setCellValueFactory(new PropertyValueFactory<>("Treatment"));
        record_prescription.setCellValueFactory(new PropertyValueFactory<>("Prescription"));
        record_status.setCellValueFactory(new PropertyValueFactory<>("Status"));
        record_date.setCellValueFactory(new PropertyValueFactory<>("RecordDate"));

        MedicalRecordsTable.setItems(medicalRecordsList);



        add_record_button.setOnAction(event -> handleAddRecord());
        modify_record_button.setOnAction(actionEvent -> handleUpdateRecord());
        delete_record_button.setOnAction(actionEvent -> handleDeleteRecord());
        view_medical_history_button.setOnAction(actionEvent -> handleViewMedicalHistory());



    }


    //refresh buttons

    private void initializePatientRefreshButton() {
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
        initializePatientRefreshButton();

        // Initialize appointment refresh button
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

        // Initialize medical records refresh button
        try {
            // Reuse the same icon resources for medical records
            staticRefreshIconRecords = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-static.png")));
            staticRefreshIconRecords.setFitHeight(20);
            staticRefreshIconRecords.setFitWidth(20);

            animatedRefreshIconRecords = new ImageView(new Image(getClass().getResourceAsStream("/com/inteliMedExpress/resources/images/refresh-animated.gif")));
            animatedRefreshIconRecords.setFitHeight(20);
            animatedRefreshIconRecords.setFitWidth(20);

            refresh_records_button.setGraphic(staticRefreshIconRecords);
            refresh_records_button.setContentDisplay(ContentDisplay.LEFT);
            refresh_records_button.setGraphicTextGap(8);
        } catch (Exception e) {
            System.err.println("Could not load medical records refresh icons: " + e.getMessage());
            // Continue without icons
        }

        // Set action for medical records refresh button
        refresh_records_button.setOnAction(event -> {
            refresh_records_button.setGraphic(animatedRefreshIconRecords != null ? animatedRefreshIconRecords : null);
            refresh_records_button.setDisable(true);

            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                loadAllMedicalRecords();
                refresh_records_button.setGraphic(staticRefreshIconRecords != null ? staticRefreshIconRecords : null);
                refresh_records_button.setDisable(false);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        });
    }



    //Enable-Disable Components


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



    //Medical Records dashboard
    private void disableMedicalRecordComponents() {
        MedicalRecordsTable.setVisible(false);
        add_record_button.setVisible(false);
        modify_record_button.setVisible(false);
        delete_record_button.setVisible(false);
        view_medical_history_button.setVisible(false);
        refresh_records_button.setVisible(false);
    }

    private void enableMedicalRecordComponents() {
        MedicalRecordsTable.setVisible(true);
        add_record_button.setVisible(true);
        modify_record_button.setVisible(true);
        delete_record_button.setVisible(true);
        view_medical_history_button.setVisible(true);
        refresh_records_button.setVisible(true);
    }









    // Patient Handlers

    private void loadPatientsFromServer() {
        // Create a background thread to fetch data
        new Thread(() -> {
            try {
                List<Patient> serverPatients = patientService.getAllPatients();
                Platform.runLater(() -> {
                    patientsList.clear();
                    patientsList.addAll(serverPatients);
                });
            } catch (IOException e) {
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






    //Appointment Handlers

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




    //Appointment Handlers



    private void handleCreateAppointment() {
        try {
            Stage stage = (Stage) create_appointment_button.getScene().getWindow();
            Appointment newAppointment = AppointmentDialog.showAddAppointmentDialog(stage);

            if (newAppointment != null) {
                boolean success = newAppointment.addToServer();
                if (success) {
                    UIHelper.showAlert("Success", "Appointment added successfully!");
                    loadAllAppointments(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to add appointment to the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error adding appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }




    private void handleModifyAppointment() {
        Appointment selectedAppointment = appointmentsViewTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            UIHelper.showAlert("Selection Required", "Please select an appointment to modify.");
            return;
        }

        try {
            Stage stage = (Stage) modify_appointment_button.getScene().getWindow();
            Appointment updatedAppointment = AppointmentDialog.showUpdateAppointmentDialog(stage, selectedAppointment);

            if (updatedAppointment != null) {
                boolean success = updatedAppointment.updateOnServer();
                if (success) {
                    UIHelper.showAlert("Success", "Appointment updated successfully!");
                    loadAllAppointments(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to update appointment on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error updating appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleDeleteAppointment() {
        Appointment selectedAppointment = appointmentsViewTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            UIHelper.showAlert("Selection Required", "Please select an appointment to delete.");
            return;
        }

        try {
            Stage stage = (Stage) delete_appointment_button.getScene().getWindow();
            boolean confirmed = AppointmentDialog.showDeleteConfirmationDialog(stage, selectedAppointment);

            if (confirmed) {
                boolean success = Appointment.deleteAppointment(selectedAppointment.getAppointmentId());
                if (success) {
                    UIHelper.showAlert("Success", "Appointment deleted successfully!");
                    loadAllAppointments(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to delete appointment from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting appointment: " + e.getMessage());
            e.printStackTrace();
        }
    }





    //Medical Records Handlers

    // Just one loadAllMedicalRecords method
    private void loadAllMedicalRecords() {
        new Thread(() -> {
            try {
                // Use MedicalRecordsService to fetch records
                List<MedicalRecord> serverMedicalRecords = medicalRecordsService.getAllMedicalRecords();

                // Update UI on the JavaFX application thread
                Platform.runLater(() -> {
                    medicalRecordsList.clear();
                    medicalRecordsList.addAll(serverMedicalRecords);
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    UIHelper.showAlert("Connection Error", "Failed to fetch medical records from server: " + e.getMessage());
                });
                e.printStackTrace();
            }
        }).start();
    }

    // Just one handleAddRecord method
    private void handleAddRecord() {
        try {
            Stage stage = (Stage) add_record_button.getScene().getWindow();
            MedicalRecord newRecord = MedicalRecordsDialog.showAddMedicalRecordDialog(stage);

            if (newRecord != null) {
                boolean success = medicalRecordsService.addMedicalRecord(newRecord);
                if (success) {
                    UIHelper.showAlert("Success", "Medical record added successfully!");
                    loadAllMedicalRecords(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to add medical record to the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error adding medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Just one handleUpdateRecord method
    private void handleUpdateRecord() {
        MedicalRecord selectedRecord = MedicalRecordsTable.getSelectionModel().getSelectedItem();

        if (selectedRecord == null) {
            UIHelper.showAlert("Selection Required", "Please select a medical record to update.");
            return;
        }

        try {
            Stage stage = (Stage) modify_record_button.getScene().getWindow();
            MedicalRecord updatedRecord = MedicalRecordsDialog.showUpdateMedicalRecordDialog(stage, selectedRecord);

            if (updatedRecord != null) {
                boolean success = medicalRecordsService.updateMedicalRecord(updatedRecord);
                if (success) {
                    UIHelper.showAlert("Success", "Medical record updated successfully!");
                    loadAllMedicalRecords(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to update medical record on the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error updating medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Just one handleDeleteRecord method
    private void handleDeleteRecord() {
        MedicalRecord selectedRecord = MedicalRecordsTable.getSelectionModel().getSelectedItem();

        if (selectedRecord == null) {
            UIHelper.showAlert("Selection Required", "Please select a medical record to delete.");
            return;
        }

        try {
            Stage stage = (Stage) delete_record_button.getScene().getWindow();
            boolean confirmed = MedicalRecordsDialog.showDeleteConfirmationDialog(stage, selectedRecord);

            if (confirmed) {
                boolean success = medicalRecordsService.deleteMedicalRecord(selectedRecord.getRecordId());
                if (success) {
                    UIHelper.showAlert("Success", "Medical record deleted successfully!");
                    loadAllMedicalRecords(); // Refresh the table
                } else {
                    UIHelper.showAlert("Error", "Failed to delete medical record from the server.");
                }
            }
        } catch (IOException e) {
            UIHelper.showAlert("Error", "Error deleting medical record: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void handleViewMedicalHistory() {
        MedicalRecord selectedRecord = MedicalRecordsTable.getSelectionModel().getSelectedItem();

        if (selectedRecord == null) {
            UIHelper.showAlert("Selection Required", "Please select a patient's medical record to view history.");
            return;
        }

        try {
            // Filter medical records by patient name and surname
            List<MedicalRecord> patientHistory = medicalRecordsList.stream()
                    .filter(record -> record.getPatientName().equals(selectedRecord.getPatientName()) &&
                            record.getPatientSurname().equals(selectedRecord.getPatientSurname()))
                    .sorted(Comparator.comparing(MedicalRecord::getRecordDate))
                    .collect(Collectors.toList());

            if (patientHistory.isEmpty()) {
                UIHelper.showAlert("No Records", "No medical history found for this patient.");
                return;
            }

            // Create a custom dialog
            Stage stage = (Stage) view_medical_history_button.getScene().getWindow();
            Dialog<Void> dialog = new Dialog<>();
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Patient Medical History Timeline");
            dialog.setHeaderText("Medical History for " + selectedRecord.getPatientName() + " " + selectedRecord.getPatientSurname());

            // Apply custom styling to the dialog
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(
                    MedicalRecordsDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());
            dialogPane.setPrefWidth(900);
            dialogPane.setPrefHeight(600);

            // Create a ScrollPane to contain the timeline
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.setPrefHeight(500);

            // Timeline container
            VBox timelineContainer = new VBox(20);
            timelineContainer.setPadding(new Insets(20));
            timelineContainer.setAlignment(Pos.CENTER);

            // Add a title for the timeline
            Label titleLabel = new Label("Medical Timeline");
            titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            timelineContainer.getChildren().add(titleLabel);

            // Create timeline visualization
            HBox timelineTrack = new HBox();
            timelineTrack.setAlignment(Pos.CENTER);
            timelineTrack.setSpacing(0);
            timelineTrack.setPadding(new Insets(10, 0, 30, 0));

            // Create a map to group records by month/year for simplified timeline
            Map<YearMonth, List<MedicalRecord>> recordsByMonth = new HashMap<>();
            for (MedicalRecord record : patientHistory) {
                YearMonth yearMonth = YearMonth.from(record.getRecordDate());
                recordsByMonth.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(record);
            }

            // Get the min and max dates for timeline bounds
            LocalDate minDate = patientHistory.get(0).getRecordDate();
            LocalDate maxDate = patientHistory.get(patientHistory.size() - 1).getRecordDate();

            // Create a sorted list of year-months for our timeline
            List<YearMonth> timelineMonths = new ArrayList<>();
            YearMonth current = YearMonth.from(minDate);
            YearMonth end = YearMonth.from(maxDate);

            while (!current.isAfter(end)) {
                timelineMonths.add(current);
                current = current.plusMonths(1);
            }

            // Create the visual timeline
            for (YearMonth month : timelineMonths) {
                boolean hasRecords = recordsByMonth.containsKey(month);

                // Create the timeline node
                VBox timelineNode = new VBox(5);
                timelineNode.setAlignment(Pos.TOP_CENTER);
                timelineNode.setPrefWidth(120);

                // Month label
                Label monthLabel = new Label(month.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " " + month.getYear());
                monthLabel.setStyle("-fx-font-size: 10px;");

                // Timeline node (circle or square)
                Region node;
                if (hasRecords) {
                    Circle circle = new Circle(10);
                    circle.setFill(Color.DODGERBLUE);
                    circle.setStroke(Color.BLACK);
                    circle.setStrokeWidth(1);

                    // Add record count badge if multiple records in same month
                    List<MedicalRecord> monthRecords = recordsByMonth.get(month);
                    if (monthRecords.size() > 1) {
                        StackPane stackPane = new StackPane();
                        stackPane.getChildren().add(circle);

                        Label countLabel = new Label(String.valueOf(monthRecords.size()));
                        countLabel.setStyle("-fx-font-size: 9px; -fx-text-fill: white; -fx-font-weight: bold;");
                        stackPane.getChildren().add(countLabel);

                        node = stackPane;
                    } else {
                        // Wrap the circle in a StackPane so it can be a Region
                        StackPane circleWrapper = new StackPane();
                        circleWrapper.getChildren().add(circle);
                        node = circleWrapper;
                    }
                } else {
                    // Empty timeline node needs to be wrapped too
                    Circle emptyCircle = new Circle(5);
                    emptyCircle.setFill(Color.LIGHTGRAY);
                    StackPane emptyWrapper = new StackPane();
                    emptyWrapper.getChildren().add(emptyCircle);
                    node = emptyWrapper;
                }

                // Connecting line
                Line line = new Line();
                line.setStartX(0);
                line.setEndX(120);
                line.setStroke(Color.LIGHTGRAY);

                // Add components to node
                timelineNode.getChildren().addAll(node, line, monthLabel);

                // Make nodes with records clickable to show details
                if (hasRecords) {
                    List<MedicalRecord> monthRecords = recordsByMonth.get(month);
                    final YearMonth finalMonth = month;
                    timelineNode.setOnMouseClicked(event -> showMonthRecordsDetails(finalMonth, monthRecords));
                    timelineNode.setCursor(Cursor.HAND);
                    timelineNode.setStyle("-fx-background-color: rgba(135, 206, 250, 0.1); -fx-background-radius: 5;");
                }

                timelineTrack.getChildren().add(timelineNode);
            }

            // Add timeline to container
            timelineContainer.getChildren().add(timelineTrack);

            // Add summary panel with most recent record details
            MedicalRecord mostRecentRecord = patientHistory.get(patientHistory.size() - 1);
            VBox summaryBox = createRecordSummaryPanel(mostRecentRecord, "Most Recent Record");
            timelineContainer.getChildren().add(summaryBox);

            // Add instructions
            Label instructionsLabel = new Label("Click on any blue node to view details for that month");
            instructionsLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #555555;");
            timelineContainer.getChildren().add(instructionsLabel);

            // Set the content
            scrollPane.setContent(timelineContainer);
            dialogPane.setContent(scrollPane);

            // Add close button
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            dialog.showAndWait();
        } catch (Exception e) {
            UIHelper.showAlert("Error", "Error viewing medical history: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Helper method to show details for a specific month
    private void showMonthRecordsDetails(YearMonth month, List<MedicalRecord> records) {
        Dialog<Void> detailsDialog = new Dialog<>();
        detailsDialog.setTitle("Medical Records: " +
                month.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + month.getYear());
        detailsDialog.setHeaderText("Patient: " + records.get(0).getPatientName() + " " + records.get(0).getPatientSurname());

        // Apply styling
        DialogPane dialogPane = detailsDialog.getDialogPane();
        dialogPane.getStylesheets().add(
                MedicalRecordsDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());

        // Content area
        VBox content = new VBox(15);
        content.setPadding(new Insets(10));

        // Sort records by date (most recent first)
        records.sort(Comparator.comparing(MedicalRecord::getRecordDate).reversed());

        // Add each record
        for (MedicalRecord record : records) {
            VBox recordSummary = createRecordSummaryPanel(record,
                    "Record Date: " + record.getRecordDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
            content.getChildren().add(recordSummary);
        }

        // Add to a scroll pane for larger history
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(600);

        dialogPane.setContent(scrollPane);
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);

        detailsDialog.showAndWait();
    }

    // Helper method to create a summary panel for a medical record
    private VBox createRecordSummaryPanel(MedicalRecord record, String title) {
        VBox recordBox = new VBox(8);
        recordBox.setPadding(new Insets(15));
        recordBox.setStyle("-fx-background-color: white; -fx-border-color: #cccccc; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        // Create a grid for record details
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(8);

        // Add record details
        int row = 0;

        addDetailRow(detailsGrid, row++, "Doctor:", "Dr. " + record.getDoctorSurname());
        addDetailRow(detailsGrid, row++, "Diagnosis:", record.getDiagnosis());
        addDetailRow(detailsGrid, row++, "Treatment:", record.getTreatment());
        addDetailRow(detailsGrid, row++, "Prescription:", record.getPrescription());

        // Status with color coding
        Label statusLabel = new Label("Status:");
        statusLabel.setStyle("-fx-font-weight: bold;");

        Label statusValue = new Label(record.getStatus());
        String statusColor;
        switch (record.getStatus().toLowerCase()) {
            case "active":
                statusColor = "forestgreen";
                break;
            case "pending":
                statusColor = "darkorange";
                break;
            case "completed":
                statusColor = "royalblue";
                break;
            case "canceled":
                statusColor = "crimson";
                break;
            default:
                statusColor = "black";
        }
        statusValue.setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold;");

        detailsGrid.add(statusLabel, 0, row);
        detailsGrid.add(statusValue, 1, row);

        // Add components to panel
        recordBox.getChildren().addAll(titleLabel, detailsGrid);

        return recordBox;
    }

    // Helper method to add a row to the details grid
    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.setStyle("-fx-font-weight: bold;");

        Label valueNode = new Label(value != null && !value.isEmpty() ? value : "N/A");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }




    /*


    private void handleViewMedicalHistory() {
        MedicalRecord selectedRecord = MedicalRecordsTable.getSelectionModel().getSelectedItem();

        if (selectedRecord == null) {
            UIHelper.showAlert("Selection Required", "Please select a patient's medical record to view history.");
            return;
        }

        try {
            // Filter medical records by patient name and surname
            List<MedicalRecord> patientHistory = medicalRecordsList.stream()
                    .filter(record -> record.getPatientName().equals(selectedRecord.getPatientName()) &&
                            record.getPatientSurname().equals(selectedRecord.getPatientSurname()))
                    .collect(Collectors.toList());

            if (patientHistory.isEmpty()) {
                UIHelper.showAlert("No Records", "No medical history found for this patient.");
                return;
            }

            // Create a simple dialog to display patient history
            Stage stage = (Stage) view_medical_history_button.getScene().getWindow();
            Dialog<Void> dialog = new Dialog<>();
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(stage);
            dialog.setTitle("Patient Medical History");
            dialog.setHeaderText("Medical History for " + selectedRecord.getPatientName() + " " + selectedRecord.getPatientSurname());

            // Apply custom styling to the dialog
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getStylesheets().add(
                    MedicalRecordsDialog.class.getResource("/com/inteliMedExpress/resources/css/patient_dialogs.css").toExternalForm());

            // Create a TableView to display the history
            TableView<MedicalRecord> historyTable = new TableView<>();
            historyTable.setPrefWidth(800);
            historyTable.setPrefHeight(400);

            // Define columns
            TableColumn<MedicalRecord, LocalDate> dateColumn = new TableColumn<>("Date");
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
            dateColumn.setPrefWidth(100);

            TableColumn<MedicalRecord, String> diagnosisColumn = new TableColumn<>("Diagnosis");
            diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
            diagnosisColumn.setPrefWidth(150);

            TableColumn<MedicalRecord, String> treatmentColumn = new TableColumn<>("Treatment");
            treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatment"));
            treatmentColumn.setPrefWidth(150);

            TableColumn<MedicalRecord, String> prescriptionColumn = new TableColumn<>("Prescription");
            prescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("prescription"));
            prescriptionColumn.setPrefWidth(150);

            TableColumn<MedicalRecord, String> doctorColumn = new TableColumn<>("Doctor");
            doctorColumn.setCellValueFactory(new PropertyValueFactory<>("doctorSurname"));
            doctorColumn.setPrefWidth(100);

            TableColumn<MedicalRecord, String> statusColumn = new TableColumn<>("Status");
            statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
            statusColumn.setPrefWidth(100);

            historyTable.getColumns().addAll(dateColumn, diagnosisColumn, treatmentColumn,
                    prescriptionColumn, doctorColumn, statusColumn);

            // Sort by date descending (newest first)
            historyTable.getSortOrder().add(dateColumn);
            dateColumn.setSortType(TableColumn.SortType.DESCENDING);

            // Add all records to the table
            historyTable.getItems().addAll(patientHistory);
            historyTable.sort();

            dialogPane.setContent(historyTable);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

            dialog.showAndWait();
        } catch (Exception e) {
            UIHelper.showAlert("Error", "Error viewing medical history: " + e.getMessage());
            e.printStackTrace();
        }
    }


**/



}




